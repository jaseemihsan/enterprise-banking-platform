package com.bank.service;

import com.bank.dao.TransactionDAO;
import com.bank.model.Transaction;

import java.util.List;

public class TransactionService {

    private final TransactionDAO transactionDAO;

    /*
     * Default constructor used by the application.
     */
    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
    }

    /*
     * Constructor used for unit testing.
     */
    TransactionService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    public List<Transaction> getAllTransactions() {

        return transactionDAO.getAllTransactions();

    }
}
