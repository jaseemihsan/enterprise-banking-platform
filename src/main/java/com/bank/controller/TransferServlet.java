package com.bank.controller;

import com.bank.service.AccountService;
import com.bank.service.TransferService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/transfer")
public class TransferServlet extends HttpServlet {

    private final AccountService accountService = new AccountService();
       private final TransferService transferService =
            new TransferService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute(
                "accounts",
                accountService.getAllAccounts());

        request.getRequestDispatcher("/transfer.jsp")
                .forward(request, response);
    }

    @Override
protected void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException {

    int fromAccountId =
            Integer.parseInt(request.getParameter("fromAccountId"));

    int toAccountId =
            Integer.parseInt(request.getParameter("toAccountId"));

    java.math.BigDecimal amount =
            new java.math.BigDecimal(request.getParameter("amount"));

    String remarks =
            request.getParameter("remarks");

    boolean success =
            transferService.transfer(
                    fromAccountId,
                    toAccountId,
                    amount,
                    remarks
            );

    if (success) {

        response.sendRedirect(
        request.getContextPath() + "/transfer");

    } else {

        response.sendRedirect(
        request.getContextPath() + "/transfer");

    }

}
}
