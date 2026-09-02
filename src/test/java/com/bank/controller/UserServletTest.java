package com.bank.controller;

import com.bank.model.User;
import com.bank.service.UserService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServletTest {

    private UserService userService;
    private UserServlet servlet;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {

        userService = mock(UserService.class);

        servlet = new UserServlet(userService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getContextPath())
                .thenReturn("");

    }

    @Test
    void users_shouldForwardToUsersPage() throws Exception {

        when(request.getServletPath())
                .thenReturn("/users");

        when(request.getRequestDispatcher("/users.jsp"))
                .thenReturn(dispatcher);

        when(userService.getAllUsers())
                .thenReturn(java.util.Collections.emptyList());

        servlet.doGet(request, response);

        verify(userService)
                .getAllUsers();

        verify(request)
                .setAttribute(eq("users"), any());

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void addUserPage_shouldForward() throws Exception {

        when(request.getServletPath())
                .thenReturn("/users/add");

        when(request.getRequestDispatcher("/add-user.jsp"))
                .thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void editUser_shouldForward() throws Exception {

        User user = new User();

        user.setId(1);
        user.setUsername("testuser");

        when(request.getServletPath())
                .thenReturn("/users/edit");

        when(request.getParameter("id"))
                .thenReturn("1");

        when(userService.getUserById(1))
                .thenReturn(user);

        when(request.getRequestDispatcher("/edit-user.jsp"))
                .thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(userService)
                .getUserById(1);

        verify(request)
                .setAttribute("user", user);

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void status_shouldUpdateAndRedirect() throws Exception {

        when(request.getServletPath())
                .thenReturn("/users/status");

        when(request.getParameter("id"))
                .thenReturn("1");

        when(request.getParameter("status"))
                .thenReturn("BLOCKED");

        servlet.doGet(request, response);

        verify(userService)
                .updateStatus(1, "BLOCKED");

        verify(response)
                .sendRedirect("/users");
    }

    @Test
    void reset_shouldForwardToResetPasswordPage()
            throws Exception {

        User user = new User();

        user.setId(1);
        user.setUsername("testuser");

        when(request.getServletPath())
                .thenReturn("/users/reset");

        when(request.getParameter("id"))
                .thenReturn("1");

        when(userService.getUserById(1))
                .thenReturn(user);

        when(request.getRequestDispatcher("/reset-password.jsp"))
                .thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(userService)
                .getUserById(1);

        verify(request)
                .setAttribute("user", user);

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void unknownGetPath_shouldReturn404() throws Exception {

        when(request.getServletPath())
                .thenReturn("/users/unknown");

        servlet.doGet(request, response);

        verify(response)
                .sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void addUser_shouldRedirectOnSuccess()
            throws Exception {

        when(request.getServletPath())
                .thenReturn("/users/add");

        when(request.getParameter("username"))
                .thenReturn("newuser");

        when(request.getParameter("password"))
                .thenReturn("password123");

        when(request.getParameter("roleId"))
                .thenReturn("1");

        when(request.getParameter("status"))
                .thenReturn("ACTIVE");

        when(userService.addUser(any(User.class)))
                .thenReturn(true);

        servlet.doPost(request, response);

        verify(userService)
                .addUser(any(User.class));

        verify(response)
                .sendRedirect("/users");
    }

    @Test
    void addUser_shouldForwardOnFailure()
            throws Exception {

        when(request.getServletPath())
                .thenReturn("/users/add");

        when(request.getParameter("username"))
                .thenReturn("existinguser");

        when(request.getParameter("password"))
                .thenReturn("password123");

        when(request.getParameter("roleId"))
                .thenReturn("1");

        when(request.getParameter("status"))
                .thenReturn("ACTIVE");

        when(userService.addUser(any(User.class)))
                .thenReturn(false);

        when(request.getRequestDispatcher("/add-user.jsp"))
                .thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request)
                .setAttribute(
                        "error",
                        "Username already exists.");

        verify(dispatcher)
                .forward(request, response);

        verify(response, never())
                .sendRedirect(anyString());
    }

    @Test
    void updateUser_shouldRedirectOnSuccess()
            throws Exception {

        when(request.getServletPath())
                .thenReturn("/users/update");

        when(request.getParameter("id"))
                .thenReturn("1");

        when(request.getParameter("username"))
                .thenReturn("updateduser");

        when(request.getParameter("roleId"))
                .thenReturn("2");

        when(request.getParameter("status"))
                .thenReturn("ACTIVE");

        when(userService.updateUser(any(User.class)))
                .thenReturn(true);

        servlet.doPost(request, response);

        verify(userService)
                .updateUser(any(User.class));

        verify(response)
                .sendRedirect("/users");
    }

    @Test
    void updateUser_shouldReturn500OnFailure()
            throws Exception {

        when(request.getServletPath())
                .thenReturn("/users/update");

        when(request.getParameter("id"))
                .thenReturn("1");

        when(request.getParameter("username"))
                .thenReturn("updateduser");

        when(request.getParameter("roleId"))
                .thenReturn("2");

        when(request.getParameter("status"))
                .thenReturn("ACTIVE");

        when(userService.updateUser(any(User.class)))
                .thenReturn(false);

        servlet.doPost(request, response);

        verify(userService)
                .updateUser(any(User.class));

        verify(response)
                .sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
