package com.bank.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bank.dto.TransactionTrend;
import com.bank.config.DBConnection;
import com.bank.model.Dashboard;
import com.bank.model.Customer;
import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.List;
import java.util.ArrayList;

public class DashboardDAO {


        private static final Logger logger =
        LoggerFactory.getLogger(DashboardDAO.class);

public Dashboard getDashboardStats() {

    Dashboard dashboard = new Dashboard();

    String customerSql =
            "SELECT COUNT(*) FROM customers";

    String accountSql =
            "SELECT COUNT(*) FROM accounts";

    String transactionSql =
            "SELECT COUNT(*) FROM transactions";

    String balanceSql =
            "SELECT COALESCE(SUM(balance),0) FROM accounts";

    try (Connection connection = DBConnection.getConnection();

         PreparedStatement ps1 =
                 connection.prepareStatement(customerSql);
         ResultSet rs1 = ps1.executeQuery()) {

        if (rs1.next()) {
            dashboard.setTotalCustomers(rs1.getInt(1));
        }

    } catch (Exception e) {
        logger.error("Error loading customer count", e);
    }

    try (Connection connection = DBConnection.getConnection();

         PreparedStatement ps2 =
                 connection.prepareStatement(accountSql);
         ResultSet rs2 = ps2.executeQuery()) {

        if (rs2.next()) {
            dashboard.setTotalAccounts(rs2.getInt(1));
        }

    } catch (Exception e) {
        logger.error("Error loading account count", e);
    }

    try (Connection connection = DBConnection.getConnection();

         PreparedStatement ps3 =
                 connection.prepareStatement(transactionSql);
         ResultSet rs3 = ps3.executeQuery()) {

        if (rs3.next()) {
            dashboard.setTotalTransactions(rs3.getInt(1));
        }

    } catch (Exception e) {
        logger.error("Error loading transaction count", e);
    }

    try (Connection connection = DBConnection.getConnection();

         PreparedStatement ps4 =
                 connection.prepareStatement(balanceSql);
         ResultSet rs4 = ps4.executeQuery()) {

        if (rs4.next()) {
            dashboard.setTotalBalance(rs4.getBigDecimal(1));
        }

    } catch (Exception e) {
        logger.error("Error loading total balance", e);
    }

    return dashboard;
}

     public List<Customer> getRecentCustomers() {

    List<Customer> customers = new ArrayList<>();

    String sql = """
        SELECT *
        FROM customers
        ORDER BY id DESC
        LIMIT 5
        """;

    try (
        Connection connection = DBConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()
    ) {

        while(rs.next()){

            Customer c = new Customer();

            c.setId(rs.getInt("id"));
            c.setFirstName(rs.getString("first_name"));
            c.setLastName(rs.getString("last_name"));
            c.setEmail(rs.getString("email"));
            c.setPhone(rs.getString("phone"));

            customers.add(c);

        }

    } catch(Exception e){

        e.printStackTrace();

    }

    return customers;
  }

    public int getCustomerCount() {

    String sql = "SELECT COUNT(*) FROM customers";

    try (
        Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery()
    ) {

        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}

public int getAccountCount() {

    String sql = "SELECT COUNT(*) FROM accounts";

    try (
        Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery()
    ) {

        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}

public int getTransactionCount() {

    String sql = "SELECT COUNT(*) FROM transactions";

    try (
        Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery()
    ) {

        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}

public BigDecimal getTotalDeposits() {

    String sql = """
        SELECT COALESCE(SUM(amount),0)
        FROM transactions
        WHERE transaction_type='DEPOSIT'
        """;

    try (
        Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery()
    ) {

        if (rs.next()) {
            return rs.getBigDecimal(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return BigDecimal.ZERO;
}

public BigDecimal getTotalWithdrawals() {

    String sql = """
        SELECT COALESCE(SUM(amount),0)
        FROM transactions
        WHERE transaction_type='WITHDRAW'
        """;

    try (
        Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery()
    ) {

        if (rs.next()) {
            return rs.getBigDecimal(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return BigDecimal.ZERO;
}

public BigDecimal getTotalTransfers() {

    String sql = """
        SELECT COALESCE(SUM(amount),0)
        FROM transactions
        WHERE transaction_type='TRANSFER_DEBIT'
        """;

    try (
        Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery()
    ) {

        if (rs.next()) {
            return rs.getBigDecimal(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return BigDecimal.ZERO;
}

public List<TransactionTrend> getTransactionTrend() {

    List<TransactionTrend> trends =
            new ArrayList<>();

    String sql = """
    SELECT
        DATE(transaction_time) AS txn_date,
        COUNT(*) AS total
    FROM transactions
    GROUP BY DATE(transaction_time)
    ORDER BY DATE(transaction_time)
    """;

    try (
        Connection connection = DBConnection.getConnection();
        PreparedStatement statement =
                connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery()
    ) {

        while (rs.next()) {

            TransactionTrend trend =
                    new TransactionTrend();

            trend.setDate(
                    rs.getString("txn_date"));

            trend.setCount(
                    rs.getInt("total"));

            trends.add(trend);
        }

    } catch (Exception e) {
       logger.error("Error loading transaction trend", e);

    }

    return trends;
}

}
