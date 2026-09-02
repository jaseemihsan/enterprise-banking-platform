package com.bank.service;

import com.bank.dao.CustomerDAO;
import com.bank.model.Customer;

import java.util.List;

public class CustomerService {

    private final CustomerDAO customerDAO;

    /*
     * Default constructor used by the application.
     */
    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }

    /*
     * Constructor used for unit testing / dependency injection.
     */
    CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public boolean addCustomer(Customer customer) {
        return customerDAO.saveCustomer(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    public boolean updateCustomer(Customer customer) {
        return customerDAO.updateCustomer(customer);
    }

    public boolean deleteCustomer(int id) {
        return customerDAO.deleteCustomer(id);
    }

    public List<Customer> searchCustomers(String keyword) {
        return customerDAO.searchCustomers(keyword);
    }

    public Customer getCustomerById(int id) {
        return customerDAO.getCustomerById(id);
    }
}
