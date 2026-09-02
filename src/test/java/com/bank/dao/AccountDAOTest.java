package com.bank.dao;

import com.bank.config.DBConnection;
import com.bank.model.Account;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountDAOTest {

    private static AccountDAO accountDAO;

    private static int customerId;

    private static int accountId;

    @BeforeAll
    static void setUpDatabase() throws Exception {

        accountDAO = new AccountDAO();

        /*
         * Create a dedicated test customer.
         */
        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement("""
                         INSERT INTO customers
                         (first_name, last_name, email, phone)
                         VALUES (?, ?, ?, ?)
                         """,
                         PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, "DAO");
            statement.setString(2, "Test");
            statement.setString(3, "dao-test@example.com");
            statement.setString(4, "0501111111");

            statement.executeUpdate();

            try (ResultSet keys =
                         statement.getGeneratedKeys()) {

                assertTrue(keys.next());

                customerId = keys.getInt(1);
            }
        }
    }

    @AfterAll
    static void cleanUpDatabase() throws Exception {

        /*
         * Remove accounts created by this test.
         */
        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                         "DELETE FROM accounts WHERE customer_id = ?")) {

            statement.setInt(1, customerId);
            statement.executeUpdate();
        }

        /*
         * Remove test customer.
         */
        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                         "DELETE FROM customers WHERE id = ?")) {

            statement.setInt(1, customerId);
            statement.executeUpdate();
        }
    }

    @Test
    @Order(1)
    void saveAccount_shouldCreateAccount() {

        Account account = new Account();

        account.setCustomerId(customerId);
        account.setAccountNumber("DAOACC10001");
        account.setAccountType("SAVINGS");
        account.setBalance(new BigDecimal("1000.00"));
        account.setStatus("ACTIVE");

        boolean result =
                accountDAO.saveAccount(account);

        assertTrue(result);
    }

    @Test
    @Order(2)
    void getAccountById_shouldReturnAccount() throws Exception {

        int id = getTestAccountId("DAOACC10001");

        Account account =
                accountDAO.getAccountById(id);

        assertNotNull(account);

        assertEquals(
                "DAOACC10001",
                account.getAccountNumber());

        assertEquals(
                new BigDecimal("1000.00"),
                account.getBalance());

        assertEquals(
                "ACTIVE",
                account.getStatus());

        accountId = id;
    }

    @Test
    @Order(3)
    void getAllAccounts_shouldContainTestAccount() {

        List<Account> accounts =
                accountDAO.getAllAccounts();

        assertFalse(accounts.isEmpty());

        assertTrue(
                accounts.stream()
                        .anyMatch(a ->
                                "DAOACC10001"
                                        .equals(a.getAccountNumber())));
    }

    @Test
    @Order(4)
    void searchAccounts_shouldFindByAccountNumber() {

        List<Account> accounts =
                accountDAO.searchAccounts("DAOACC10001");

        assertFalse(accounts.isEmpty());

        assertEquals(
                "DAOACC10001",
                accounts.get(0).getAccountNumber());
    }

    @Test
    @Order(5)
    void searchAccounts_shouldFindByCustomerName() {

        List<Account> accounts =
                accountDAO.searchAccounts("DAO Test");

        assertFalse(accounts.isEmpty());

        assertEquals(
                "DAOACC10001",
                accounts.get(0).getAccountNumber());
    }

    @Test
    @Order(6)
    void updateAccount_shouldUpdateTypeAndStatus() {

        Account account =
                accountDAO.getAccountById(accountId);

        assertNotNull(account);

        account.setAccountType("CURRENT");
        account.setStatus("BLOCKED");

        boolean result =
                accountDAO.updateAccount(account);

        assertTrue(result);

        Account updated =
                accountDAO.getAccountById(accountId);

        assertNotNull(updated);

        assertEquals(
                "CURRENT",
                updated.getAccountType());

        assertEquals(
                "BLOCKED",
                updated.getStatus());
    }

    @Test
    @Order(7)
    void updateBalance_shouldUpdateBalance() throws Exception {

        try (Connection connection =
                     DBConnection.getConnection()) {

            boolean result =
                    accountDAO.updateBalance(
                            connection,
                            accountId,
                            new BigDecimal("1500.00"));

            assertTrue(result);

        }

        Account account =
                accountDAO.getAccountById(accountId);

        assertNotNull(account);

        assertEquals(
                new BigDecimal("1500.00"),
                account.getBalance());
    }

    @Test
    @Order(8)
    void getActiveAccounts_shouldReturnOnlyActiveAccounts() {

        Account account =
                accountDAO.getAccountById(accountId);

        assertNotNull(account);

        account.setStatus("ACTIVE");

        assertTrue(
                accountDAO.updateAccount(account));

        List<Account> accounts =
                accountDAO.getActiveAccounts();

        assertTrue(
                accounts.stream()
                        .anyMatch(a ->
                                "DAOACC10001"
                                        .equals(a.getAccountNumber())));

        assertTrue(
                accounts.stream()
                        .allMatch(a ->
                                "ACTIVE".equals(a.getStatus())));
    }

    @Test
    @Order(9)
    void closeAccount_shouldSetStatusClosed() {

        boolean result =
                accountDAO.closeAccount(accountId);

        assertTrue(result);

        Account account =
                accountDAO.getAccountById(accountId);

        assertNotNull(account);

        assertEquals(
                "CLOSED",
                account.getStatus());
    }

    @Test
    @Order(10)
    void getAccountReport_shouldContainTestAccount() {

        List<Account> accounts =
                accountDAO.getAccountReport();

        assertTrue(
                accounts.stream()
                        .anyMatch(a ->
                                "DAOACC10001"
                                        .equals(a.getAccountNumber())));
    }

    @Test
    @Order(11)
    void countAccounts_shouldBeGreaterThanZero() {

        int count =
                accountDAO.countAccounts();

        assertTrue(count > 0);
    }

    private int getTestAccountId(
            String accountNumber) throws Exception {

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                         "SELECT id FROM accounts WHERE account_number = ?")) {

            statement.setString(1, accountNumber);

            try (ResultSet rs =
                         statement.executeQuery()) {

                assertTrue(rs.next());

                return rs.getInt("id");
            }
        }
    }
}
