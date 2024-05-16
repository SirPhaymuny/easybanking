package co.istad.easybanking.jwtGenerater;

import co.istad.easybanking.api.transaction.FundToken;
import co.istad.easybanking.util.JwtTokenUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

        private final JwtTokenUtil jwtTokenUtil;
        private final JwtEncoder jwtAccessTokenEncoder;
        private final JwtEncoder jwtRefreshTokenEncoder;
        public String generateAccessToken(Authentication authentication) {
                Instant instantNow = Instant.now();
                String authorities = authentication.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(" "));
                System.out.println(authorities);
                JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                                .id(authentication.getName())
                                .issuer(authentication.getName())
                                .issuedAt(instantNow)
                                .claim("authorities", authorities)
                                .subject("Access Token")
                                .expiresAt(Instant.now().plus(1, ChronoUnit.MINUTES))
                                .build();
                return jwtAccessTokenEncoder.encode(
                                JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        }
        public String generateFundToken(FundToken fundTransferDto, String subject) {
                Instant instantNow = Instant.now();
                JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                                .id(fundTransferDto.username())
                                .issuer(fundTransferDto.username())
                                .issuedAt(instantNow)
                                .claim("Associated",
                                                fundTransferDto.debitAccount() + "|" + fundTransferDto.creditAccount()
                                                                + "|" + fundTransferDto.amount() +
                                                                "|" + fundTransferDto.username())
                                .subject("Funds Token")
                                .expiresAt(Instant.now().plus(1, ChronoUnit.MINUTES))
                                .build();
                return jwtAccessTokenEncoder.encode(
                                JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        }

        public String generateRefreshToken(Authentication authentication) {
                Instant instantNow = Instant.now();
                JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                                .id(authentication.getName())
                                .issuer(authentication.getName())
                                .issuedAt(instantNow)
                                .subject("Refresh Token")
                                .expiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
                                .build();
                return jwtRefreshTokenEncoder.encode(
                                JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        }
        public JWTClaimsSet JwtValidate(String token) throws ParseException, JOSEException {
                // take string token and Verify it keys
                try {
                        SignedJWT signedJWT = SignedJWT.parse(token);
                        JWSVerifier verifier = new RSASSAVerifier(jwtTokenUtil.getAccessTokenPublicKey());
                        boolean valid = signedJWT.verify(verifier);
                        if (!valid) {
                                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token");
                        } else {
                                JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
                                if (isTokenExpired(jwtClaimsSet)) {
                                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                                        "Jwt already expired");
                                } else {
                                        return signedJWT.getJWTClaimsSet();
                                }
                        }
                } catch (JOSEException e) {
                        throw new JOSEException("Error validating token", e);
                }
        }
        public boolean isTokenExpired(JWTClaimsSet claimsSet) {
                // check expired Token Time if it true it mean that already expired
                Instant expirationTime = claimsSet.getExpirationTime().toInstant();
                return Instant.now().isAfter(expirationTime);
        }
}
