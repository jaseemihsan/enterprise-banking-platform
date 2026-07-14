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

public class DepositService {

    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    public boolean deposit(int accountId,
                           BigDecimal amount,
                           String remarks) {

        Connection connection = null;

        try {

            connection = DBConnection.getConnection();

            /*
             * Start Database Transaction
             */
            connection.setAutoCommit(false);

            /*
             * Load Account
             */
            Account account =
        accountDAO.getAccountById(
                connection,
                accountId);

            if (account == null) {

                connection.rollback();

                return false;

            }

            if (!"ACTIVE".equalsIgnoreCase(account.getStatus())) {

                connection.rollback();

                return false;

            }

	    BigDecimal balanceBefore = account.getBalance();

BigDecimal balanceAfter =
        balanceBefore.add(amount);

            /*
             * Update Balance
             */
            boolean updated =
                    accountDAO.updateBalance(
                            connection,
                            accountId,
                            balanceAfter);

            if (!updated) {

                connection.rollback();

                return false;

            }

            /*
             * Create Transaction
             */
            Transaction transaction =
                    new Transaction();

            transaction.setAccountId(accountId);

            transaction.setTransactionType(
                    TransactionType.DEPOSIT);

            transaction.setAmount(amount);

            transaction.setReferenceNo(
                    ReferenceGenerator.generateReference());

	    transaction.setBalanceBefore(balanceBefore);

            transaction.setBalanceAfter(balanceAfter);

            transaction.setRemarks(remarks);

            /*
             * Save Transaction
             */
            boolean inserted =
                    transactionDAO.saveTransaction(
                            connection,
                            transaction);

            if (!inserted) {

                connection.rollback();

                return false;

            }

            /*
             * Commit
             */
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
