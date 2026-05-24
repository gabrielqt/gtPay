package com.gabrielqt.gtpay.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class SecretService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";

    private final SecretKey secretKey;

    public SecretService(
            @Value("${app.security.secret-key}")
            String base64Key
    ) {

        byte[] keyBytes = Base64.getDecoder().decode(base64Key);

        this.secretKey = new SecretKeySpec(keyBytes, "AES");
    }

    public String generateSecret() {

        SecureRandom random = new SecureRandom();

        byte[] bytes = new byte[32];

        random.nextBytes(bytes);

        return "whsec_" +
                Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(bytes);
    }

    public String encryptSecret(String value) {

        try {

            byte[] iv = new byte[12];

            SecureRandom random = new SecureRandom();

            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);

            GCMParameterSpec spec =
                    new GCMParameterSpec(128, iv);

            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    secretKey,
                    spec
            );

            byte[] encryptedBytes =
                    cipher.doFinal(
                            value.getBytes(StandardCharsets.UTF_8)
                    );

            byte[] combined =
                    ByteBuffer.allocate(iv.length + encryptedBytes.length)
                            .put(iv)
                            .put(encryptedBytes)
                            .array();

            return Base64.getEncoder()
                    .encodeToString(combined);

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Error encrypting secret",
                    ex
            );
        }
    }

    public String decryptSecret(String encryptedValue) {

        try {

            byte[] combined =
                    Base64.getDecoder()
                            .decode(encryptedValue);

            ByteBuffer buffer =
                    ByteBuffer.wrap(combined);

            byte[] iv = new byte[12];

            buffer.get(iv);

            byte[] encryptedBytes =
                    new byte[buffer.remaining()];

            buffer.get(encryptedBytes);

            Cipher cipher =
                    Cipher.getInstance(ALGORITHM);

            GCMParameterSpec spec =
                    new GCMParameterSpec(128, iv);

            cipher.init(
                    Cipher.DECRYPT_MODE,
                    secretKey,
                    spec
            );

            byte[] decrypted =
                    cipher.doFinal(encryptedBytes);

            return new String(
                    decrypted,
                    StandardCharsets.UTF_8
            );

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Error decrypting secret",
                    ex
            );
        }
    }
}

