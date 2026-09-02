package com.bank.dao;

import com.bank.model.Dashboard;
import com.bank.model.Customer;
import com.bank.dto.TransactionTrend;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DashboardDAOTest {

    private final DashboardDAO dashboardDAO =
            new DashboardDAO();

    @Test
    void getDashboardStats_shouldReturnDashboard() {

        Dashboard dashboard =
                dashboardDAO.getDashboardStats();

        assertNotNull(dashboard);
        assertTrue(dashboard.getTotalCustomers() >= 0);
        assertTrue(dashboard.getTotalAccounts() >= 0);
        assertTrue(dashboard.getTotalTransactions() >= 0);
        assertNotNull(dashboard.getTotalBalance());
    }

    @Test
    void getRecentCustomers_shouldReturnList() {

        List<Customer> customers =
                dashboardDAO.getRecentCustomers();

        assertNotNull(customers);
        assertTrue(customers.size() <= 5);
    }

    @Test
    void getCustomerCount_shouldBeNonNegative() {

        assertTrue(
                dashboardDAO.getCustomerCount() >= 0);
    }

    @Test
    void getAccountCount_shouldBeNonNegative() {

        assertTrue(
                dashboardDAO.getAccountCount() >= 0);
    }

    @Test
    void getTransactionCount_shouldBeNonNegative() {

        assertTrue(
                dashboardDAO.getTransactionCount() >= 0);
    }

    @Test
    void getTotalDeposits_shouldNotBeNull() {

        BigDecimal value =
                dashboardDAO.getTotalDeposits();

        assertNotNull(value);
        assertTrue(value.compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    void getTotalWithdrawals_shouldNotBeNull() {

        BigDecimal value =
                dashboardDAO.getTotalWithdrawals();

        assertNotNull(value);
        assertTrue(value.compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    void getTotalTransfers_shouldNotBeNull() {

        BigDecimal value =
                dashboardDAO.getTotalTransfers();

        assertNotNull(value);
        assertTrue(value.compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    void getTransactionTrend_shouldReturnList() {

        List<TransactionTrend> trends =
                dashboardDAO.getTransactionTrend();

        assertNotNull(trends);
    }
}
