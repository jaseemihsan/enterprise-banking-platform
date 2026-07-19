package com.bank.controller;

import com.bank.model.User;
import com.bank.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet({
        "/users",
        "/users/add"
})
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserService();

     @Override
protected void doGet(HttpServletRequest request,
                     HttpServletResponse response)
        throws ServletException, IOException {

    String path = request.getServletPath();

    switch (path) {

        case "/users":

            List<User> users = userService.getAllUsers();

            request.setAttribute("users", users);

            request.getRequestDispatcher("/users.jsp")
                    .forward(request, response);

            break;

        case "/users/add":

            request.getRequestDispatcher("/add-user.jsp")
                    .forward(request, response);

            break;

        default:

            response.sendError(HttpServletResponse.SC_NOT_FOUND);

    }
  }

       @Override
protected void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException {

    String path = request.getServletPath();

    if ("/users/add".equals(path)) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        int roleId = Integer.parseInt(request.getParameter("roleId"));
        String status = request.getParameter("status");

        User user = new User();

        user.setUsername(username);
        user.setPassword(password);
        user.setRoleId(roleId);
        user.setStatus(status);

        boolean saved = userService.addUser(user);

        if (saved) {

            response.sendRedirect(request.getContextPath() + "/users");

        } else {

            request.setAttribute("error",
                    "Username already exists.");

            request.getRequestDispatcher("/add-user.jsp")
                    .forward(request, response);
        }
    }
}
}
