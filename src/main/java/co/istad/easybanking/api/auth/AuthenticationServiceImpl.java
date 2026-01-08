package co.istad.easybanking.api.auth;

import co.istad.easybanking.jwtGenerater.JwtTokenService;
import co.istad.easybanking.api.user.User;
import co.istad.easybanking.api.user.UserDto;
import co.istad.easybanking.api.user.UserMapper;
import co.istad.easybanking.api.user.UserRepository;
import co.istad.easybanking.api.user.UserService;
import co.istad.easybanking.base.BaseSuccess;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final DaoAuthenticationProvider daoAuthenticationProvider;

    @Override
    public void register(RegisterDto registerDto) {
        try {
            UserDto userDto = userMapper.fromRegisterDto(registerDto);
            userService.createNewUser(userDto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getLocalizedMessage());
        }
    }
    @Override
    public String verifyToken(TokenDto tokenDto) {
        try {
            System.out.println("in verify");
            Authentication auth = new BearerTokenAuthenticationToken(tokenDto.token());
            Authentication authentication = jwtAuthenticationProvider.authenticate(auth);
            System.out.println(authentication.getAuthorities());
            System.out.println(authentication.getName());
            System.out.println(authentication.isAuthenticated());
            if (authentication.isAuthenticated()) {
                return "Token Valid";
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Refresh Token not correct");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void sendEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        Context context = new Context();
        context.setVariable("user", user);
    }
    @Override
    public AuthDto<?> login(LoginDto loginDto) {
        try {
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    loginDto.username(), loginDto.pwd());
            auth = daoAuthenticationProvider.authenticate(auth);
            String accessToken = jwtTokenService.generateAccessToken(auth);
            String refreshToken = jwtTokenService.generateRefreshToken(auth);
            System.out.println(auth.getAuthorities());
            return AuthDto.builder()
                    .accessToken(accessToken)
                    .code(HttpStatus.OK.value())
                    .message("Successfully")
                    .timestamp(LocalDateTime.now())
                    .status(true)
                    .refreshToken(refreshToken)
                    .build();
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,e.getLocalizedMessage());
        }
    }
    @Override
    public BaseSuccess<?> changPassword(ChangPDto changPDto) {
        User foundUser = userRepository.findByUserNameAndIsEnabled(changPDto.username(), true)
                .orElseThrow(
                        ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username not found")
                );
        if (!Objects.equals(changPDto.pwd(), foundUser.getPassword())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Password not correct");
        }
        if(!Objects.equals(changPDto.email(), foundUser.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"User email not correct");
        }
        if(!Objects.equals(changPDto.newPass(), changPDto.ConfirmPass())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"New password not match");
        }
        foundUser.setPassword(changPDto.newPass());
        userRepository.save(foundUser);
        return BaseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully Changed")
                .status(true)
                .build();
    }
    @Override
    public BaseSuccess<?> resetPassword(ResetPasswordDto resetPasswordDto) {

        User foundUser = userRepository.findByUserNameAndIsEnabled(resetPasswordDto.username(),true).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found")
        );
        if(!Objects.equals(resetPasswordDto.email(), foundUser.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User email not found");
        }
        return null;
    }
    @Override
    public Boolean confirmOtp(Long id, Integer opt) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT,"User Id not found")
        );
        return Objects.equals(user.getOtp(), opt);
    }

}
