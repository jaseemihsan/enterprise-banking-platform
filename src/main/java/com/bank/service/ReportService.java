package com.bank.service;

import com.bank.dao.CustomerDAO;
import com.bank.dao.TransactionDAO;
import com.bank.dao.AccountDAO;

import com.bank.model.Customer;
import com.bank.model.Transaction;
import com.bank.model.Account;

import java.util.List;

public class ReportService {

    private final TransactionDAO transactionDAO;
    private final CustomerDAO customerDAO;
    private final AccountDAO accountDAO;

    /*
     * Default constructor used by the application.
     */
    public ReportService() {
        this.transactionDAO = new TransactionDAO();
        this.customerDAO = new CustomerDAO();
        this.accountDAO = new AccountDAO();
    }

    /*
     * Constructor used for unit testing.
     */
    ReportService(
            TransactionDAO transactionDAO,
            CustomerDAO customerDAO,
            AccountDAO accountDAO) {

        this.transactionDAO = transactionDAO;
        this.customerDAO = customerDAO;
        this.accountDAO = accountDAO;
    }

    public List<Transaction> getTodayTransactions() {
        return transactionDAO.getTodayTransactions();
    }

    public List<Transaction> getDepositTransactions() {
        return transactionDAO.getDepositTransactions();
    }

    public List<Transaction> getWithdrawTransactions() {
        return transactionDAO.getWithdrawTransactions();
    }

    public List<Transaction> getTransferTransactions() {
        return transactionDAO.getTransferTransactions();
    }

    public List<Customer> getCustomerReport() {
        return customerDAO.getAllCustomers();
    }

    public List<Account> getAccountReport() {
        return accountDAO.getAccountReport();
    }
}
