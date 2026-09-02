package com.bank.service;

import com.bank.dao.TransactionDAO;
import com.bank.model.Transaction;

import java.util.List;

public class StatementService {

    private final TransactionDAO transactionDAO;

    /*
     * Default constructor used by the application.
     */
    public StatementService() {
        this.transactionDAO = new TransactionDAO();
    }

    /*
     * Constructor used for unit testing.
     */
    StatementService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    public List<Transaction> getStatement(int accountId) {

        return transactionDAO.getTransactionsByAccount(accountId);

    }
}
