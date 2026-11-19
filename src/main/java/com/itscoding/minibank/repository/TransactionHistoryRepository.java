package com.itscoding.minibank.repository;

import com.itscoding.minibank.domain.TransactionHistory;
import com.itscoding.minibank.domain.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

    List<TransactionHistory> findByAccountIdOrderByTransactedAtDesc(Long accountId);

    // 12.7절 기간 검색과 페이징 — start 이상, end 이하 (Between)
    Page<TransactionHistory> findByAccountIdAndTransactedAtBetween(
            Long accountId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    // 🔥 13장 오늘 출금 합계를 구하기 위한 조회 (계좌 + 구분 + 기간)
    List<TransactionHistory> findByAccountIdAndTypeAndTransactedAtBetween(
            Long accountId, TransactionType type, LocalDateTime start, LocalDateTime end);
}