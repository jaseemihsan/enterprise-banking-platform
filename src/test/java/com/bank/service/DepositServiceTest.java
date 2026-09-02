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

class DepositServiceTest {

    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private ConnectionProvider connectionProvider;
    private Connection connection;

    private DepositService depositService;

    @BeforeEach
    void setUp() throws Exception {

        accountDAO = mock(AccountDAO.class);
        transactionDAO = mock(TransactionDAO.class);
        connectionProvider = mock(ConnectionProvider.class);
        connection = mock(Connection.class);

        when(connectionProvider.getConnection())
                .thenReturn(connection);

        depositService =
                new DepositService(
                        accountDAO,
                        transactionDAO,
                        connectionProvider);
    }

    @Test
    void deposit_shouldReturnFalse_whenAccountNotFound()
            throws Exception {

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(null);

        boolean result =
                depositService.deposit(
                        1,
                        new BigDecimal("1000"),
                        "Test deposit");

        assertFalse(result);

        verify(connection).rollback();

        verify(accountDAO)
                .getAccountById(connection, 1);

        verifyNoInteractions(transactionDAO);

        verify(connection).close();
    }

    @Test
    void deposit_shouldReturnFalse_whenAccountInactive()
            throws Exception {

        Account account = new Account();

        account.setStatus("INACTIVE");
        account.setBalance(new BigDecimal("5000"));

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(account);

        boolean result =
                depositService.deposit(
                        1,
                        new BigDecimal("1000"),
                        "Test deposit");

        assertFalse(result);

        verify(connection).rollback();

        verify(accountDAO)
                .getAccountById(connection, 1);

        verifyNoInteractions(transactionDAO);

        verify(connection).close();
    }

    @Test
    void deposit_shouldReturnFalse_whenBalanceUpdateFails()
            throws Exception {

        Account account = new Account();

        account.setStatus("ACTIVE");
        account.setBalance(new BigDecimal("5000"));

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(account);

        when(accountDAO.updateBalance(
                eq(connection),
                eq(1),
                eq(new BigDecimal("6000"))))
                .thenReturn(false);

        boolean result =
                depositService.deposit(
                        1,
                        new BigDecimal("1000"),
                        "Test deposit");

        assertFalse(result);

        verify(connection).rollback();

        verify(accountDAO)
                .updateBalance(
                        connection,
                        1,
                        new BigDecimal("6000"));

        verifyNoInteractions(transactionDAO);

        verify(connection).close();
    }

    @Test
    void deposit_shouldReturnFalse_whenTransactionInsertFails()
            throws Exception {

        Account account = new Account();

        account.setStatus("ACTIVE");
        account.setBalance(new BigDecimal("5000"));

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(account);

        when(accountDAO.updateBalance(
                eq(connection),
                eq(1),
                eq(new BigDecimal("6000"))))
                .thenReturn(true);

        when(transactionDAO.saveTransaction(
                eq(connection),
                any()))
                .thenReturn(false);

        boolean result =
                depositService.deposit(
                        1,
                        new BigDecimal("1000"),
                        "Test deposit");

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
    void deposit_shouldReturnTrue_whenSuccessful()
            throws Exception {

        Account account = new Account();

        account.setStatus("ACTIVE");
        account.setBalance(new BigDecimal("5000"));

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(account);

        when(accountDAO.updateBalance(
                eq(connection),
                eq(1),
                eq(new BigDecimal("6000"))))
                .thenReturn(true);

        when(transactionDAO.saveTransaction(
                eq(connection),
                any()))
                .thenReturn(true);

        boolean result =
                depositService.deposit(
                        1,
                        new BigDecimal("1000"),
                        "Test deposit");

        assertTrue(result);

        verify(connection).setAutoCommit(false);

        verify(accountDAO)
                .updateBalance(
                        connection,
                        1,
                        new BigDecimal("6000"));

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
    void deposit_shouldRollback_whenExceptionOccurs()
            throws Exception {

        when(connectionProvider.getConnection())
                .thenReturn(connection);

        when(accountDAO.getAccountById(connection, 1))
                .thenThrow(new RuntimeException("Database error"));

        boolean result =
                depositService.deposit(
                        1,
                        new BigDecimal("1000"),
                        "Test deposit");

        assertFalse(result);

        verify(connection).rollback();

        verify(connection).setAutoCommit(true);

        verify(connection).close();
    }
}
