package com.bank.controller;

import com.bank.service.DashboardService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final Logger logger =
        LoggerFactory.getLogger(DashboardServlet.class);

    private final DashboardService dashboardService =
            new DashboardService();

    @Override
protected void doGet(HttpServletRequest request,
                     HttpServletResponse response)
        throws ServletException, IOException {

    try {

        String username =
                (String) request.getSession()
                        .getAttribute("username");

        logger.info(
                "Dashboard accessed. username={}",
                username);

        request.setAttribute(
                "dashboard",
                dashboardService.getDashboardStats());

        request.setAttribute(
                "recentCustomers",
                dashboardService.getRecentCustomers());

        request.setAttribute(
                "statistics",
                dashboardService.getStatistics());

        request.setAttribute(
                "transactionTrend",
                dashboardService.getTransactionTrend());

        request.getRequestDispatcher("/dashboard.jsp")
                .forward(request, response);

    } catch (Exception e) {

        logger.error(
                "Error loading dashboard",
                e);

        response.sendError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Unable to load dashboard");
    }

 }
}
