package com.bank.controller;

import com.bank.service.AccountService;
import com.bank.service.WithdrawService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.*;

class WithdrawServletTest {

    @Test
    void withdrawGet_shouldLoadActiveAccountsAndForward()
            throws Exception {

        WithdrawService withdrawService =
                mock(WithdrawService.class);

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

        when(request.getRequestDispatcher("/withdraw.jsp"))
                .thenReturn(dispatcher);

        WithdrawServlet servlet =
                new WithdrawServlet(
                        withdrawService,
                        accountService);

        servlet.doGet(request, response);

        verify(accountService).getActiveAccounts();

        verify(request).setAttribute(
                "accounts",
                Collections.emptyList());

        verify(request).getRequestDispatcher(
                "/withdraw.jsp");

        verify(dispatcher).forward(request, response);
    }

    @Test
    void withdraw_shouldRedirectSuccess() throws Exception {

        WithdrawService withdrawService =
                mock(WithdrawService.class);

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
                .thenReturn("Test withdrawal");

        when(withdrawService.withdraw(
                1,
                new BigDecimal("100.00"),
                "Test withdrawal"))
                .thenReturn(true);

        when(request.getContextPath())
                .thenReturn("/banking-app");

        WithdrawServlet servlet =
                new WithdrawServlet(
                        withdrawService,
                        accountService);

        servlet.doPost(request, response);

        verify(withdrawService).withdraw(
                1,
                new BigDecimal("100.00"),
                "Test withdrawal");

        verify(response).sendRedirect(
                "/banking-app/withdraw?success=Withdrawal+Successful");
    }

    @Test
    void withdraw_shouldRedirectFailure() throws Exception {

        WithdrawService withdrawService =
                mock(WithdrawService.class);

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
                .thenReturn("Failed withdrawal");

        when(withdrawService.withdraw(
                1,
                new BigDecimal("50.00"),
                "Failed withdrawal"))
                .thenReturn(false);

        when(request.getContextPath())
                .thenReturn("/banking-app");

        WithdrawServlet servlet =
                new WithdrawServlet(
                        withdrawService,
                        accountService);

        servlet.doPost(request, response);

        verify(response).sendRedirect(
                "/banking-app/withdraw?error=Withdrawal+Failed");
    }

    @Test
    void withdraw_shouldRedirectUnexpectedError() throws Exception {

        WithdrawService withdrawService =
                mock(WithdrawService.class);

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

        when(withdrawService.withdraw(
                1,
                new BigDecimal("25.00"),
                "Error test"))
                .thenThrow(
                        new RuntimeException("Database error"));

        when(request.getContextPath())
                .thenReturn("/banking-app");

        WithdrawServlet servlet =
                new WithdrawServlet(
                        withdrawService,
                        accountService);

        servlet.doPost(request, response);

        verify(response).sendRedirect(
                "/banking-app/withdraw?error=Unexpected+Error");
    }
}
