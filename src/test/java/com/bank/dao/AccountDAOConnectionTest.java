package com.bank.dao;

import com.bank.config.DBConnection;
import com.bank.model.Account;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class AccountDAOConnectionTest {

    private AccountDAO accountDAO;

    private int accountId;

    @BeforeEach
    void setUp() throws Exception {

        accountDAO = new AccountDAO();

        /*
         * Create a test account for customer ID 1.
         */
        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                         """
                         INSERT INTO accounts
                         (customer_id, account_number,
                          account_type, balance, status)
                         VALUES (?, ?, ?, ?, ?)
                         """,
                         PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, 1);
            statement.setString(2,
                    "CONNTEST" + System.currentTimeMillis());
            statement.setString(3, "SAVINGS");
            statement.setBigDecimal(4,
                    new BigDecimal("1000.00"));
            statement.setString(5, "ACTIVE");

            statement.executeUpdate();

            try (ResultSet keys =
                         statement.getGeneratedKeys()) {

                assertTrue(keys.next());

                accountId = keys.getInt(1);
            }
        }
    }

    @AfterEach
    void cleanUp() throws Exception {

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                         "DELETE FROM accounts WHERE id = ?")) {

            statement.setInt(1, accountId);

            statement.executeUpdate();
        }
    }

    @Test
    void getAccountById_shouldReturnAccount()
            throws Exception {

        try (Connection connection =
                     DBConnection.getConnection()) {

            Account account =
                    accountDAO.getAccountById(
                            connection,
                            accountId);

            assertNotNull(account);

            assertEquals(
                    accountId,
                    account.getId());

            assertEquals(
                    new BigDecimal("1000.00"),
                    account.getBalance());

            assertEquals(
                    "ACTIVE",
                    account.getStatus());

            assertEquals(
                    1,
                    account.getCustomerId());
        }
    }

    @Test
    void getAccountById_shouldReturnNullForInvalidId()
            throws Exception {

        try (Connection connection =
                     DBConnection.getConnection()) {

            Account account =
                    accountDAO.getAccountById(
                            connection,
                            999999);

            assertNull(account);
        }
    }

    @Test
    void updateBalance_shouldUpdateBalance()
            throws Exception {

        try (Connection connection =
                     DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            boolean result =
                    accountDAO.updateBalance(
                            connection,
                            accountId,
                            new BigDecimal("1500.00"));

            assertTrue(result);

            /*
             * Commit because the DAO only performs
             * the UPDATE; it does not commit.
             */
            connection.commit();
        }

        /*
         * Verify persisted value.
         */
        try (Connection connection =
                     DBConnection.getConnection()) {

            Account account =
                    accountDAO.getAccountById(
                            connection,
                            accountId);

            assertNotNull(account);

            assertEquals(
                    new BigDecimal("1500.00"),
                    account.getBalance());
        }
    }

    @Test
    void updateBalance_shouldReturnFalseForInvalidAccount()
            throws Exception {

        try (Connection connection =
                     DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            boolean result =
                    accountDAO.updateBalance(
                            connection,
                            999999,
                            new BigDecimal("500.00"));

            assertFalse(result);

            connection.rollback();
        }
    }
}
