package com.bank.dao;

import com.bank.config.DBConnection;
import com.bank.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    /*
     * Create Account
     */
    public boolean saveAccount(Account account) {

        String sql = """
                INSERT INTO accounts
                (
                    customer_id,
                    account_number,
                    account_type,
                    balance,
                    status
                )
                VALUES (?,?,?,?,?)
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, account.getCustomerId());
            statement.setString(2, account.getAccountNumber());
            statement.setString(3, account.getAccountType());
            statement.setBigDecimal(4, account.getBalance());
            statement.setString(5, account.getStatus());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /*
     * List Accounts
     */
    public List<Account> getAllAccounts() {

        List<Account> accounts = new ArrayList<>();

        String sql = """
                SELECT
                    a.id,
                    a.customer_id,
                    a.account_number,
                    a.account_type,
                    a.balance,
                    a.status,
                    CONCAT(c.first_name,' ',c.last_name) AS customer_name,
                    c.email
                FROM accounts a
                INNER JOIN customers c
                    ON a.customer_id = c.id
                ORDER BY a.id
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()
        ) {

            while (rs.next()) {

                Account account = new Account();

                account.setId(rs.getInt("id"));
                account.setCustomerId(rs.getInt("customer_id"));
                account.setAccountNumber(rs.getString("account_number"));
                account.setAccountType(rs.getString("account_type"));
                account.setBalance(rs.getBigDecimal("balance"));
                account.setStatus(rs.getString("status"));
                account.setCustomerName(rs.getString("customer_name"));
                account.setCustomerEmail(rs.getString("email"));

                accounts.add(account);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return accounts;
    }

    /*
     * Search Accounts
     */
    public List<Account> searchAccounts(String keyword) {

        List<Account> accounts = new ArrayList<>();

        String sql = """
                SELECT
                    a.id,
                    a.customer_id,
                    a.account_number,
                    a.account_type,
                    a.balance,
                    a.status,
                    CONCAT(c.first_name,' ',c.last_name) AS customer_name,
                    c.email
                FROM accounts a
                INNER JOIN customers c
                    ON a.customer_id = c.id
                WHERE
                    a.account_number LIKE ?
                    OR
                    CONCAT(c.first_name,' ',c.last_name) LIKE ?
                ORDER BY a.id
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, "%" + keyword + "%");
            statement.setString(2, "%" + keyword + "%");

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                Account account = new Account();

                account.setId(rs.getInt("id"));
                account.setCustomerId(rs.getInt("customer_id"));
                account.setAccountNumber(rs.getString("account_number"));
                account.setAccountType(rs.getString("account_type"));
                account.setBalance(rs.getBigDecimal("balance"));
                account.setStatus(rs.getString("status"));
                account.setCustomerName(rs.getString("customer_name"));
                account.setCustomerEmail(rs.getString("email"));

                accounts.add(account);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return accounts;
    }

    /*
     * Get Account By ID
     */
     public Account getAccountById(int id) {

        String sql = """
                SELECT
                    a.*,
                    CONCAT(c.first_name,' ',c.last_name) AS customer_name,
                    c.email
                FROM accounts a
                INNER JOIN customers c
                    ON a.customer_id = c.id
                WHERE a.id = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {

                Account account = new Account();

                account.setId(rs.getInt("id"));
                account.setCustomerId(rs.getInt("customer_id"));
                account.setAccountNumber(rs.getString("account_number"));
                account.setAccountType(rs.getString("account_type"));
                account.setBalance(rs.getBigDecimal("balance"));
                account.setStatus(rs.getString("status"));
                account.setCustomerName(rs.getString("customer_name"));
                account.setCustomerEmail(rs.getString("email"));

                return account;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
     * Update Account
     */
    public boolean updateAccount(Account account) {

        String sql = """
                UPDATE accounts
                SET
                    account_type = ?,
                    status = ?
                WHERE id = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, account.getAccountType());
            statement.setString(2, account.getStatus());
            statement.setInt(3, account.getId());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /*
     * Close Account
     */
    public boolean closeAccount(int id) {

        String sql = """
                UPDATE accounts
                SET status='CLOSED'
                WHERE id=?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

     /*
 * Update Account Balance
 */
public boolean updateBalance(Connection connection,
                             int accountId,
                             java.math.BigDecimal balance) {

    String sql = """
            UPDATE accounts
            SET balance = ?
            WHERE id = ?
            """;

    try (

            PreparedStatement statement =
                    connection.prepareStatement(sql)

    ) {

        statement.setBigDecimal(1, balance);
        statement.setInt(2, accountId);

        return statement.executeUpdate() > 0;

    } catch (Exception e) {

        e.printStackTrace();

    }

    return false;

  }

    /*
 * Get Active Accounts
 */
public List<Account> getActiveAccounts() {

    List<Account> accounts = new ArrayList<>();

    String sql = """
            SELECT
                a.id,
                a.account_number,
                a.account_type,
                a.balance,
                a.status,
                CONCAT(c.first_name,' ',c.last_name) customer_name
            FROM accounts a
            JOIN customers c
                ON a.customer_id=c.id
            WHERE a.status='ACTIVE'
            ORDER BY account_number
            """;

    try (

            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet rs =
                    statement.executeQuery();

    ) {

        while (rs.next()) {

            Account account = new Account();

            account.setId(rs.getInt("id"));

            account.setAccountNumber(
                    rs.getString("account_number"));

            account.setAccountType(
                    rs.getString("account_type"));

            account.setBalance(
                    rs.getBigDecimal("balance"));

            account.setStatus(
                    rs.getString("status"));

            account.setCustomerName(
                    rs.getString("customer_name"));

            accounts.add(account);

        }

    } catch (Exception e) {

        e.printStackTrace();

    }

    return accounts;

  }

   /*
 * Get Account By ID using existing connection
 */
public Account getAccountById(Connection connection, int id) {

    String sql = """
            SELECT
                a.id,
                a.customer_id,
                a.account_number,
                a.account_type,
                a.balance,
                a.status,
                CONCAT(c.first_name,' ',c.last_name) customer_name,
                c.email
            FROM accounts a
            JOIN customers c
            ON a.customer_id=c.id
            WHERE a.id=?
            """;

    try (

            PreparedStatement statement =
                    connection.prepareStatement(sql)

    ) {

        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();

        if (rs.next()) {

            Account account = new Account();

            account.setId(rs.getInt("id"));

            account.setCustomerId(
                    rs.getInt("customer_id"));

            account.setAccountNumber(
                    rs.getString("account_number"));

            account.setAccountType(
                    rs.getString("account_type"));

            account.setBalance(
                    rs.getBigDecimal("balance"));

            account.setStatus(
                    rs.getString("status"));

            account.setCustomerName(
                    rs.getString("customer_name"));

            account.setCustomerEmail(
                    rs.getString("email"));

            return account;

        }

    } catch (Exception e) {

        e.printStackTrace();

    }

    return null;

}

}
