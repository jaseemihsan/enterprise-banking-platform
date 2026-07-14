package com.bank.controller;

import com.bank.service.DepositService;
import com.bank.service.AccountService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/deposit")
public class DepositServlet extends HttpServlet {

    private final DepositService depositService =
            new DepositService();

    @Override
protected void doGet(HttpServletRequest request,
                     HttpServletResponse response)
        throws ServletException, IOException {

    AccountService accountService =
            new AccountService();

    request.setAttribute(
            "accounts",
            accountService.getActiveAccounts());

    request.getRequestDispatcher("/deposit.jsp")
            .forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        int accountId =
                Integer.parseInt(request.getParameter("accountId"));

        BigDecimal amount =
                new BigDecimal(request.getParameter("amount"));

        String remarks =
                request.getParameter("remarks");

        boolean success =
                depositService.deposit(
                        accountId,
                        amount,
                        remarks);

        if (success) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/deposit?success=Deposit+Successful");

        } else {

            response.sendRedirect(
                    request.getContextPath()
                            + "/deposit?error=Deposit+Failed");

        }

    }

}
