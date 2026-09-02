package com.bank.controller;

import com.bank.service.TransactionService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

class TransactionHistoryServletTest {

    @Test
    void transactionHistory_shouldLoadTransactionsAndForward() throws Exception {

        TransactionService transactionService =
                mock(TransactionService.class);

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        RequestDispatcher dispatcher =
                mock(RequestDispatcher.class);

        when(transactionService.getAllTransactions())
                .thenReturn(Collections.emptyList());

        when(request.getRequestDispatcher(
                "/transaction-history.jsp"))
                .thenReturn(dispatcher);

        TransactionHistoryServlet servlet =
                new TransactionHistoryServlet(transactionService);

        servlet.doGet(request, response);

        verify(transactionService).getAllTransactions();

        verify(request).setAttribute(
                "transactions",
                Collections.emptyList());

        verify(request).getRequestDispatcher(
                "/transaction-history.jsp");

        verify(dispatcher).forward(request, response);
    }
}
