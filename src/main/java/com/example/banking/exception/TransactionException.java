package com.example.banking.exception;

import lombok.Getter;

@Getter
public class TransactionException extends RuntimeException {

    private final String transactionId;
    private final String account;
    private final String inDebt;
    private final String have;
    private final String time;

    public TransactionException(String message, Throwable cause,
                                String transactionId, String account,
                                String inDebt, String have, String time) {
        super(message, cause);
        this.transactionId = transactionId;
        this.account = account;
        this.inDebt = inDebt;
        this.have = have;
        this.time = time;
    }
}
