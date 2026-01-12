package com.example.banking.service;

import com.example.banking.dto.IncomingTransactionDTO;

public interface ITransactionService {
    void processTransaction(IncomingTransactionDTO dto);

}
