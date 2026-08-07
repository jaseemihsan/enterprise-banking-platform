package com.bank.controller;

import com.bank.service.AccountService;
import com.bank.service.WithdrawService;
import com.bank.metrics.TransactionMetrics;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/withdraw")
public class WithdrawServlet extends HttpServlet {

    private static final Logger logger =
        LoggerFactory.getLogger(WithdrawServlet.class);

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

    try {

        boolean success =
                withdrawService.withdraw(
                        accountId,
                        amount,
                        remarks);

        if (success) {

            TransactionMetrics.incrementWithdrawals();

            logger.info(
                    "Withdrawal successful. accountId={}, amount={}, remarks={}",
                    accountId,
                    amount,
                    remarks);

            response.sendRedirect(
                    request.getContextPath()
                            + "/withdraw?success=Withdrawal+Successful");

        } else {

            logger.warn(
                    "Withdrawal failed. accountId={}, amount={}, remarks={}",
                    accountId,
                    amount,
                    remarks);

            response.sendRedirect(
                    request.getContextPath()
                            + "/withdraw?error=Withdrawal+Failed");
        }

    } catch (Exception e) {

        logger.error(
                "Unexpected error during withdrawal. accountId={}, amount={}",
                accountId,
                amount,
                e);

        response.sendRedirect(
                request.getContextPath()
                        + "/withdraw?error=Unexpected+Error");
    }
}
}
