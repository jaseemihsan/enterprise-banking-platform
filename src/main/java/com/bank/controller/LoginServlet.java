package com.bank.controller;

import java.io.IOException;

import com.bank.model.User;
import com.bank.service.LoginService;
import com.bank.service.AuditLogService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {


	private final LoginService loginService = new LoginService();
        private final AuditLogService auditLogService = new AuditLogService();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = loginService.authenticate(username, password);

        if (user != null) {

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

            response.sendRedirect(request.getContextPath() + "/dashboard");

        } else {

             request.setAttribute("error", "Invalid Username or Password");
             request.getRequestDispatcher("/login.jsp").forward(request, response);

        }

    }

}
