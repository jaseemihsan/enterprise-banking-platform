package com.bank.controller;

import com.bank.model.User;
import com.bank.service.AuditLogService;
import com.bank.service.LoginService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class LoginServletTest {

    private LoginService loginService;
    private AuditLogService auditLogService;

    private LoginServlet servlet;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {

        loginService = mock(LoginService.class);
        auditLogService = mock(AuditLogService.class);

        servlet = new LoginServlet(
                loginService,
                auditLogService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
    }

    @Test
    void login_shouldRedirectToDashboard_whenCredentialsAreValid()
            throws Exception {

        User user = new User();

        user.setId(1);
        user.setUsername("testuser");
        user.setRoleName("ADMIN");
        user.setStatus("ACTIVE");

        when(request.getParameter("username"))
                .thenReturn("testuser");

        when(request.getParameter("password"))
                .thenReturn("password123");

        when(loginService.authenticate(
                "testuser",
                "password123"))
                .thenReturn(user);

        when(request.getSession())
                .thenReturn(session);

        when(request.getRemoteAddr())
                .thenReturn("192.168.30.157");

        when(request.getContextPath())
                .thenReturn("");

        servlet.doPost(request, response);

        verify(loginService)
                .authenticate(
                        "testuser",
                        "password123");

        verify(session)
                .setAttribute("user", user);

        verify(session)
                .setAttribute("username", "testuser");

        verify(session)
                .setAttribute("role", "ADMIN");

        verify(auditLogService)
                .log(
                        "testuser",
                        "LOGIN",
                        "Authentication",
                        "Login successful",
                        "192.168.30.157");

        verify(response)
                .sendRedirect("/dashboard");

        verify(request, never())
                .setAttribute(
                        eq("error"),
                        anyString());

        verify(request, never())
                .getRequestDispatcher(anyString());
    }

    @Test
    void login_shouldForwardToLoginPage_whenCredentialsAreInvalid()
            throws Exception {

        when(request.getParameter("username"))
                .thenReturn("wronguser");

        when(request.getParameter("password"))
                .thenReturn("wrongpassword");

        when(loginService.authenticate(
                "wronguser",
                "wrongpassword"))
                .thenReturn(null);

        when(request.getRemoteAddr())
                .thenReturn("192.168.30.157");

        when(request.getRequestDispatcher("/login.jsp"))
                .thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(loginService)
                .authenticate(
                        "wronguser",
                        "wrongpassword");

        verify(request)
                .setAttribute(
                        "error",
                        "Invalid Username or Password");

        verify(dispatcher)
                .forward(request, response);

        verify(response, never())
                .sendRedirect(anyString());

        verify(auditLogService, never())
                .log(
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString());
    }
}
