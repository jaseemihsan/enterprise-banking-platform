package com.bank.controller;

import com.bank.service.AccountService;
import com.bank.service.WithdrawService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/withdraw")
public class WithdrawServlet extends HttpServlet {

    private final WithdrawService withdrawService =
            new WithdrawService();

    private final AccountService accountService =
            new AccountService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute(
                "accounts",
                accountService.getActiveAccounts());

        request.getRequestDispatcher("/withdraw.jsp")
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
                withdrawService.withdraw(
                        accountId,
                        amount,
                        remarks);

        if (success) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/withdraw?success=Withdrawal+Successful");

        } else {

            response.sendRedirect(
                    request.getContextPath()
                            + "/withdraw?error=Withdrawal+Failed");

        }

    }
}
