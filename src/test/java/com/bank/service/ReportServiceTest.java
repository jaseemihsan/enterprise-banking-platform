package com.bank.service;

import com.bank.dao.AccountDAO;
import com.bank.dao.CustomerDAO;
import com.bank.dao.TransactionDAO;
import com.bank.model.Account;
import com.bank.model.Customer;
import com.bank.model.Transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    private TransactionDAO transactionDAO;
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;

    private ReportService reportService;

    @BeforeEach
    void setUp() {

        transactionDAO = mock(TransactionDAO.class);
        customerDAO = mock(CustomerDAO.class);
        accountDAO = mock(AccountDAO.class);

        reportService =
                new ReportService(
                        transactionDAO,
                        customerDAO,
                        accountDAO);
    }

    @Test
    void getTodayTransactions_shouldReturnTransactions() {

        Transaction transaction =
                new Transaction();

        List<Transaction> transactions =
                Arrays.asList(transaction);

        when(transactionDAO.getTodayTransactions())
                .thenReturn(transactions);

        List<Transaction> result =
                reportService.getTodayTransactions();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(transaction, result.get(0));

        verify(transactionDAO)
                .getTodayTransactions();
    }

    @Test
    void getDepositTransactions_shouldReturnTransactions() {

        Transaction transaction =
                new Transaction();

        when(transactionDAO.getDepositTransactions())
                .thenReturn(Arrays.asList(transaction));

        List<Transaction> result =
                reportService.getDepositTransactions();

        assertEquals(1, result.size());
        assertSame(transaction, result.get(0));

        verify(transactionDAO)
                .getDepositTransactions();
    }

    @Test
    void getWithdrawTransactions_shouldReturnTransactions() {

        Transaction transaction =
                new Transaction();

        when(transactionDAO.getWithdrawTransactions())
                .thenReturn(Arrays.asList(transaction));

        List<Transaction> result =
                reportService.getWithdrawTransactions();

        assertEquals(1, result.size());
        assertSame(transaction, result.get(0));

        verify(transactionDAO)
                .getWithdrawTransactions();
    }

    @Test
    void getTransferTransactions_shouldReturnTransactions() {

        Transaction transaction =
                new Transaction();

        when(transactionDAO.getTransferTransactions())
                .thenReturn(Arrays.asList(transaction));

        List<Transaction> result =
                reportService.getTransferTransactions();

        assertEquals(1, result.size());
        assertSame(transaction, result.get(0));

        verify(transactionDAO)
                .getTransferTransactions();
    }

    @Test
    void getCustomerReport_shouldReturnCustomers() {

        Customer customer =
                new Customer();

        when(customerDAO.getAllCustomers())
                .thenReturn(Arrays.asList(customer));

        List<Customer> result =
                reportService.getCustomerReport();

        assertEquals(1, result.size());
        assertSame(customer, result.get(0));

        verify(customerDAO)
                .getAllCustomers();
    }

    @Test
    void getAccountReport_shouldReturnAccounts() {

        Account account =
                new Account();

        when(accountDAO.getAccountReport())
                .thenReturn(Arrays.asList(account));

        List<Account> result =
                reportService.getAccountReport();

        assertEquals(1, result.size());
        assertSame(account, result.get(0));

        verify(accountDAO)
                .getAccountReport();
    }

    @Test
    void getTodayTransactions_shouldReturnEmptyList() {

        when(transactionDAO.getTodayTransactions())
                .thenReturn(Collections.emptyList());

        List<Transaction> result =
                reportService.getTodayTransactions();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(transactionDAO)
                .getTodayTransactions();
    }

    @Test
    void getCustomerReport_shouldReturnEmptyList() {

        when(customerDAO.getAllCustomers())
                .thenReturn(Collections.emptyList());

        List<Customer> result =
                reportService.getCustomerReport();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(customerDAO)
                .getAllCustomers();
    }
}
