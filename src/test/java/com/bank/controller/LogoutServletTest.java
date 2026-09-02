package com.bank.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class LogoutServletTest {

    @Test
    void logout_shouldInvalidateSessionAndRedirect() throws Exception {

        LogoutServlet servlet = new LogoutServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession(false)).thenReturn(session);
        when(request.getContextPath()).thenReturn("/banking-app");

        servlet.doGet(request, response);

        verify(session).invalidate();
        verify(response).sendRedirect("/banking-app/");
    }

    @Test
    void logout_shouldRedirectWhenNoSessionExists() throws Exception {

        LogoutServlet servlet = new LogoutServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/banking-app");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/banking-app/");
    }
}
