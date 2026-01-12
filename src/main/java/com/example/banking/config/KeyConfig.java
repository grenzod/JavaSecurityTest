package com.example.banking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;

@Configuration
public class KeyConfig {

    @Value("${security.keystore.location}")
    private Resource keystore;

    @Value("${security.keystore.password}")
    private String storePassword;

    @Value("${security.keystore.alias}")
    private String keyAlias;

    @Bean
    public KeyPair rsaKeyPair() throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        try (InputStream is = keystore.getInputStream()) {
            ks.load(is, storePassword.toCharArray());
        }
        Key key = ks.getKey(keyAlias, storePassword.toCharArray());
        Certificate cert = ks.getCertificate(keyAlias);
        PublicKey publicKey = cert.getPublicKey();
        PrivateKey privateKey = (PrivateKey) key;
        return new KeyPair(publicKey, privateKey);
    }
}

