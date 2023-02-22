package com.sejin.bankingsever;

import com.sejin.bankingsever.exception.InsufficientBalanceException;
import com.sejin.bankingsever.model.Account;
import com.sejin.bankingsever.model.User;
import com.sejin.bankingsever.service.AccountService;
import com.sejin.bankingsever.service.UserService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class AccountTests {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;
    private Long userId;
    private String accountNumber;

    final String USER_EMAIL = "test";
    final String PASS_WORD = "pw_test";
    final String FRIEND_EMAIL = "freind_test";

    @BeforeEach
    void setUp() {
        User user = userService.createUser(USER_EMAIL, PASS_WORD);
        userId = user.getUserId();
        accountNumber = "123-456-789";
    }

    @Test
    @DisplayName("계좌 생성 테스트")
    void createAccountTest() {
        // when
        Account account = accountService.createAccount(userId, accountNumber);

        // then
        Assertions.assertNotNull(account);
        Assertions.assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    @DisplayName("입금 테스트")
    void addBalanceTest() {
        // given
        accountService.createAccount(userId, accountNumber);

        // when
        BigDecimal balanceToAdd = BigDecimal.valueOf(1000);
        accountService.addBalance(userId, balanceToAdd);

        // then
        BigDecimal expectedBalance = BigDecimal.valueOf(1000);
        BigDecimal actualBalance = accountService.getBalance(userId);
        Assertions.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    @DisplayName("잔액 조회 테스트")
    void getBalanceTest() {
        // given
        accountService.createAccount(userId, accountNumber);
        BigDecimal balanceToAdd = BigDecimal.valueOf(1000);
        accountService.addBalance(userId, balanceToAdd);

        // when
        BigDecimal actualBalance = accountService.getBalance(userId);

        // then
        BigDecimal expectedBalance = BigDecimal.valueOf(1000);
        Assertions.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    @DisplayName("송금 테스트")
    void transferTest() {
        //given
        accountService.createAccount(userId, accountNumber);
        User toUser = userService.createUser(FRIEND_EMAIL, PASS_WORD);
        Long toId = toUser.getUserId();
        accountService.createAccount(toId, "1234");
        accountService.addBalance(userId, BigDecimal.valueOf(100));

        //when
        accountService.transfer(userId, toId, BigDecimal.valueOf(100));

        //then
        BigDecimal fromAccountBalance = accountService.getBalance(userId);
        BigDecimal toAccountBalance = accountService.getBalance(toId);
        Assertions.assertEquals(fromAccountBalance, BigDecimal.valueOf(0));
        Assertions.assertEquals(toAccountBalance, BigDecimal.valueOf(100));
    }

    @Test
    @DisplayName("송금 테스트 (실패 : )")
    void transferErrorTest() {
        //given
        accountService.createAccount(userId, accountNumber);
        User toUser = userService.createUser(FRIEND_EMAIL, PASS_WORD);
        Long toId = toUser.getUserId();
        BigDecimal balance = BigDecimal.valueOf(10000);
        accountService.createAccount(toId, "1234");

        // when, then
        Assertions.assertThrows(InsufficientBalanceException.class,
            () -> accountService.transfer(userId, toId, balance));
    }
}
