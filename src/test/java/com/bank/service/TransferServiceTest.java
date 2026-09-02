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

class TransferServiceTest {

    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private ConnectionProvider connectionProvider;
    private Connection connection;

    private TransferService transferService;

    @BeforeEach
    void setUp() throws Exception {

        accountDAO = mock(AccountDAO.class);
        transactionDAO = mock(TransactionDAO.class);
        connectionProvider = mock(ConnectionProvider.class);
        connection = mock(Connection.class);

        when(connectionProvider.getConnection())
                .thenReturn(connection);

        transferService =
                new TransferService(
                        accountDAO,
                        transactionDAO,
                        connectionProvider);
    }

    private Account account(
            String status,
            String balance) {

        Account account = new Account();

        account.setStatus(status);
        account.setBalance(
                new BigDecimal(balance));

        return account;
    }

    @Test
    void transfer_shouldRollback_whenSourceAccountMissing()
            throws Exception {

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(null);

        boolean result =
                transferService.transfer(
                        1,
                        2,
                        new BigDecimal("1000"),
                        "Test transfer");

        assertFalse(result);

        verify(connection).rollback();

        verify(connection).close();

        verifyNoInteractions(transactionDAO);
    }

    @Test
    void transfer_shouldRollback_whenDestinationAccountMissing()
            throws Exception {

        Account source =
                account("ACTIVE", "5000");

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(source);

        when(accountDAO.getAccountById(connection, 2))
                .thenReturn(null);

        boolean result =
                transferService.transfer(
                        1,
                        2,
                        new BigDecimal("1000"),
                        "Test transfer");

        assertFalse(result);

        verify(connection).rollback();

        verify(connection).close();

        verifyNoInteractions(transactionDAO);
    }

    @Test
    void transfer_shouldRollback_whenSameAccount()
            throws Exception {

        Account source =
                account("ACTIVE", "5000");

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(source);

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(source);

        boolean result =
                transferService.transfer(
                        1,
                        1,
                        new BigDecimal("1000"),
                        "Test transfer");

        assertFalse(result);

        verify(connection).rollback();

        verify(connection).close();

        verifyNoInteractions(transactionDAO);
    }

    @Test
    void transfer_shouldRollback_whenAmountInvalid()
            throws Exception {

        Account source =
                account("ACTIVE", "5000");

        Account destination =
                account("ACTIVE", "2000");

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(source);

        when(accountDAO.getAccountById(connection, 2))
                .thenReturn(destination);

        boolean result =
                transferService.transfer(
                        1,
                        2,
                        BigDecimal.ZERO,
                        "Test transfer");

        assertFalse(result);

        verify(connection).rollback();

        verifyNoInteractions(transactionDAO);

        verify(connection).close();
    }

    @Test
    void transfer_shouldRollback_whenSourceInactive()
            throws Exception {

        Account source =
                account("INACTIVE", "5000");

        Account destination =
                account("ACTIVE", "2000");

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(source);

        when(accountDAO.getAccountById(connection, 2))
                .thenReturn(destination);

        boolean result =
                transferService.transfer(
                        1,
                        2,
                        new BigDecimal("1000"),
                        "Test transfer");

        assertFalse(result);

        verify(connection).rollback();

        verifyNoInteractions(transactionDAO);

        verify(connection).close();
    }

    @Test
    void transfer_shouldRollback_whenDestinationInactive()
            throws Exception {

        Account source =
                account("ACTIVE", "5000");

        Account destination =
                account("INACTIVE", "2000");

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(source);

        when(accountDAO.getAccountById(connection, 2))
                .thenReturn(destination);

        boolean result =
                transferService.transfer(
                        1,
                        2,
                        new BigDecimal("1000"),
                        "Test transfer");

        assertFalse(result);

        verify(connection).rollback();

        verifyNoInteractions(transactionDAO);

        verify(connection).close();
    }

    @Test
    void transfer_shouldRollback_whenInsufficientBalance()
            throws Exception {

        Account source =
                account("ACTIVE", "500");

        Account destination =
                account("ACTIVE", "2000");

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(source);

        when(accountDAO.getAccountById(connection, 2))
                .thenReturn(destination);

        boolean result =
                transferService.transfer(
                        1,
                        2,
                        new BigDecimal("1000"),
                        "Test transfer");

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
    void transfer_shouldRollback_whenDebitFails()
            throws Exception {

        Account source =
                account("ACTIVE", "5000");

        Account destination =
                account("ACTIVE", "2000");

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(source);

        when(accountDAO.getAccountById(connection, 2))
                .thenReturn(destination);

        when(accountDAO.updateBalance(
                connection,
                1,
                new BigDecimal("4000")))
                .thenReturn(false);

        boolean result =
                transferService.transfer(
                        1,
                        2,
                        new BigDecimal("1000"),
                        "Test transfer");

        assertFalse(result);

        verify(connection).rollback();

        verify(accountDAO, never())
                .updateBalance(
                        connection,
                        2,
                        new BigDecimal("3000"));

        verifyNoInteractions(transactionDAO);

        verify(connection).close();
    }

    @Test
    void transfer_shouldRollback_whenCreditFails()
            throws Exception {

        Account source =
                account("ACTIVE", "5000");

        Account destination =
                account("ACTIVE", "2000");

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(source);

        when(accountDAO.getAccountById(connection, 2))
                .thenReturn(destination);

        when(accountDAO.updateBalance(
                connection,
                1,
                new BigDecimal("4000")))
                .thenReturn(true);

        when(accountDAO.updateBalance(
                connection,
                2,
                new BigDecimal("3000")))
                .thenReturn(false);

        boolean result =
                transferService.transfer(
                        1,
                        2,
                        new BigDecimal("1000"),
                        "Test transfer");

        assertFalse(result);

        verify(connection).rollback();

        verifyNoInteractions(transactionDAO);

        verify(connection).close();
    }

    @Test
    void transfer_shouldRollback_whenDebitTransactionFails()
            throws Exception {

        Account source =
                account("ACTIVE", "5000");

        Account destination =
                account("ACTIVE", "2000");

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(source);

        when(accountDAO.getAccountById(connection, 2))
                .thenReturn(destination);

        when(accountDAO.updateBalance(
                connection,
                1,
                new BigDecimal("4000")))
                .thenReturn(true);

        when(accountDAO.updateBalance(
                connection,
                2,
                new BigDecimal("3000")))
                .thenReturn(true);

        when(transactionDAO.saveTransaction(
                eq(connection),
                any()))
                .thenReturn(false);

        boolean result =
                transferService.transfer(
                        1,
                        2,
                        new BigDecimal("1000"),
                        "Test transfer");

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
    void transfer_shouldRollback_whenCreditTransactionFails()
            throws Exception {

        Account source =
                account("ACTIVE", "5000");

        Account destination =
                account("ACTIVE", "2000");

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(source);

        when(accountDAO.getAccountById(connection, 2))
                .thenReturn(destination);

        when(accountDAO.updateBalance(
                connection,
                1,
                new BigDecimal("4000")))
                .thenReturn(true);

        when(accountDAO.updateBalance(
                connection,
                2,
                new BigDecimal("3000")))
                .thenReturn(true);

        when(transactionDAO.saveTransaction(
                eq(connection),
                any()))
                .thenReturn(true)
                .thenReturn(false);

        boolean result =
                transferService.transfer(
                        1,
                        2,
                        new BigDecimal("1000"),
                        "Test transfer");

        assertFalse(result);

        verify(transactionDAO, times(2))
                .saveTransaction(
                        eq(connection),
                        any());

        verify(connection).rollback();

        verify(connection, never()).commit();

        verify(connection).close();
    }

    @Test
    void transfer_shouldReturnTrue_whenSuccessful()
            throws Exception {

        Account source =
                account("ACTIVE", "5000");

        Account destination =
                account("ACTIVE", "2000");

        when(accountDAO.getAccountById(connection, 1))
                .thenReturn(source);

        when(accountDAO.getAccountById(connection, 2))
                .thenReturn(destination);

        when(accountDAO.updateBalance(
                connection,
                1,
                new BigDecimal("4000")))
                .thenReturn(true);

        when(accountDAO.updateBalance(
                connection,
                2,
                new BigDecimal("3000")))
                .thenReturn(true);

        when(transactionDAO.saveTransaction(
                eq(connection),
                any()))
                .thenReturn(true);

        boolean result =
                transferService.transfer(
                        1,
                        2,
                        new BigDecimal("1000"),
                        "Test transfer");

        assertTrue(result);

        verify(accountDAO)
                .updateBalance(
                        connection,
                        1,
                        new BigDecimal("4000"));

        verify(accountDAO)
                .updateBalance(
                        connection,
                        2,
                        new BigDecimal("3000"));

        verify(transactionDAO, times(2))
                .saveTransaction(
                        eq(connection),
                        any());

        verify(connection).commit();

        verify(connection)
                .setAutoCommit(true);

        verify(connection).close();
    }

    @Test
    void transfer_shouldRollback_whenExceptionOccurs()
            throws Exception {

        when(accountDAO.getAccountById(connection, 1))
                .thenThrow(
                        new RuntimeException("Database error"));

        boolean result =
                transferService.transfer(
                        1,
                        2,
                        new BigDecimal("1000"),
                        "Test transfer");

        assertFalse(result);

        verify(connection).rollback();

        verify(connection)
                .setAutoCommit(true);

        verify(connection).close();
    }
}
