package com.bank.controller;

import com.bank.service.AccountService;
import com.bank.service.TransferService;
import com.bank.metrics.TransactionMetrics;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/transfer")
public class TransferServlet extends HttpServlet {

   private static final Logger logger =
        LoggerFactory.getLogger(TransferServlet.class);

private final AccountService accountService;
private final TransferService transferService;

public TransferServlet() {
    this.accountService = new AccountService();
    this.transferService = new TransferService();
}

TransferServlet(AccountService accountService,
                TransferService transferService) {
    this.accountService = accountService;
    this.transferService = transferService;
}
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

    try {

        boolean success =
                transferService.transfer(
                        fromAccountId,
                        toAccountId,
                        amount,
                        remarks);

        if (success) {

            TransactionMetrics.incrementTransfers();

            logger.info(
                    "Transfer completed. fromAccountId={}, toAccountId={}, amount={}, remarks={}",
                    fromAccountId,
                    toAccountId,
                    amount,
                    remarks);

            response.sendRedirect(
                    request.getContextPath() + "/transfer");

        } else {

            logger.warn(
                    "Transfer failed. fromAccountId={}, toAccountId={}, amount={}, remarks={}",
                    fromAccountId,
                    toAccountId,
                    amount,
                    remarks);

            response.sendRedirect(
                    request.getContextPath() + "/transfer");
        }

    } catch (Exception e) {

        logger.error(
                "Unexpected error during transfer. fromAccountId={}, toAccountId={}, amount={}",
                fromAccountId,
                toAccountId,
                amount,
                e);

        response.sendRedirect(
                request.getContextPath()
                        + "/transfer?error=Unexpected+Error");
    }
}
}
