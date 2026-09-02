package com.bank.controller;

import com.bank.service.AccountService;
import com.bank.service.StatementService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/statement")
public class StatementServlet extends HttpServlet {

private final AccountService accountService;
private final StatementService statementService;

public StatementServlet() {
    this.accountService = new AccountService();
    this.statementService = new StatementService();
}

StatementServlet(AccountService accountService,
                 StatementService statementService) {
    this.accountService = accountService;
    this.statementService = statementService;
}
    @Override
protected void doGet(HttpServletRequest request,
                     HttpServletResponse response)
        throws ServletException, IOException {

    request.setAttribute(
            "accounts",
            accountService.getAllAccounts());

    String accountId = request.getParameter("accountId");

    if (accountId != null && !accountId.isBlank()) {

        int id = Integer.parseInt(accountId);

        request.setAttribute(
                "selectedAccount",
                accountService.getAccountById(id));

        request.setAttribute(
                "transactions",
                statementService.getStatement(id));
    }

    request.getRequestDispatcher("/statement.jsp")
            .forward(request, response);
}
}
