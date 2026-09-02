package com.bank.controller;

import com.bank.service.AccountService;
import com.bank.service.StatementService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

class StatementServletTest {

    @Test
    void statement_withoutAccountId_shouldLoadAccountsAndForward()
            throws Exception {

        AccountService accountService =
                mock(AccountService.class);

        StatementService statementService =
                mock(StatementService.class);

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        RequestDispatcher dispatcher =
                mock(RequestDispatcher.class);

        when(accountService.getAllAccounts())
                .thenReturn(Collections.emptyList());

        when(request.getParameter("accountId"))
                .thenReturn(null);

        when(request.getRequestDispatcher("/statement.jsp"))
                .thenReturn(dispatcher);

        StatementServlet servlet =
                new StatementServlet(
                        accountService,
                        statementService);

        servlet.doGet(request, response);

        verify(accountService).getAllAccounts();

        verify(request).setAttribute(
                "accounts",
                Collections.emptyList());

        verify(request).getRequestDispatcher(
                "/statement.jsp");

        verify(dispatcher).forward(request, response);

        verify(accountService, never())
                .getAccountById(anyInt());

        verify(statementService, never())
                .getStatement(anyInt());
    }

    @Test
    void statement_withAccountId_shouldLoadAccountAndTransactions()
            throws Exception {

        AccountService accountService =
                mock(AccountService.class);

        StatementService statementService =
                mock(StatementService.class);

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        RequestDispatcher dispatcher =
                mock(RequestDispatcher.class);

        when(accountService.getAllAccounts())
                .thenReturn(Collections.emptyList());

        when(request.getParameter("accountId"))
                .thenReturn("1");

        when(accountService.getAccountById(1))
                .thenReturn(null);

        when(statementService.getStatement(1))
                .thenReturn(Collections.emptyList());

        when(request.getRequestDispatcher("/statement.jsp"))
                .thenReturn(dispatcher);

        StatementServlet servlet =
                new StatementServlet(
                        accountService,
                        statementService);

        servlet.doGet(request, response);

        verify(accountService).getAllAccounts();

        verify(accountService).getAccountById(1);

        verify(statementService).getStatement(1);

        verify(request).setAttribute(
                "selectedAccount",
                null);

        verify(request).setAttribute(
                "transactions",
                Collections.emptyList());

        verify(request).getRequestDispatcher(
                "/statement.jsp");

        verify(dispatcher).forward(request, response);
    }
}
