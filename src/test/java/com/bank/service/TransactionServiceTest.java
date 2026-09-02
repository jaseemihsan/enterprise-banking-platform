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

class TransactionServiceTest {

    private TransactionDAO transactionDAO;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {

        transactionDAO = mock(TransactionDAO.class);

        transactionService =
                new TransactionService(transactionDAO);
    }

    @Test
    void getAllTransactions_shouldReturnTransactions() {

        Transaction transaction =
                new Transaction();

        List<Transaction> transactions =
                Arrays.asList(transaction);

        when(transactionDAO.getAllTransactions())
                .thenReturn(transactions);

        List<Transaction> result =
                transactionService.getAllTransactions();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(transaction, result.get(0));

        verify(transactionDAO)
                .getAllTransactions();
    }

    @Test
    void getAllTransactions_shouldReturnEmptyList() {

        when(transactionDAO.getAllTransactions())
                .thenReturn(Collections.emptyList());

        List<Transaction> result =
                transactionService.getAllTransactions();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(transactionDAO)
                .getAllTransactions();
    }

    @Test
    void getAllTransactions_shouldReturnNull_whenDaoReturnsNull() {

        when(transactionDAO.getAllTransactions())
                .thenReturn(null);

        List<Transaction> result =
                transactionService.getAllTransactions();

        assertNull(result);

        verify(transactionDAO)
                .getAllTransactions();
    }
}
