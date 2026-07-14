package com.bank.service;

import com.bank.config.DBConnection;
import com.bank.dao.AccountDAO;
import com.bank.dao.TransactionDAO;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.util.ReferenceGenerator;
import com.bank.util.TransactionType;

import java.math.BigDecimal;
import java.sql.Connection;

public class WithdrawService {

    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    public boolean withdraw(int accountId,
                            BigDecimal amount,
                            String remarks) {

        Connection connection = null;

        try {

            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            Account account =
                    accountDAO.getAccountById(connection, accountId);

            if (account == null) {
                connection.rollback();
                return false;
            }

            if (!"ACTIVE".equalsIgnoreCase(account.getStatus())) {
                connection.rollback();
                return false;
            }

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                connection.rollback();
                return false;
            }

            BigDecimal balanceBefore = account.getBalance();

            /*
             * Check Balance
             */
            if (balanceBefore.compareTo(amount) < 0) {

                connection.rollback();

                return false;

            }

            BigDecimal balanceAfter =
                    balanceBefore.subtract(amount);

            boolean updated =
                    accountDAO.updateBalance(
                            connection,
                            accountId,
                            balanceAfter);

            if (!updated) {

                connection.rollback();

                return false;

            }

            Transaction transaction = new Transaction();

            transaction.setAccountId(accountId);

            transaction.setTransactionType(
                    TransactionType.WITHDRAW);

            transaction.setAmount(amount);

            transaction.setBalanceBefore(balanceBefore);

            transaction.setBalanceAfter(balanceAfter);

            transaction.setReferenceNo(
                    ReferenceGenerator.generateReference());

            transaction.setRemarks(remarks);

            boolean inserted =
                    transactionDAO.saveTransaction(
                            connection,
                            transaction);

            if (!inserted) {

                connection.rollback();

                return false;

            }

            connection.commit();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            try {

                if (connection != null) {

                    connection.rollback();

                }

            } catch (Exception ignored) {
            }

        } finally {

            try {

                if (connection != null) {

                    connection.setAutoCommit(true);

                    connection.close();

                }

            } catch (Exception ignored) {
            }

        }

        return false;

    }

}
