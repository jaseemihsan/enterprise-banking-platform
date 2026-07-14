package com.bank.controller;

import com.bank.service.DashboardService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final DashboardService dashboardService =
            new DashboardService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute(
                "dashboard",
                dashboardService.getDashboardStats());

        request.setAttribute(
                "recentCustomers",
                dashboardService.getRecentCustomers());

        request.getRequestDispatcher("/dashboard.jsp")
                .forward(request, response);
    }
}
