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
    "/users/add",
    "/users/edit",
    "/users/update"
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

	case "/users/edit":

    int id = Integer.parseInt(request.getParameter("id"));

    User user = userService.getUserById(id);

    request.setAttribute("user", user);

    request.getRequestDispatcher("/edit-user.jsp")
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

            request.setAttribute("error", "Username already exists.");

            request.getRequestDispatcher("/add-user.jsp")
                    .forward(request, response);
        }

    } else if ("/users/update".equals(path)) {

        User user = new User();

        user.setId(Integer.parseInt(request.getParameter("id")));
        user.setUsername(request.getParameter("username"));
        user.setRoleId(Integer.parseInt(request.getParameter("roleId")));
        user.setStatus(request.getParameter("status"));

        boolean updated = userService.updateUser(user);

        if (updated) {

            response.sendRedirect(request.getContextPath() + "/users");

        } else {

            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
}
