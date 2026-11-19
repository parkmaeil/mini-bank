package com.itscoding.minibank.repository;

import com.itscoding.minibank.domain.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    List<TransactionHistory> findByAccountIdOrderByTransactedAtDesc(Long accountId);
}