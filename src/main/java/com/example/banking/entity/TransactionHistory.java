package com.example.banking.entity;

import com.example.banking.config.AccountAttributeConverter;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TransactionHistory")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TransactionID", nullable = false)
    private String transactionId;

    // Tự động Mã hóa AES khi lưu, Giải mã khi lấy
    @Convert(converter = AccountAttributeConverter.class)
    @Column(name = "Account", nullable = false, length = 500)
    private String account;

    @Column(name = "InDebt")
    private BigDecimal inDebt;

    @Column(name = "Have")
    private BigDecimal have;

    @Column(name = "Time")
    private LocalDateTime time;
}
