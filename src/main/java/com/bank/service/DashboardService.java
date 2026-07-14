package com.bank.service;

import com.bank.dao.DashboardDAO;
import com.bank.model.Dashboard;

import com.bank.model.Customer;
import java.util.List;

public class DashboardService {

    private final DashboardDAO dashboardDAO =
            new DashboardDAO();

    public Dashboard getDashboardStats() {

        return dashboardDAO.getDashboardStats();

    }
    
    public List<Customer> getRecentCustomers(){

    return dashboardDAO.getRecentCustomers();

    }
}
