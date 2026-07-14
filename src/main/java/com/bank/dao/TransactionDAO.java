package com.bank.dao;

import com.bank.model.Transaction;
import com.bank.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    /*
     * Insert Transaction
     */
    public boolean saveTransaction(Connection connection,
                                   Transaction transaction) {

        String sql = """
INSERT INTO transactions
(
    account_id,
    transaction_type,
    amount,
    balance_before,
    balance_after,
    reference_no,
    remarks
)
VALUES (?,?,?,?,?,?,?)
""";

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, transaction.getAccountId());

statement.setString(2, transaction.getTransactionType());

statement.setBigDecimal(3, transaction.getAmount());

statement.setBigDecimal(4, transaction.getBalanceBefore());

statement.setBigDecimal(5, transaction.getBalanceAfter());

statement.setString(6, transaction.getReferenceNo());

statement.setString(7, transaction.getRemarks());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    /*
     * Transaction History
     */
    public List<Transaction> getTransactionsByAccount(int accountId) {

        List<Transaction> transactions = new ArrayList<>();

        String sql = """
                SELECT *
                FROM transactions
                WHERE account_id = ?
                ORDER BY transaction_time DESC
                """;

        try (

                Connection connection =
                        com.bank.config.DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setInt(1, accountId);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                Transaction transaction =
                        new Transaction();

                transaction.setId(
                        rs.getInt("id"));

                transaction.setAccountId(
                        rs.getInt("account_id"));

                transaction.setTransactionType(
                        rs.getString("transaction_type"));

                transaction.setAmount(
                        rs.getBigDecimal("amount"));

                transaction.setReferenceNo(
                        rs.getString("reference_no"));

                transaction.setRemarks(
                        rs.getString("remarks"));

                transaction.setTransactionTime(
                        rs.getTimestamp("transaction_time"));

                transactions.add(transaction);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return transactions;

    }

    /*
     * Search Transactions
     */
    public List<Transaction> searchTransactions(String keyword) {

        List<Transaction> transactions = new ArrayList<>();

        String sql = """
                SELECT *
                FROM transactions
                WHERE
                    reference_no LIKE ?
                    OR
                    transaction_type LIKE ?
                ORDER BY transaction_time DESC
                """;

        try (

                Connection connection =
                        com.bank.config.DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setString(1,
                    "%" + keyword + "%");

            statement.setString(2,
                    "%" + keyword + "%");

            ResultSet rs =
                    statement.executeQuery();

            while (rs.next()) {

                Transaction transaction =
                        new Transaction();

                transaction.setId(
                        rs.getInt("id"));

                transaction.setAccountId(
                        rs.getInt("account_id"));

                transaction.setTransactionType(
                        rs.getString("transaction_type"));

                transaction.setAmount(
                        rs.getBigDecimal("amount"));

                transaction.setReferenceNo(
                        rs.getString("reference_no"));

                transaction.setRemarks(
                        rs.getString("remarks"));

                transaction.setTransactionTime(
                        rs.getTimestamp("transaction_time"));

                transactions.add(transaction);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return transactions;

    }

    /*
 * Get All Transactions
 */
public List<Transaction> getAllTransactions() {

    List<Transaction> transactions = new ArrayList<>();

    String sql = """
        SELECT
            t.*,
            a.account_number
        FROM transactions t
        JOIN accounts a
            ON t.account_id = a.id
        ORDER BY transaction_time DESC
        """;

    try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
    ) {

        while (rs.next()) {

            Transaction transaction = new Transaction();

            transaction.setId(rs.getInt("id"));
            transaction.setAccountId(rs.getInt("account_id"));
            transaction.setAccountNumber(rs.getString("account_number"));
            transaction.setTransactionType(rs.getString("transaction_type"));
            transaction.setAmount(rs.getBigDecimal("amount"));
            transaction.setBalanceBefore(rs.getBigDecimal("balance_before"));
            transaction.setBalanceAfter(rs.getBigDecimal("balance_after"));
            transaction.setReferenceNo(rs.getString("reference_no"));
            transaction.setRemarks(rs.getString("remarks"));
            transaction.setTransactionTime(rs.getTimestamp("transaction_time"));

            transactions.add(transaction);

        }

    } catch (Exception e) {

        e.printStackTrace();

    }

    return transactions;

}

}
