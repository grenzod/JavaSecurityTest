package com.example.banking.dto;

import lombok.Data;

@Data
public class IncomingTransactionDTO {
    private String transactionIdEnc;
    private String fromAccountEnc;
    private String toAccountEnc;
    private String amountEnc;
    private String timeEnc;
}
