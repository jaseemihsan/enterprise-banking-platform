package com.bank.dao;

import com.bank.config.DBConnection;
import com.bank.model.User;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDAOTest {

    private static UserDAO userDAO;

    private static int testUserId;

    private static final String TEST_USERNAME =
            "dao_test_user_001";

    /*
     * Change this if your roles table uses
     * a different role ID for the normal user.
     */
    private static final int TEST_ROLE_ID = 2;

    @BeforeAll
    static void setUpDatabase() throws Exception {

        userDAO = new UserDAO();

        /*
         * Remove an old test record if it exists.
         * This makes repeated test runs safer.
         */
        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                         "DELETE FROM users WHERE username = ?")) {

            statement.setString(1, TEST_USERNAME);
            statement.executeUpdate();
        }

        /*
         * Verify the role exists before inserting.
         */
        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                         "SELECT id FROM roles WHERE id = ?")) {

            statement.setInt(1, TEST_ROLE_ID);

            try (ResultSet rs =
                         statement.executeQuery()) {

                assertTrue(
                        rs.next(),
                        "Test role ID does not exist: "
                                + TEST_ROLE_ID);
            }
        }
    }

    @AfterAll
    static void cleanUpDatabase() throws Exception {

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                         "DELETE FROM users WHERE username = ?")) {

            statement.setString(1, TEST_USERNAME);

            statement.executeUpdate();
        }
    }

    @Test
    @Order(1)
    void addUser_shouldCreateUser() {

        User user = new User();

        user.setUsername(TEST_USERNAME);
        user.setPassword("Test@123");
        user.setRoleId(TEST_ROLE_ID);
        user.setStatus("ACTIVE");

        boolean result =
                userDAO.addUser(user);

        assertTrue(result);

        assertTrue(
                userDAO.usernameExists(TEST_USERNAME));
    }

    @Test
    @Order(2)
    void findUserByUsername_shouldReturnUser() {

        User user =
                userDAO.findUserByUsername(
                        TEST_USERNAME);

        assertNotNull(user);

        assertEquals(
                TEST_USERNAME,
                user.getUsername());

        assertEquals(
                "ACTIVE",
                user.getStatus());
    }

    @Test
    @Order(3)
    void findUserByUsername_shouldReturnNullForUnknownUser() {

        User user =
                userDAO.findUserByUsername(
                        "user_that_does_not_exist_999");

        assertNull(user);
    }

    @Test
    @Order(4)
    void getAllUsers_shouldContainTestUser() {

        List<User> users =
                userDAO.getAllUsers();

        assertFalse(users.isEmpty());

        assertTrue(
                users.stream()
                        .anyMatch(u ->
                                TEST_USERNAME.equals(
                                        u.getUsername())));
    }

    @Test
    @Order(5)
    void getUserById_shouldReturnUser() {

        User user =
                userDAO.findUserByUsername(
                        TEST_USERNAME);

        assertNotNull(user);

        testUserId = user.getId();

        User result =
                userDAO.getUserById(testUserId);

        assertNotNull(result);

        assertEquals(
                TEST_USERNAME,
                result.getUsername());

        assertEquals(
                TEST_ROLE_ID,
                result.getRoleId());
    }

    @Test
    @Order(6)
    void getUserById_shouldReturnNullForUnknownId() {

        User result =
                userDAO.getUserById(999999);

        assertNull(result);
    }

    @Test
    @Order(7)
    void usernameExists_shouldReturnTrueForExistingUser() {

        assertTrue(
                userDAO.usernameExists(
                        TEST_USERNAME));
    }

    @Test
    @Order(8)
    void usernameExists_shouldReturnFalseForUnknownUser() {

        assertFalse(
                userDAO.usernameExists(
                        "unknown_user_999999"));
    }

    @Test
    @Order(9)
    void updateUser_shouldUpdateUser() {

        User user =
                userDAO.getUserById(testUserId);

        assertNotNull(user);

        user.setUsername(TEST_USERNAME);
        user.setRoleId(TEST_ROLE_ID);
        user.setStatus("BLOCKED");

        boolean result =
                userDAO.updateUser(user);

        assertTrue(result);

        User updated =
                userDAO.getUserById(testUserId);

        assertNotNull(updated);

        assertEquals(
                "BLOCKED",
                updated.getStatus());
    }

    @Test
    @Order(10)
    void updateStatus_shouldUpdateStatus() {

        boolean result =
                userDAO.updateStatus(
                        testUserId,
                        "ACTIVE");

        assertTrue(result);

        User user =
                userDAO.getUserById(testUserId);

        assertNotNull(user);

        assertEquals(
                "ACTIVE",
                user.getStatus());
    }

    @Test
    @Order(11)
    void updateStatus_shouldReturnFalseForUnknownId() {

        boolean result =
                userDAO.updateStatus(
                        999999,
                        "BLOCKED");

        assertFalse(result);
    }
}
