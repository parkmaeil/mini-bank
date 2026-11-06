package com.itscoding.minibank.repository;
 
import com.itscoding.minibank.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
 
import java.util.Optional;
 
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
}
