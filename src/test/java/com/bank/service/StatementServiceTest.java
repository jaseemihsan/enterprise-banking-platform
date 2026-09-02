package com.bank.service;

import com.bank.dao.TransactionDAO;
import com.bank.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatementServiceTest {

    private TransactionDAO transactionDAO;
    private StatementService statementService;

    @BeforeEach
    void setUp() {

        transactionDAO = mock(TransactionDAO.class);

        statementService =
                new StatementService(transactionDAO);
    }

    @Test
    void getStatement_shouldReturnTransactions() {

        Transaction transaction =
                new Transaction();

        List<Transaction> transactions =
                Arrays.asList(transaction);

        when(transactionDAO.getTransactionsByAccount(1))
                .thenReturn(transactions);

        List<Transaction> result =
                statementService.getStatement(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(transaction, result.get(0));

        verify(transactionDAO)
                .getTransactionsByAccount(1);
    }

    @Test
    void getStatement_shouldReturnEmptyList_whenNoTransactionsExist() {

        when(transactionDAO.getTransactionsByAccount(1))
                .thenReturn(Collections.emptyList());

        List<Transaction> result =
                statementService.getStatement(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(transactionDAO)
                .getTransactionsByAccount(1);
    }

    @Test
    void getStatement_shouldReturnNull_whenDaoReturnsNull() {

        when(transactionDAO.getTransactionsByAccount(1))
                .thenReturn(null);

        List<Transaction> result =
                statementService.getStatement(1);

        assertNull(result);

        verify(transactionDAO)
                .getTransactionsByAccount(1);
    }
}
