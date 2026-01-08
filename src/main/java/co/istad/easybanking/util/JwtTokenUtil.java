package co.istad.easybanking.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${keys.access-private-token}")
    private String secretKey;
    @Value("${keys.access-public-token}")
    private String publicKey;
    @Value("${jwt.token-validity}")
    private long tokenValidity;
    private KeyPair accessTokenKeyPair;
    private KeyPair accessRefreshTokenKeyPair;


    //Check if key pair for token have or not
    public KeyPair getAccessTokenKeyPair() {

        if (Objects.isNull(accessTokenKeyPair)) {
            //if key pair not exists then use method getKeyPair to generate key pair.
            accessTokenKeyPair = this.getKeyPair(publicKey, secretKey);
        }
        return accessTokenKeyPair;
    }

    // Check if key pair for token have or not
    public KeyPair getAccessRefreshTokenKeyPair() {

        if (Objects.isNull(accessRefreshTokenKeyPair)) {
            // if key pair not exists then use method getKeyPair to generate key pair.
            accessRefreshTokenKeyPair = this.getKeyPair(publicKey, secretKey);
        }
        return accessRefreshTokenKeyPair;
    }

    //Take Secret Key with Type RSA:
    public RSAPublicKey getAccessTokenPublicKey() {
        return (RSAPublicKey) getAccessTokenKeyPair().getPublic();
    }
    public RSAPrivateKey getAccessTokenPrivateKey() {
        return (RSAPrivateKey) getAccessTokenKeyPair().getPrivate();
    }

    //Take Secret Key with Type RSA for refresh Token:
    public RSAPublicKey getRefreshTokenPublicKey() {
        return (RSAPublicKey) getAccessRefreshTokenKeyPair().getPublic();
    }

    public RSAPrivateKey getRefreshTokenPrivateKey() {
        return (RSAPrivateKey) getAccessRefreshTokenKeyPair().getPrivate();
    }


    //Generate user token:
    private KeyPair getKeyPair(String publicKeyPath, String privateKeyPath) {
        if (publicKeyPath == null || privateKeyPath == null || publicKeyPath.isEmpty() || privateKeyPath.isEmpty()) {
            throw new IllegalArgumentException("Public key path or private key path is null or empty");
        }

        KeyPair keyPair;

        File publicKeyFile = new File(publicKeyPath);
        File privateKeyFile = new File(privateKeyPath);

        if (publicKeyFile.exists() && privateKeyFile.exists()) {

            //check if two path public and private exists then work on generate key :

            try {

                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
                EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
                PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

                byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

                keyPair = new KeyPair(publicKey, privateKey);

                return keyPair;

            } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
        }

        //check if directory keys is exists or not:
        File directory = new File("keys");

        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created){
                System.out.println("created");
            }else {
                System.out.println("created");
            }
        }

        try {

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();

            try (FileOutputStream fos = new FileOutputStream(publicKeyPath)) {
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
                fos.write(keySpec.getEncoded());
            }

            try (FileOutputStream fos = new FileOutputStream(privateKeyPath)) {
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
                fos.write(keySpec.getEncoded());
            }

        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }

        return keyPair;
    }
}
