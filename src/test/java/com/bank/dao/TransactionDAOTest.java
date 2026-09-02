package com.bank.dao;

import com.bank.config.DBConnection;
import com.bank.model.Transaction;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransactionDAOTest {

    private static TransactionDAO transactionDAO;
    private static int customerId;
    private static int accountId;
    private static int transactionId;

    @BeforeAll
    static void setUp() throws Exception {

        transactionDAO = new TransactionDAO();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     """
                     INSERT INTO customers
                     (first_name,last_name,email,phone)
                     VALUES (?,?,?,?)
                     """,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, "Transaction");
            ps.setString(2, "DAOTest");
            ps.setString(3, "transaction-dao@test.com");
            ps.setString(4, "0502222222");
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                assertTrue(keys.next());
                customerId = keys.getInt(1);
            }
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     """
                     INSERT INTO accounts
                     (customer_id,account_number,account_type,balance,status)
                     VALUES (?,?,?,?,?)
                     """,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, customerId);
            ps.setString(2, "TXNDAO10001");
            ps.setString(3, "SAVINGS");
            ps.setBigDecimal(4, new BigDecimal("5000.00"));
            ps.setString(5, "ACTIVE");
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                assertTrue(keys.next());
                accountId = keys.getInt(1);
            }
        }
    }

    @AfterAll
    static void cleanup() throws Exception {

        try (Connection connection = DBConnection.getConnection()) {

            try (PreparedStatement ps =
                         connection.prepareStatement(
                                 "DELETE FROM transactions WHERE account_id=?")) {
                ps.setInt(1, accountId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps =
                         connection.prepareStatement(
                                 "DELETE FROM accounts WHERE id=?")) {
                ps.setInt(1, accountId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps =
                         connection.prepareStatement(
                                 "DELETE FROM customers WHERE id=?")) {
                ps.setInt(1, customerId);
                ps.executeUpdate();
            }
        }
    }

    private Transaction createTransaction(
            String type,
            String reference) {

        Transaction t = new Transaction();

        t.setAccountId(accountId);
        t.setTransactionType(type);
        t.setAmount(new BigDecimal("100.00"));
        t.setBalanceBefore(new BigDecimal("5000.00"));
        t.setBalanceAfter(new BigDecimal("5100.00"));
        t.setReferenceNo(reference);
        t.setRemarks("DAO integration test");

        return t;
    }

    @Test
    @Order(1)
    void saveTransaction_shouldInsertTransaction() throws Exception {

        Transaction t =
                createTransaction("DEPOSIT", "TXNDAO-DEP-001");

        try (Connection connection = DBConnection.getConnection()) {

            boolean result =
                    transactionDAO.saveTransaction(connection, t);

            assertTrue(result);
        }
    }

    @Test
    @Order(2)
    void getTransactionsByAccount_shouldReturnTransactions() {

        List<Transaction> list =
                transactionDAO.getTransactionsByAccount(accountId);

        assertFalse(list.isEmpty());

        assertTrue(
                list.stream().anyMatch(
                        t -> "TXNDAO-DEP-001"
                                .equals(t.getReferenceNo())));

        transactionId = list.get(0).getId();
    }

    @Test
    @Order(3)
    void searchTransactions_shouldFindByReference() {

        List<Transaction> list =
                transactionDAO.searchTransactions("TXNDAO-DEP-001");

        assertFalse(list.isEmpty());

        assertEquals(
                "TXNDAO-DEP-001",
                list.get(0).getReferenceNo());
    }

    @Test
    @Order(4)
    void searchTransactions_shouldFindByType() {

        List<Transaction> list =
                transactionDAO.searchTransactions("DEPOSIT");

        assertFalse(list.isEmpty());

        assertTrue(
                list.stream().anyMatch(
                        t -> "DEPOSIT"
                                .equals(t.getTransactionType())));
    }

    @Test
    @Order(5)
    void getAllTransactions_shouldReturnTransactions() {

        List<Transaction> list =
                transactionDAO.getAllTransactions();

        assertFalse(list.isEmpty());

        assertTrue(
                list.stream().anyMatch(
                        t -> accountId == t.getAccountId()));
    }

    @Test
    @Order(6)
    void getTodayTransactions_shouldReturnTodayTransactions() {

        List<Transaction> list =
                transactionDAO.getTodayTransactions();

        assertFalse(list.isEmpty());
    }

    @Test
    @Order(7)
    void getDepositTransactions_shouldReturnDeposits() {

        List<Transaction> list =
                transactionDAO.getDepositTransactions();

        assertFalse(list.isEmpty());

        assertTrue(
                list.stream().allMatch(
                        t -> "DEPOSIT"
                                .equals(t.getTransactionType())));
    }

    @Test
    @Order(8)
    void getWithdrawTransactions_shouldReturnWithdrawals() throws Exception {

        Transaction t =
                createTransaction("WITHDRAW", "TXNDAO-WD-001");

        try (Connection connection = DBConnection.getConnection()) {
            assertTrue(
                    transactionDAO.saveTransaction(connection, t));
        }

        List<Transaction> list =
                transactionDAO.getWithdrawTransactions();

        assertFalse(list.isEmpty());

        assertTrue(
                list.stream().allMatch(
                        x -> "WITHDRAW"
                                .equals(x.getTransactionType())));
    }

    @Test
    @Order(9)
    void getTransferTransactions_shouldReturnTransfers() throws Exception {

        Transaction t =
                createTransaction(
                        "TRANSFER_DEBIT",
                        "TXNDAO-TR-001");

        try (Connection connection = DBConnection.getConnection()) {
            assertTrue(
                    transactionDAO.saveTransaction(connection, t));
        }

        List<Transaction> list =
                transactionDAO.getTransferTransactions();

        assertFalse(list.isEmpty());

        assertTrue(
                list.stream().anyMatch(
                        x -> "TRANSFER_DEBIT"
                                .equals(x.getTransactionType())));
    }
}
