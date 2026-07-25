package com.bank.service;

import com.bank.dao.TransactionDAO;
import com.bank.model.Transaction;

import java.util.List;

public class StatementService {

    private final TransactionDAO transactionDAO =
            new TransactionDAO();

    public List<Transaction> getStatement(int accountId) {

        return transactionDAO.getTransactionsByAccount(accountId);

    }
}
