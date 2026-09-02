package com.bank.controller;

import com.bank.service.DashboardService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class DashboardServletTest {

    private DashboardService dashboardService;
    private DashboardServlet servlet;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {

        dashboardService = mock(DashboardService.class);

        servlet = new DashboardServlet(dashboardService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getSession())
                .thenReturn(session);

        when(request.getRequestDispatcher("/dashboard.jsp"))
                .thenReturn(dispatcher);
    }

    @Test
    void doGet_shouldLoadDashboardAndForward()
            throws Exception {

        when(session.getAttribute("username"))
                .thenReturn("testuser");

        when(dashboardService.getDashboardStats())
                .thenReturn(null);

        when(dashboardService.getRecentCustomers())
                .thenReturn(java.util.Collections.emptyList());

        when(dashboardService.getStatistics())
                .thenReturn(null);

        when(dashboardService.getTransactionTrend())
                .thenReturn(java.util.Collections.emptyList());

        servlet.doGet(request, response);

        verify(session)
                .getAttribute("username");

        verify(dashboardService)
                .getDashboardStats();

        verify(dashboardService)
                .getRecentCustomers();

        verify(dashboardService)
                .getStatistics();

        verify(dashboardService)
                .getTransactionTrend();

        verify(request)
                .setAttribute(eq("dashboard"), isNull());

        verify(request)
                .setAttribute(eq("recentCustomers"), any());

        verify(request)
                .setAttribute(eq("statistics"), isNull());

        verify(request)
                .setAttribute(eq("transactionTrend"), any());

        verify(dispatcher)
                .forward(request, response);

        verify(response, never())
                .sendError(anyInt(), anyString());
    }

    @Test
    void doGet_shouldReturn500WhenServiceFails()
            throws Exception {

        when(session.getAttribute("username"))
                .thenReturn("testuser");

        when(dashboardService.getDashboardStats())
                .thenThrow(new RuntimeException("Database error"));

        servlet.doGet(request, response);

        verify(response)
                .sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Unable to load dashboard");

        verify(dispatcher, never())
                .forward(any(), any());
    }
}
