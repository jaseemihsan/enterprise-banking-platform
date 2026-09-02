package com.bank.controller;

import com.bank.service.AccountService;
import com.bank.service.TransferService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.*;

class TransferServletTest {

    @Test
    void transferGet_shouldLoadAccountsAndForward()
            throws Exception {

        AccountService accountService =
                mock(AccountService.class);

        TransferService transferService =
                mock(TransferService.class);

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        RequestDispatcher dispatcher =
                mock(RequestDispatcher.class);

        when(accountService.getAllAccounts())
                .thenReturn(Collections.emptyList());

        when(request.getRequestDispatcher("/transfer.jsp"))
                .thenReturn(dispatcher);

        TransferServlet servlet =
                new TransferServlet(
                        accountService,
                        transferService);

        servlet.doGet(request, response);

        verify(accountService).getAllAccounts();

        verify(request).setAttribute(
                "accounts",
                Collections.emptyList());

        verify(request).getRequestDispatcher(
                "/transfer.jsp");

        verify(dispatcher).forward(request, response);
    }

    @Test
    void transfer_shouldRedirectSuccess() throws Exception {

        AccountService accountService =
                mock(AccountService.class);

        TransferService transferService =
                mock(TransferService.class);

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        when(request.getParameter("fromAccountId"))
                .thenReturn("1");

        when(request.getParameter("toAccountId"))
                .thenReturn("2");

        when(request.getParameter("amount"))
                .thenReturn("100.00");

        when(request.getParameter("remarks"))
                .thenReturn("Test transfer");

        when(transferService.transfer(
                1,
                2,
                new BigDecimal("100.00"),
                "Test transfer"))
                .thenReturn(true);

        when(request.getContextPath())
                .thenReturn("/banking-app");

        TransferServlet servlet =
                new TransferServlet(
                        accountService,
                        transferService);

        servlet.doPost(request, response);

        verify(transferService).transfer(
                1,
                2,
                new BigDecimal("100.00"),
                "Test transfer");

        verify(response).sendRedirect(
                "/banking-app/transfer");
    }

    @Test
    void transfer_shouldRedirectFailure() throws Exception {

        AccountService accountService =
                mock(AccountService.class);

        TransferService transferService =
                mock(TransferService.class);

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        when(request.getParameter("fromAccountId"))
                .thenReturn("1");

        when(request.getParameter("toAccountId"))
                .thenReturn("2");

        when(request.getParameter("amount"))
                .thenReturn("50.00");

        when(request.getParameter("remarks"))
                .thenReturn("Failed transfer");

        when(transferService.transfer(
                1,
                2,
                new BigDecimal("50.00"),
                "Failed transfer"))
                .thenReturn(false);

        when(request.getContextPath())
                .thenReturn("/banking-app");

        TransferServlet servlet =
                new TransferServlet(
                        accountService,
                        transferService);

        servlet.doPost(request, response);

        verify(response).sendRedirect(
                "/banking-app/transfer");
    }

    @Test
    void transfer_shouldRedirectUnexpectedError()
            throws Exception {

        AccountService accountService =
                mock(AccountService.class);

        TransferService transferService =
                mock(TransferService.class);

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        when(request.getParameter("fromAccountId"))
                .thenReturn("1");

        when(request.getParameter("toAccountId"))
                .thenReturn("2");

        when(request.getParameter("amount"))
                .thenReturn("25.00");

        when(request.getParameter("remarks"))
                .thenReturn("Error test");

        when(transferService.transfer(
                1,
                2,
                new BigDecimal("25.00"),
                "Error test"))
                .thenThrow(
                        new RuntimeException("Database error"));

        when(request.getContextPath())
                .thenReturn("/banking-app");

        TransferServlet servlet =
                new TransferServlet(
                        accountService,
                        transferService);

        servlet.doPost(request, response);

        verify(response).sendRedirect(
                "/banking-app/transfer?error=Unexpected+Error");
    }
}
