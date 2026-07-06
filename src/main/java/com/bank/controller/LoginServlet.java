package com.bank.controller;

import java.io.IOException;

import com.bank.service.LoginService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        LoginService loginService = new LoginService();

        if (loginService.authenticate(username, password)) {

        response.sendRedirect("dashboard.jsp");

        } else {

        response.getWriter().println("Invalid Username or Password");

        }  

    }

}
