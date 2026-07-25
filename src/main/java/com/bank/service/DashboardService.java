package com.bank.service;

import com.bank.dto.TransactionTrend;
import com.bank.dao.DashboardDAO;
import com.bank.model.Dashboard;
import com.bank.dto.DashboardStatistics;

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

    public List<TransactionTrend> getTransactionTrend() {

    return dashboardDAO.getTransactionTrend();

}

    public DashboardStatistics getStatistics() {

    DashboardStatistics stats =
            new DashboardStatistics();

    stats.setCustomerCount(
            dashboardDAO.getCustomerCount());

    stats.setAccountCount(
            dashboardDAO.getAccountCount());

    stats.setTransactionCount(
            dashboardDAO.getTransactionCount());

    stats.setTotalDeposits(
            dashboardDAO.getTotalDeposits());

    stats.setTotalWithdrawals(
            dashboardDAO.getTotalWithdrawals());

    stats.setTotalTransfers(
            dashboardDAO.getTotalTransfers());

    return stats;
}
}
