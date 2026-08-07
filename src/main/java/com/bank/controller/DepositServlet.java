package com.bank.controller;

import com.bank.service.DepositService;
import com.bank.service.AccountService;
import com.bank.metrics.TransactionMetrics;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/deposit")
public class DepositServlet extends HttpServlet {

    private static final Logger logger =
        LoggerFactory.getLogger(DepositServlet.class);

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

    try {

        boolean success = depositService.deposit(
                accountId,
                amount,
                remarks);

        if (success) {

            TransactionMetrics.incrementDeposits();

            logger.info(
                    "Deposit successful. accountId={}, amount={}, remarks={}",
                    accountId,
                    amount,
                    remarks);

            response.sendRedirect(
                    request.getContextPath()
                            + "/deposit?success=Deposit+Successful");

        } else {

            logger.warn(
                    "Deposit failed. accountId={}, amount={}, remarks={}",
                    accountId,
                    amount,
                    remarks);

            response.sendRedirect(
                    request.getContextPath()
                            + "/deposit?error=Deposit+Failed");
        }

    } catch (Exception e) {

        logger.error(
                "Unexpected error during deposit. accountId={}, amount={}",
                accountId,
                amount,
                e);

        response.sendRedirect(
                request.getContextPath()
                        + "/deposit?error=Unexpected+Error");
    }
 }
}
