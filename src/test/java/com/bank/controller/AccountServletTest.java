package com.bank.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AccountServletTest {

    private final AccountServlet servlet = new AccountServlet();

    private HttpServletRequest request(String path) {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getServletPath()).thenReturn(path);
        when(request.getContextPath()).thenReturn("");

        return request;
    }

    private HttpServletResponse response() {
        return mock(HttpServletResponse.class);
    }

    @Test
    void listAccounts_shouldForwardToAccountsPage() throws Exception {

        HttpServletRequest request = request("/accounts");
        HttpServletResponse response = response();

        RequestDispatcher dispatcher =
                mock(RequestDispatcher.class);

        when(request.getParameter("search"))
                .thenReturn(null);

        when(request.getRequestDispatcher("/accounts.jsp"))
                .thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request)
                .setAttribute(eq("customers"), any());

        verify(request)
                .setAttribute(eq("accounts"), any());

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void searchAccounts_shouldForwardToAccountsPage() throws Exception {

        HttpServletRequest request = request("/accounts");
        HttpServletResponse response = response();

        RequestDispatcher dispatcher =
                mock(RequestDispatcher.class);

        when(request.getParameter("search"))
                .thenReturn("ACC");

        when(request.getRequestDispatcher("/accounts.jsp"))
                .thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request)
                .setAttribute(eq("customers"), any());

        verify(request)
                .setAttribute(eq("accounts"), any());

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void searchAccounts_blankKeyword_shouldListAll() throws Exception {

        HttpServletRequest request = request("/accounts");
        HttpServletResponse response = response();

        RequestDispatcher dispatcher =
                mock(RequestDispatcher.class);

        when(request.getParameter("search"))
                .thenReturn("   ");

        when(request.getRequestDispatcher("/accounts.jsp"))
                .thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request)
                .setAttribute(eq("accounts"), any());

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void editAccount_shouldForwardToEditPage() throws Exception {

        HttpServletRequest request =
                request("/accounts/edit");

        HttpServletResponse response = response();

        RequestDispatcher dispatcher =
                mock(RequestDispatcher.class);

        when(request.getParameter("id"))
                .thenReturn("1");

        when(request.getRequestDispatcher("/edit-account.jsp"))
                .thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request)
                .setAttribute(eq("account"), any());

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void closeAccount_shouldRedirect() throws Exception {

        HttpServletRequest request =
                request("/accounts/close");

        HttpServletResponse response = response();

        when(request.getParameter("id"))
                .thenReturn("1");

        servlet.doGet(request, response);

        verify(response)
                .sendRedirect(
                        "/accounts?success=Account+Closed");
    }

    @Test
    void createAccount_shouldRedirectOnSuccessOrFailure()
            throws Exception {

        HttpServletRequest request =
                request("/accounts");

        HttpServletResponse response = response();

        when(request.getParameter("customerId"))
                .thenReturn("1");

        when(request.getParameter("accountType"))
                .thenReturn("SAVINGS");

        when(request.getParameter("balance"))
                .thenReturn("1000.00");

        servlet.doPost(request, response);

        verify(response)
                .sendRedirect(anyString());
    }

    @Test
    void updateAccount_shouldRedirect() throws Exception {

        HttpServletRequest request =
                request("/accounts/update");

        HttpServletResponse response = response();

        when(request.getParameter("id"))
                .thenReturn("1");

        when(request.getParameter("accountType"))
                .thenReturn("CURRENT");

        when(request.getParameter("status"))
                .thenReturn("ACTIVE");

        servlet.doPost(request, response);

        verify(response)
                .sendRedirect(anyString());
    }

    @Test
    void unknownPostPath_shouldDoNothing() throws Exception {

        HttpServletRequest request =
                request("/accounts/unknown");

        HttpServletResponse response = response();

        servlet.doPost(request, response);

        verify(response, never())
                .sendRedirect(anyString());
    }

    @Test
    void accountNumberGenerator_shouldBeUsedDuringCreate()
            throws Exception {

        HttpServletRequest request =
                request("/accounts");

        HttpServletResponse response = response();

        when(request.getParameter("customerId"))
                .thenReturn("1");

        when(request.getParameter("accountType"))
                .thenReturn("SAVINGS");

        when(request.getParameter("balance"))
                .thenReturn("500.00");

        servlet.doPost(request, response);

        verify(response)
                .sendRedirect(anyString());
    }
}
