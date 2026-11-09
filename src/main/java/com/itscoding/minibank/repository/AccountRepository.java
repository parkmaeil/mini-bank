package com.itscoding.minibank.repository;
 
import com.itscoding.minibank.domain.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
 
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)       // SELECT ... FOR UPDATE
    @Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber")
    Optional<Account> findByAccountNumberForUpdate(@Param("accountNumber") String accountNumber);

    // 6.2절 고객의 계좌 수와 보유 여부 — Customer가 계좌 목록을 모르므로 계좌 쪽에 묻는다
    long countByCustomerId(Long customerId);      // SELECT COUNT(...) ... WHERE customer_id = ?

    boolean existsByCustomerId(Long customerId);  // 한 건이라도 있으면 true — 전부 세지 않는다

}
