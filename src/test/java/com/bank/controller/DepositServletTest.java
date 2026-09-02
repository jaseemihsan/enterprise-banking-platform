package com.bank.controller;

import com.bank.service.AccountService;
import com.bank.service.DepositService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.*;

class DepositServletTest {

    @Test
    void deposit_shouldRedirectSuccess() throws Exception {

        DepositService depositService =
                mock(DepositService.class);

        AccountService accountService =
                mock(AccountService.class);

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        when(request.getParameter("accountId"))
                .thenReturn("1");

        when(request.getParameter("amount"))
                .thenReturn("100.00");

        when(request.getParameter("remarks"))
                .thenReturn("Test deposit");

        when(depositService.deposit(
                1,
                new BigDecimal("100.00"),
                "Test deposit"))
                .thenReturn(true);

        when(request.getContextPath())
                .thenReturn("/banking-app");

        DepositServlet servlet =
                new DepositServlet(
                        depositService,
                        accountService);

        servlet.doPost(request, response);

        verify(depositService).deposit(
                1,
                new BigDecimal("100.00"),
                "Test deposit");

        verify(response).sendRedirect(
                "/banking-app/deposit?success=Deposit+Successful");
    }

    @Test
    void deposit_shouldRedirectFailure() throws Exception {

        DepositService depositService =
                mock(DepositService.class);

        AccountService accountService =
                mock(AccountService.class);

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        when(request.getParameter("accountId"))
                .thenReturn("1");

        when(request.getParameter("amount"))
                .thenReturn("50.00");

        when(request.getParameter("remarks"))
                .thenReturn("Failed deposit");

        when(depositService.deposit(
                1,
                new BigDecimal("50.00"),
                "Failed deposit"))
                .thenReturn(false);

        when(request.getContextPath())
                .thenReturn("/banking-app");

        DepositServlet servlet =
                new DepositServlet(
                        depositService,
                        accountService);

        servlet.doPost(request, response);

        verify(response).sendRedirect(
                "/banking-app/deposit?error=Deposit+Failed");
    }

    @Test
    void deposit_shouldRedirectUnexpectedError() throws Exception {

        DepositService depositService =
                mock(DepositService.class);

        AccountService accountService =
                mock(AccountService.class);

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        when(request.getParameter("accountId"))
                .thenReturn("1");

        when(request.getParameter("amount"))
                .thenReturn("25.00");

        when(request.getParameter("remarks"))
                .thenReturn("Error test");

        when(depositService.deposit(
                1,
                new BigDecimal("25.00"),
                "Error test"))
                .thenThrow(
                        new RuntimeException("Database error"));

        when(request.getContextPath())
                .thenReturn("/banking-app");

        DepositServlet servlet =
                new DepositServlet(
                        depositService,
                        accountService);

        servlet.doPost(request, response);

        verify(response).sendRedirect(
                "/banking-app/deposit?error=Unexpected+Error");
    }

    @Test
    void depositGet_shouldLoadActiveAccountsAndForward()
            throws Exception {

        DepositService depositService =
                mock(DepositService.class);

        AccountService accountService =
                mock(AccountService.class);

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        RequestDispatcher dispatcher =
                mock(RequestDispatcher.class);

        when(accountService.getActiveAccounts())
                .thenReturn(Collections.emptyList());

        when(request.getRequestDispatcher("/deposit.jsp"))
                .thenReturn(dispatcher);

        DepositServlet servlet =
                new DepositServlet(
                        depositService,
                        accountService);

        servlet.doGet(request, response);

        verify(accountService).getActiveAccounts();

        verify(request).setAttribute(
                "accounts",
                Collections.emptyList());

        verify(request).getRequestDispatcher(
                "/deposit.jsp");

        verify(dispatcher).forward(request, response);
    }
}
