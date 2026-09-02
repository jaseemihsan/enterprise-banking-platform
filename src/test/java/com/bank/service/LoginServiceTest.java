package com.bank.service;

import com.bank.dao.UserDAO;
import com.bank.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    private UserDAO userDAO;
    private LoginService loginService;
    private User user;

    @BeforeEach
    void setUp() {

        userDAO = mock(UserDAO.class);

        loginService =
                new LoginService(userDAO);

        user = new User();

        user.setUsername("admin");
        user.setPassword("admin123");
        user.setStatus("ACTIVE");
    }

    @Test
    void authenticate_shouldReturnNull_whenUserDoesNotExist() {

        when(userDAO.findUserByUsername("admin"))
                .thenReturn(null);

        User result =
                loginService.authenticate(
                        "admin",
                        "admin123");

        assertNull(result);

        verify(userDAO)
                .findUserByUsername("admin");
    }

    @Test
    void authenticate_shouldReturnNull_whenUserIsInactive() {

        user.setStatus("INACTIVE");

        when(userDAO.findUserByUsername("admin"))
                .thenReturn(user);

        User result =
                loginService.authenticate(
                        "admin",
                        "admin123");

        assertNull(result);

        verify(userDAO)
                .findUserByUsername("admin");
    }

    @Test
    void authenticate_shouldReturnUser_whenPasswordIsCorrect() {

        when(userDAO.findUserByUsername("admin"))
                .thenReturn(user);

        User result =
                loginService.authenticate(
                        "admin",
                        "admin123");

        assertNotNull(result);
        assertEquals("admin",
                result.getUsername());

        verify(userDAO)
                .findUserByUsername("admin");
    }

    @Test
    void authenticate_shouldReturnNull_whenPasswordIsWrong() {

        when(userDAO.findUserByUsername("admin"))
                .thenReturn(user);

        User result =
                loginService.authenticate(
                        "admin",
                        "wrongPassword");

        assertNull(result);

        verify(userDAO)
                .findUserByUsername("admin");
    }

    @Test
    void authenticate_shouldAcceptLowercaseActiveStatus() {

        user.setStatus("active");

        when(userDAO.findUserByUsername("admin"))
                .thenReturn(user);

        User result =
                loginService.authenticate(
                        "admin",
                        "admin123");

        assertNotNull(result);
        assertEquals("admin",
                result.getUsername());

        verify(userDAO)
                .findUserByUsername("admin");
    }
}
