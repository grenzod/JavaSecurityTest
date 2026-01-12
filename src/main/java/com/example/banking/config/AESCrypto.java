package com.example.banking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AESCrypto {

    private final String algorithm;
    private final int tagLengthBit;
    private final int ivLengthByte;
    private final SecretKey key;

    public AESCrypto(
            @Value("${security.aes.key}") String base64Key,
            @Value("${security.aes.algorithm:AES/GCM/NoPadding}") String algorithm,
            @Value("${security.aes.tag-length:128}") int tagLengthBit,
            @Value("${security.aes.iv-length:12}") int ivLengthByte
    ) {
        this.algorithm = algorithm;
        this.tagLengthBit = tagLengthBit;
        this.ivLengthByte = ivLengthByte;

        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        if (decodedKey.length != 16 && decodedKey.length != 24 && decodedKey.length != 32) {
            throw new IllegalArgumentException("Invalid AES Key length: " + decodedKey.length + " bytes. Must be 16, 24, or 32 bytes.");
        }
        this.key = new SecretKeySpec(decodedKey, "AES");
    }

    public String encrypt(String plaintext) throws Exception {
        byte[] iv = new byte[this.ivLengthByte];
        SecureRandom.getInstanceStrong().nextBytes(iv);

        Cipher cipher = Cipher.getInstance(this.algorithm);

        GCMParameterSpec spec = new GCMParameterSpec(this.tagLengthBit, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] cipherText = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipherText);

        return Base64.getEncoder().encodeToString(byteBuffer.array());
    }

    public String decrypt(String base64CipherResponse) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(base64CipherResponse);

        ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);

        byte[] iv = new byte[this.ivLengthByte];
        byteBuffer.get(iv);

        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);

        Cipher cipher = Cipher.getInstance(this.algorithm);
        GCMParameterSpec spec = new GCMParameterSpec(this.tagLengthBit, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] plainText = cipher.doFinal(cipherText);
        return new String(plainText, StandardCharsets.UTF_8);
    }
}