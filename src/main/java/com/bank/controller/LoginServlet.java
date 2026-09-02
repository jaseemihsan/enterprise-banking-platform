package com.bank.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bank.metrics.LoginMetrics;
import com.bank.model.User;
import com.bank.service.AuditLogService;
import com.bank.service.LoginService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger logger =
            LoggerFactory.getLogger(LoginServlet.class);

private final LoginService loginService;
private final AuditLogService auditLogService;

public LoginServlet() {
    this.loginService = new LoginService();
    this.auditLogService = new AuditLogService();
}

LoginServlet(LoginService loginService,
             AuditLogService auditLogService) {
    this.loginService = loginService;
    this.auditLogService = auditLogService;
}
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = loginService.authenticate(username, password);

        if (user != null) {

            LoginMetrics.incrementLogins();

            HttpSession session = request.getSession();

            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRoleName());

            auditLogService.log(
                    user.getUsername(),
                    "LOGIN",
                    "Authentication",
                    "Login successful",
                    request.getRemoteAddr());
            logger.info(
                    "User logged in. username={}, role={}, ip={}",
                    user.getUsername(),
                    user.getRoleName(),
                    request.getRemoteAddr());

            response.sendRedirect(
                    request.getContextPath() + "/dashboard");

        } else {

            LoginMetrics.incrementFailedLogins();

            logger.warn(
                    "Failed login. username={}, ip={}",
                    username,
                    request.getRemoteAddr());

            request.setAttribute(
                    "error",
                    "Invalid Username or Password");

            request.getRequestDispatcher("/login.jsp")
                    .forward(request, response);
        }
    }
}
