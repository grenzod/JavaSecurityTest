package com.example.banking.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Converter
@RequiredArgsConstructor
public class AccountAttributeConverter implements AttributeConverter<String, String> {

    private final AESCrypto aesCrypto;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            return aesCrypto.encrypt(attribute); // Tự động mã hóa khi save()
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting account", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return aesCrypto.decrypt(dbData); // Tự động giải mã khi get()
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting account", e);
        }
    }
}
