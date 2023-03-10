package com.sejin.bankingsever.repository;

import com.sejin.bankingsever.model.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUserUserId(Long userId);
    boolean existsByUserUserId(Long userId);

}
