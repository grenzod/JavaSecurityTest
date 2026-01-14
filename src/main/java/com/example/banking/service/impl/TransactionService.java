package com.example.banking.service.impl;

import com.example.banking.config.RSACrypto;
import com.example.banking.dto.IncomingTransactionDTO;
import com.example.banking.entity.TransactionHistory;
import com.example.banking.exception.TransactionException;
import com.example.banking.repository.TransactionHistoryRepository;
import com.example.banking.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final TransactionHistoryRepository repository;
    private final RSACrypto rsaCrypto;

    @Transactional
    public void processTransaction(IncomingTransactionDTO dto) {
        String transId = dto.getTransactionIdEnc();
        String fromAcc = dto.getFromAccountEnc();
        String toAcc = dto.getToAccountEnc();
        String amountStr = dto.getAmountEnc();
        String timeStr = dto.getTimeEnc();

        try {
            // Giải mã RSA
            transId = rsaCrypto.decrypt(dto.getTransactionIdEnc());
            fromAcc = rsaCrypto.decrypt(dto.getFromAccountEnc());
            toAcc = rsaCrypto.decrypt(dto.getToAccountEnc());
            amountStr = rsaCrypto.decrypt(dto.getAmountEnc());
            timeStr = rsaCrypto.decrypt(dto.getTimeEnc());

            BigDecimal amount = new BigDecimal(amountStr);
            LocalDateTime now = LocalDateTime.now();

            TransactionHistory debitRecord = TransactionHistory.builder()
                    .transactionId(transId)
                    .account(fromAcc) // Tự động mã hóa AES ở tầng Entity
                    .inDebt(amount)
                    .have(BigDecimal.ZERO)
                    .time(now)
                    .build();

            TransactionHistory creditRecord = TransactionHistory.builder()
                    .transactionId(transId)
                    .account(toAcc) // Tự động mã hóa AES ở tầng Entity
                    .inDebt(BigDecimal.ZERO)
                    .have(amount)
                    .time(now)
                    .build();

            repository.save(debitRecord);
            repository.save(creditRecord);

        } catch (Exception e) {
            throw new TransactionException(
                    "Error processing transaction",
                    e,
                    transId,
                    fromAcc + " -> " + toAcc,
                    amountStr,
                    amountStr,
                    timeStr
            );
        }
    }
}