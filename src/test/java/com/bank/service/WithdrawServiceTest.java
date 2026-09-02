package com.bank.service;

import com.bank.config.ConnectionProvider;
import com.bank.dao.AccountDAO;
import com.bank.dao.TransactionDAO;
import com.bank.model.Account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class WithdrawServiceTest {

    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private ConnectionProvider connectionProvider;
    private Connection connection;

    private WithdrawService withdrawService;

    @BeforeEach
    void setUp() throws Exception {

        accountDAO = mock(AccountDAO.class);
        transactionDAO = mock(TransactionDAO.class);
        connectionProvider = mock(ConnectionProvider.class);
        connection = mock(Connection.class);

        when(connectionProvider.getConnection())
                .thenReturn(connection);

        withdrawService =
                new WithdrawService(
                        accountDAO,
                        transactionDAO,
                        connectionProvider);
    }

    @Test
    void withdraw_shouldReturnFalse_whenAccountNotFound()
            throws Exception {

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(null);

        boolean result =
                withdrawService.withdraw(
                        1,
                        new BigDecimal("1000"),
                        "Test withdrawal");

        assertFalse(result);

        verify(connection).rollback();

        verify(accountDAO)
                .getAccountById(connection, 1);

        verifyNoInteractions(transactionDAO);

        verify(connection).close();
    }

    @Test
    void withdraw_shouldReturnFalse_whenAccountInactive()
            throws Exception {

        Account account = new Account();

        account.setStatus("INACTIVE");
        account.setBalance(new BigDecimal("5000"));

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(account);

        boolean result =
                withdrawService.withdraw(
                        1,
                        new BigDecimal("1000"),
                        "Test withdrawal");

        assertFalse(result);

        verify(connection).rollback();

        verifyNoInteractions(transactionDAO);

        verify(connection).close();
    }

    @Test
    void withdraw_shouldReturnFalse_whenAmountIsZero()
            throws Exception {

        Account account = new Account();

        account.setStatus("ACTIVE");
        account.setBalance(new BigDecimal("5000"));

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(account);

        boolean result =
                withdrawService.withdraw(
                        1,
                        BigDecimal.ZERO,
                        "Test withdrawal");

        assertFalse(result);

        verify(connection).rollback();

        verifyNoInteractions(transactionDAO);

        verify(connection).close();
    }

    @Test
    void withdraw_shouldReturnFalse_whenAmountIsNegative()
            throws Exception {

        Account account = new Account();

        account.setStatus("ACTIVE");
        account.setBalance(new BigDecimal("5000"));

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(account);

        boolean result =
                withdrawService.withdraw(
                        1,
                        new BigDecimal("-100"),
                        "Test withdrawal");

        assertFalse(result);

        verify(connection).rollback();

        verifyNoInteractions(transactionDAO);

        verify(connection).close();
    }

    @Test
    void withdraw_shouldReturnFalse_whenInsufficientBalance()
            throws Exception {

        Account account = new Account();

        account.setStatus("ACTIVE");
        account.setBalance(new BigDecimal("500"));

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(account);

        boolean result =
                withdrawService.withdraw(
                        1,
                        new BigDecimal("1000"),
                        "Test withdrawal");

        assertFalse(result);

        verify(connection).rollback();

        verify(accountDAO, never())
                .updateBalance(
                        any(),
                        anyInt(),
                        any());

        verifyNoInteractions(transactionDAO);

        verify(connection).close();
    }

    @Test
    void withdraw_shouldReturnFalse_whenBalanceUpdateFails()
            throws Exception {

        Account account = new Account();

        account.setStatus("ACTIVE");
        account.setBalance(new BigDecimal("5000"));

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(account);

        when(accountDAO.updateBalance(
                eq(connection),
                eq(1),
                eq(new BigDecimal("4000"))))
                .thenReturn(false);

        boolean result =
                withdrawService.withdraw(
                        1,
                        new BigDecimal("1000"),
                        "Test withdrawal");

        assertFalse(result);

        verify(accountDAO)
                .updateBalance(
                        connection,
                        1,
                        new BigDecimal("4000"));

        verify(connection).rollback();

        verifyNoInteractions(transactionDAO);

        verify(connection).close();
    }

    @Test
    void withdraw_shouldReturnFalse_whenTransactionInsertFails()
            throws Exception {

        Account account = new Account();

        account.setStatus("ACTIVE");
        account.setBalance(new BigDecimal("5000"));

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(account);

        when(accountDAO.updateBalance(
                eq(connection),
                eq(1),
                eq(new BigDecimal("4000"))))
                .thenReturn(true);

        when(transactionDAO.saveTransaction(
                eq(connection),
                any()))
                .thenReturn(false);

        boolean result =
                withdrawService.withdraw(
                        1,
                        new BigDecimal("1000"),
                        "Test withdrawal");

        assertFalse(result);

        verify(transactionDAO)
                .saveTransaction(
                        eq(connection),
                        any());

        verify(connection).rollback();

        verify(connection, never()).commit();

        verify(connection).close();
    }

    @Test
    void withdraw_shouldReturnTrue_whenSuccessful()
            throws Exception {

        Account account = new Account();

        account.setStatus("ACTIVE");
        account.setBalance(new BigDecimal("5000"));

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(account);

        when(accountDAO.updateBalance(
                eq(connection),
                eq(1),
                eq(new BigDecimal("4000"))))
                .thenReturn(true);

        when(transactionDAO.saveTransaction(
                eq(connection),
                any()))
                .thenReturn(true);

        boolean result =
                withdrawService.withdraw(
                        1,
                        new BigDecimal("1000"),
                        "Test withdrawal");

        assertTrue(result);

        verify(connection)
                .setAutoCommit(false);

        verify(accountDAO)
                .updateBalance(
                        connection,
                        1,
                        new BigDecimal("4000"));

        verify(transactionDAO)
                .saveTransaction(
                        eq(connection),
                        any());

        verify(connection).commit();

        verify(connection)
                .setAutoCommit(true);

        verify(connection).close();
    }

    @Test
    void withdraw_shouldRollback_whenExceptionOccurs()
            throws Exception {

        when(accountDAO.getAccountById(connection, 1))
                .thenThrow(
                        new RuntimeException("Database error"));

        boolean result =
                withdrawService.withdraw(
                        1,
                        new BigDecimal("1000"),
                        "Test withdrawal");

        assertFalse(result);

        verify(connection).rollback();

        verify(connection)
                .setAutoCommit(true);

        verify(connection).close();
    }
}
