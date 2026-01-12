package com.example.banking.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class RSACrypto {
    private final KeyPair keyPair;

    public String encrypt(String plaintext) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }
        catch (Exception e) {
            throw new RuntimeException("RSA Encrypt Error", e);
        }
    }

    public String decrypt(String encryptedBase64) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] decoded = Base64.getDecoder().decode(encryptedBase64);
            return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("RSA Decrypt Error", e);
        }
    }
}
