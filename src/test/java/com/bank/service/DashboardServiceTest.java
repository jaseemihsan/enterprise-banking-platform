package com.bank.service;

import com.bank.dao.DashboardDAO;
import com.bank.dto.DashboardStatistics;
import com.bank.dto.TransactionTrend;
import com.bank.model.Customer;
import com.bank.model.Dashboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DashboardServiceTest {

    private DashboardDAO dashboardDAO;
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {

        dashboardDAO = mock(DashboardDAO.class);

        dashboardService =
                new DashboardService(dashboardDAO);
    }

    @Test
    void getDashboardStats_shouldReturnDashboard() {

        Dashboard dashboard = new Dashboard();

        when(dashboardDAO.getDashboardStats())
                .thenReturn(dashboard);

        Dashboard result =
                dashboardService.getDashboardStats();

        assertNotNull(result);

        verify(dashboardDAO)
                .getDashboardStats();
    }

    @Test
    void getDashboardStats_shouldReturnNull_whenDaoReturnsNull() {

        when(dashboardDAO.getDashboardStats())
                .thenReturn(null);

        Dashboard result =
                dashboardService.getDashboardStats();

        assertNull(result);

        verify(dashboardDAO)
                .getDashboardStats();
    }

    @Test
    void getRecentCustomers_shouldReturnCustomers() {

        Customer customer = new Customer();
        customer.setId(1);

        List<Customer> customers =
                Arrays.asList(customer);

        when(dashboardDAO.getRecentCustomers())
                .thenReturn(customers);

        List<Customer> result =
                dashboardService.getRecentCustomers();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());

        verify(dashboardDAO)
                .getRecentCustomers();
    }

    @Test
    void getRecentCustomers_shouldReturnEmptyList() {

        when(dashboardDAO.getRecentCustomers())
                .thenReturn(Collections.emptyList());

        List<Customer> result =
                dashboardService.getRecentCustomers();

        assertTrue(result.isEmpty());

        verify(dashboardDAO)
                .getRecentCustomers();
    }

    @Test
    void getTransactionTrend_shouldReturnTrends() {

        TransactionTrend trend =
                new TransactionTrend();

        List<TransactionTrend> trends =
                Arrays.asList(trend);

        when(dashboardDAO.getTransactionTrend())
                .thenReturn(trends);

        List<TransactionTrend> result =
                dashboardService.getTransactionTrend();

        assertEquals(1, result.size());

        verify(dashboardDAO)
                .getTransactionTrend();
    }

    @Test
    void getTransactionTrend_shouldReturnEmptyList() {

        when(dashboardDAO.getTransactionTrend())
                .thenReturn(Collections.emptyList());

        List<TransactionTrend> result =
                dashboardService.getTransactionTrend();

        assertTrue(result.isEmpty());

        verify(dashboardDAO)
                .getTransactionTrend();
    }

    @Test
    void getStatistics_shouldBuildStatisticsFromDaoValues() {

        when(dashboardDAO.getCustomerCount())
                .thenReturn(100);

        when(dashboardDAO.getAccountCount())
                .thenReturn(150);

        when(dashboardDAO.getTransactionCount())
                .thenReturn(500);

        when(dashboardDAO.getTotalDeposits())
                .thenReturn(new BigDecimal("100000.00"));

        when(dashboardDAO.getTotalWithdrawals())
                .thenReturn(new BigDecimal("40000.00"));

        when(dashboardDAO.getTotalTransfers())
                .thenReturn(new BigDecimal("25000.00"));

        DashboardStatistics result =
                dashboardService.getStatistics();

        assertNotNull(result);

        assertEquals(100,
                result.getCustomerCount());

        assertEquals(150,
                result.getAccountCount());

        assertEquals(500,
                result.getTransactionCount());

        assertEquals(
                new BigDecimal("100000.00"),
                result.getTotalDeposits());

        assertEquals(
                new BigDecimal("40000.00"),
                result.getTotalWithdrawals());

        assertEquals(
                new BigDecimal("25000.00"),
                result.getTotalTransfers());

        verify(dashboardDAO)
                .getCustomerCount();

        verify(dashboardDAO)
                .getAccountCount();

        verify(dashboardDAO)
                .getTransactionCount();

        verify(dashboardDAO)
                .getTotalDeposits();

        verify(dashboardDAO)
                .getTotalWithdrawals();

        verify(dashboardDAO)
                .getTotalTransfers();
    }
}
