package com.bank.service;

import com.bank.dao.CustomerDAO;
import com.bank.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerDAO customerDAO;

    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {

        customerDAO = mock(CustomerDAO.class);

        customerService =
                new CustomerService(customerDAO);

        customer = new Customer();

        customer.setId(1);
    }

    @Test
    void addCustomer_shouldReturnTrue_whenDaoSucceeds() {

        when(customerDAO.saveCustomer(customer))
                .thenReturn(true);

        boolean result =
                customerService.addCustomer(customer);

        assertTrue(result);

        verify(customerDAO)
                .saveCustomer(customer);
    }

    @Test
    void addCustomer_shouldReturnFalse_whenDaoFails() {

        when(customerDAO.saveCustomer(customer))
                .thenReturn(false);

        boolean result =
                customerService.addCustomer(customer);

        assertFalse(result);

        verify(customerDAO)
                .saveCustomer(customer);
    }

    @Test
    void getAllCustomers_shouldReturnCustomers() {

        List<Customer> customers =
                Arrays.asList(customer);

        when(customerDAO.getAllCustomers())
                .thenReturn(customers);

        List<Customer> result =
                customerService.getAllCustomers();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());

        verify(customerDAO)
                .getAllCustomers();
    }

    @Test
    void getAllCustomers_shouldReturnEmptyList() {

        when(customerDAO.getAllCustomers())
                .thenReturn(Collections.emptyList());

        List<Customer> result =
                customerService.getAllCustomers();

        assertTrue(result.isEmpty());

        verify(customerDAO)
                .getAllCustomers();
    }

    @Test
    void updateCustomer_shouldReturnTrue_whenDaoSucceeds() {

        when(customerDAO.updateCustomer(customer))
                .thenReturn(true);

        boolean result =
                customerService.updateCustomer(customer);

        assertTrue(result);

        verify(customerDAO)
                .updateCustomer(customer);
    }

    @Test
    void updateCustomer_shouldReturnFalse_whenDaoFails() {

        when(customerDAO.updateCustomer(customer))
                .thenReturn(false);

        boolean result =
                customerService.updateCustomer(customer);

        assertFalse(result);

        verify(customerDAO)
                .updateCustomer(customer);
    }

    @Test
    void deleteCustomer_shouldReturnTrue_whenDaoSucceeds() {

        when(customerDAO.deleteCustomer(1))
                .thenReturn(true);

        boolean result =
                customerService.deleteCustomer(1);

        assertTrue(result);

        verify(customerDAO)
                .deleteCustomer(1);
    }

    @Test
    void deleteCustomer_shouldReturnFalse_whenDaoFails() {

        when(customerDAO.deleteCustomer(1))
                .thenReturn(false);

        boolean result =
                customerService.deleteCustomer(1);

        assertFalse(result);

        verify(customerDAO)
                .deleteCustomer(1);
    }

    @Test
    void searchCustomers_shouldReturnMatchingCustomers() {

        List<Customer> customers =
                Arrays.asList(customer);

        when(customerDAO.searchCustomers("admin"))
                .thenReturn(customers);

        List<Customer> result =
                customerService.searchCustomers("admin");

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());

        verify(customerDAO)
                .searchCustomers("admin");
    }

    @Test
    void getCustomerById_shouldReturnCustomer() {

        when(customerDAO.getCustomerById(1))
                .thenReturn(customer);

        Customer result =
                customerService.getCustomerById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());

        verify(customerDAO)
                .getCustomerById(1);
    }

    @Test
    void getCustomerById_shouldReturnNull_whenNotFound() {

        when(customerDAO.getCustomerById(999))
                .thenReturn(null);

        Customer result =
                customerService.getCustomerById(999);

        assertNull(result);

        verify(customerDAO)
                .getCustomerById(999);
    }
}
