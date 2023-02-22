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
    public Account createAccount(
        Long userId,
        String accountNumber
    ) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("찾을 수 없는 유저 : " + userRepository.findById(userId)));

        Account account = new Account(accountNumber, user);
        account.setBalance(BigDecimal.ZERO);

        return accountRepository.save(account);
    }
    @Transactional
    public void addBalance(
        Long userId,
        BigDecimal balance
    ) {
        Account account = accountRepository
            .findByUserUserId(userId)
            .orElseThrow(AccountNotFoundException::new);
        account.setBalance(account.getBalance().add(balance));
        accountRepository.save(account);
    }

    @Transactional
    public BigDecimal getBalance(Long userId) {
        Account account = accountRepository
            .findByUserUserId(userId)
            .orElseThrow(AccountNotFoundException::new);

        return account.getBalance();
    }


    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void transfer(
        Long fromUserId,
        Long toUserId,
        BigDecimal balance
    ) {
        Account fromAccount = accountRepository
            .findByUserUserId(fromUserId)
            .orElseThrow(AccountNotFoundException::new);
        Account toAccount = accountRepository
            .findByUserUserId(toUserId)
            .orElseThrow(AccountNotFoundException::new);

        if (fromAccount.getBalance().compareTo(balance) < 0) {
            throw new InsufficientBalanceException("잔액이 부족합니다");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(balance));
        toAccount.setBalance(toAccount.getBalance().add(balance));
    }
}
