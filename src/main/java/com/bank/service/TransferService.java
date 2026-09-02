package com.bank.service;

import com.bank.config.ConnectionProvider;
import com.bank.config.DBConnectionProvider;
import com.bank.dao.AccountDAO;
import com.bank.dao.TransactionDAO;
import com.bank.model.Account;
import com.bank.model.Transaction;

import java.math.BigDecimal;
import java.sql.Connection;

public class TransferService {

    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;
    private final ConnectionProvider connectionProvider;

    /*
     * Default constructor used by the application.
     */
    public TransferService() {

        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        this.connectionProvider = new DBConnectionProvider();
    }

    /*
     * Constructor used by unit tests.
     */
    TransferService(
            AccountDAO accountDAO,
            TransactionDAO transactionDAO,
            ConnectionProvider connectionProvider) {

        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
        this.connectionProvider = connectionProvider;
    }

    public boolean transfer(
            int fromAccountId,
            int toAccountId,
            BigDecimal amount,
            String remarks) {

        Connection connection = null;

        try {

            connection =
                    connectionProvider.getConnection();

            connection.setAutoCommit(false);

            Account fromAccount =
                    accountDAO.getAccountById(
                            connection,
                            fromAccountId);

            Account toAccount =
                    accountDAO.getAccountById(
                            connection,
                            toAccountId);

            /*
             * Account validation
             */
            if (fromAccount == null ||
                    toAccount == null) {

                connection.rollback();

                return false;
            }

            /*
             * Cannot transfer to same account
             */
            if (fromAccountId == toAccountId) {

                connection.rollback();

                return false;
            }

            /*
             * Validate amount
             */
            if (amount == null ||
                    amount.compareTo(BigDecimal.ZERO) <= 0) {

                connection.rollback();

                return false;
            }

            /*
             * Source account must be active
             */
            if (!"ACTIVE".equalsIgnoreCase(
                    fromAccount.getStatus())) {

                connection.rollback();

                return false;
            }

            /*
             * Destination account must be active
             */
            if (!"ACTIVE".equalsIgnoreCase(
                    toAccount.getStatus())) {

                connection.rollback();

                return false;
            }

            /*
             * Check sufficient balance
             */
            if (fromAccount.getBalance()
                    .compareTo(amount) < 0) {

                connection.rollback();

                return false;
            }

            BigDecimal newFromBalance =
                    fromAccount.getBalance()
                            .subtract(amount);

            BigDecimal newToBalance =
                    toAccount.getBalance()
                            .add(amount);

            /*
             * Debit source account
             */
            boolean debitSuccess =
                    accountDAO.updateBalance(
                            connection,
                            fromAccountId,
                            newFromBalance);

            if (!debitSuccess) {

                connection.rollback();

                return false;
            }

            /*
             * Credit destination account
             */
            boolean creditSuccess =
                    accountDAO.updateBalance(
                            connection,
                            toAccountId,
                            newToBalance);

            if (!creditSuccess) {

                connection.rollback();

                return false;
            }

            /*
             * Generate common reference number
             */
            String referenceNo =
                    "TRX" + System.currentTimeMillis();

            /*
             * Debit transaction
             */
            Transaction debitTransaction =
                    new Transaction();

            debitTransaction.setAccountId(
                    fromAccountId);

            debitTransaction.setTransactionType(
                    "TRANSFER_DEBIT");

            debitTransaction.setAmount(amount);

            debitTransaction.setBalanceBefore(
                    fromAccount.getBalance());

            debitTransaction.setBalanceAfter(
                    newFromBalance);

            debitTransaction.setReferenceNo(
                    referenceNo);

            debitTransaction.setRemarks(
                    remarks);

            boolean debitTransactionSaved =
                    transactionDAO.saveTransaction(
                            connection,
                            debitTransaction);

            if (!debitTransactionSaved) {

                connection.rollback();

                return false;
            }

            /*
             * Credit transaction
             */
            Transaction creditTransaction =
                    new Transaction();

            creditTransaction.setAccountId(
                    toAccountId);

            creditTransaction.setTransactionType(
                    "TRANSFER_CREDIT");

            creditTransaction.setAmount(amount);

            creditTransaction.setBalanceBefore(
                    toAccount.getBalance());

            creditTransaction.setBalanceAfter(
                    newToBalance);

            creditTransaction.setReferenceNo(
                    referenceNo);

            creditTransaction.setRemarks(
                    remarks);

            boolean creditTransactionSaved =
                    transactionDAO.saveTransaction(
                            connection,
                            creditTransaction);

            if (!creditTransactionSaved) {

                connection.rollback();

                return false;
            }

            /*
             * Everything succeeded
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
