package com.bank.service;

import com.bank.config.DBConnection;
import com.bank.dao.AccountDAO;
import com.bank.dao.TransactionDAO;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.service.TransferService;

import java.sql.Connection;

public class TransferService {

    private final AccountDAO accountDAO =
            new AccountDAO();

    private final TransactionDAO transactionDAO =
            new TransactionDAO();

    public boolean transfer(
            int fromAccountId,
            int toAccountId,
            java.math.BigDecimal amount,
            String remarks) {

        Connection connection = null;

        try {

            connection = DBConnection.getConnection();

            connection.setAutoCommit(false);

        Account fromAccount =
        accountDAO.getAccountById(connection, fromAccountId);

Account toAccount =
        accountDAO.getAccountById(connection, toAccountId);

       if (fromAccount == null || toAccount == null) {
    return false;
}

if (fromAccountId == toAccountId) {
    return false;
}

if (amount.compareTo(java.math.BigDecimal.ZERO) <= 0) {
    return false;
}

if (!"ACTIVE".equalsIgnoreCase(fromAccount.getStatus())) {
    return false;
}

if (!"ACTIVE".equalsIgnoreCase(toAccount.getStatus())) {
    return false;
}

if (fromAccount.getBalance().compareTo(amount) < 0) {
    return false;
}

java.math.BigDecimal newFromBalance =
        fromAccount.getBalance().subtract(amount);

java.math.BigDecimal newToBalance =
        toAccount.getBalance().add(amount);

	boolean debitSuccess =
        accountDAO.updateBalance(
                connection,
                fromAccountId,
                newFromBalance
        );

if (!debitSuccess) {
    return false;
}

boolean creditSuccess =
        accountDAO.updateBalance(
                connection,
                toAccountId,
                newToBalance
        );

if (!creditSuccess) {
    return false;
}

// Generate Reference Number
String referenceNo =
        "TRX" + System.currentTimeMillis();

	// Create Debit Transaction
Transaction debitTransaction = new Transaction();

debitTransaction.setAccountId(fromAccountId);
debitTransaction.setTransactionType("TRANSFER_DEBIT");
debitTransaction.setAmount(amount);
debitTransaction.setBalanceBefore(fromAccount.getBalance());
debitTransaction.setBalanceAfter(newFromBalance);
debitTransaction.setReferenceNo(referenceNo);
debitTransaction.setRemarks(remarks);

boolean debitTransactionSaved =
        transactionDAO.saveTransaction(
                connection,
                debitTransaction
        );

if (!debitTransactionSaved) {
    return false;
}

Transaction creditTransaction = new Transaction();

creditTransaction.setAccountId(toAccountId);
creditTransaction.setTransactionType("TRANSFER_CREDIT");
creditTransaction.setAmount(amount);
creditTransaction.setBalanceBefore(toAccount.getBalance());
creditTransaction.setBalanceAfter(newToBalance);
creditTransaction.setReferenceNo(referenceNo);
creditTransaction.setRemarks(remarks);

boolean creditTransactionSaved =
        transactionDAO.saveTransaction(
                connection,
                creditTransaction
        );

if (!creditTransactionSaved) {
    return false;
}

            connection.commit();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            try {

                if(connection != null){

                    connection.rollback();

                }

            } catch (Exception ignored){}

        } finally {

            try {

                if(connection != null){

                    connection.setAutoCommit(true);

                    connection.close();

                }

            } catch (Exception ignored){}

        }

        return false;

    }

}
