package com.bank.controller;

import com.bank.service.TransactionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/transactions")
public class TransactionHistoryServlet extends HttpServlet {

    private final TransactionService transactionService;

    public TransactionHistoryServlet() {
        this.transactionService = new TransactionService();
    }

    TransactionHistoryServlet(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute(
                "transactions",
                transactionService.getAllTransactions());

        request.getRequestDispatcher("/transaction-history.jsp")
                .forward(request, response);
    }
}
