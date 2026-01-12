package com.example.banking.repository;

import com.example.banking.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
}
