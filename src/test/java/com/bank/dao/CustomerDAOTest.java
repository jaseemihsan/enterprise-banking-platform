package com.bank.dao;

import com.bank.config.DBConnection;
import com.bank.model.Customer;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerDAOTest {

    private static CustomerDAO customerDAO;

    private static int customerId;

    @BeforeAll
    static void setUpDatabase() throws Exception {

        customerDAO = new CustomerDAO();

        Customer customer = new Customer();

        customer.setFirstName("DAO");
        customer.setLastName("Customer");
        customer.setEmail("customer-dao-test@example.com");
        customer.setPhone("0502222222");

        /*
         * Create test customer.
         */
        assertTrue(
                customerDAO.saveCustomer(customer));

        /*
         * Get the generated ID.
         */
        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                         """
                         SELECT id
                         FROM customers
                         WHERE email = ?
                         """)) {

            statement.setString(
                    1,
                    "customer-dao-test@example.com");

            var rs = statement.executeQuery();

            assertTrue(rs.next());

            customerId = rs.getInt("id");
        }
    }

    @AfterAll
    static void cleanUpDatabase() throws Exception {

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
    void getAllCustomers_shouldContainTestCustomer() {

        List<Customer> customers =
                customerDAO.getAllCustomers();

        assertFalse(customers.isEmpty());

        assertTrue(
                customers.stream()
                        .anyMatch(customer ->
                                customerId ==
                                customer.getId()));
    }

    @Test
    @Order(2)
    void searchCustomers_shouldFindByFirstName() {

        List<Customer> customers =
                customerDAO.searchCustomers("DAO");

        assertFalse(customers.isEmpty());

        assertTrue(
                customers.stream()
                        .anyMatch(customer ->
                                customerId ==
                                customer.getId()));
    }

    @Test
    @Order(3)
    void searchCustomers_shouldFindByEmail() {

        List<Customer> customers =
                customerDAO.searchCustomers(
                        "customer-dao-test@example.com");

        assertFalse(customers.isEmpty());

        assertEquals(
                customerId,
                customers.get(0).getId());
    }

    @Test
    @Order(4)
    void getCustomerById_shouldReturnCustomer() {

        Customer customer =
                customerDAO.getCustomerById(
                        customerId);

        assertNotNull(customer);

        assertEquals(
                customerId,
                customer.getId());

        assertEquals(
                "DAO",
                customer.getFirstName());

        assertEquals(
                "Customer",
                customer.getLastName());

        assertEquals(
                "customer-dao-test@example.com",
                customer.getEmail());

        assertEquals(
                "0502222222",
                customer.getPhone());
    }

    @Test
    @Order(5)
    void getCustomerById_shouldReturnNullForInvalidId() {

        Customer customer =
                customerDAO.getCustomerById(999999);

        assertNull(customer);
    }

    @Test
    @Order(6)
    void updateCustomer_shouldUpdateCustomer() {

        Customer customer =
                customerDAO.getCustomerById(
                        customerId);

        assertNotNull(customer);

        customer.setFirstName("Updated");
        customer.setLastName("DAO");
        customer.setEmail(
                "updated-dao-test@example.com");
        customer.setPhone("0503333333");

        boolean result =
                customerDAO.updateCustomer(customer);

        assertTrue(result);

        Customer updated =
                customerDAO.getCustomerById(
                        customerId);

        assertNotNull(updated);

        assertEquals(
                "Updated",
                updated.getFirstName());

        assertEquals(
                "DAO",
                updated.getLastName());

        assertEquals(
                "updated-dao-test@example.com",
                updated.getEmail());

        assertEquals(
                "0503333333",
                updated.getPhone());
    }

    @Test
    @Order(7)
    void updateCustomer_shouldReturnFalseForInvalidId() {

        Customer customer = new Customer();

        customer.setId(999999);
        customer.setFirstName("Nobody");
        customer.setLastName("Test");
        customer.setEmail("nobody@example.com");
        customer.setPhone("0000000000");

        boolean result =
                customerDAO.updateCustomer(customer);

        assertFalse(result);
    }

    @Test
    @Order(8)
    void countCustomers_shouldBeGreaterThanZero() {

        int count =
                customerDAO.countCustomers();

        assertTrue(count > 0);
    }

    @Test
    @Order(9)
    void deleteCustomer_shouldDeleteTestCustomer()
            throws Exception {

        /*
         * Delete using DAO.
         */
        boolean result =
                customerDAO.deleteCustomer(
                        customerId);

        assertTrue(result);

        /*
         * Verify deletion.
         */
        Customer deleted =
                customerDAO.getCustomerById(
                        customerId);

        assertNull(deleted);

        /*
         * Prevent @AfterAll from trying to
         * delete it again.
         */
        customerId = -1;
    }
}
