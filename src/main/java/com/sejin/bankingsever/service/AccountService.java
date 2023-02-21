package com.sejin.bankingsever.service;

import com.sejin.bankingsever.exception.AccountNotFoundException;
import com.sejin.bankingsever.exception.InsufficientBalanceException;
import com.sejin.bankingsever.exception.UserNotFoundException;
import com.sejin.bankingsever.model.Account;
import com.sejin.bankingsever.model.User;
import com.sejin.bankingsever.repository.AccountRepository;
import com.sejin.bankingsever.repository.UserRepository;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Account createAccount(Long userId, String accountNumber) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("찾을 수 없는 유저 : " + userRepository.findById(userId)));

        Account account = new Account(accountNumber, user);

        return accountRepository.save(account);
    }
}
