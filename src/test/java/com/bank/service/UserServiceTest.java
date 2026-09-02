package com.bank.service;

import com.bank.dao.UserDAO;
import com.bank.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserDAO userDAO;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {

        userDAO = mock(UserDAO.class);

        userService =
                new UserService(userDAO);

        user = new User();
        user.setUsername("admin");
    }

    @Test
    void getAllUsers_shouldReturnUsers() {

        List<User> users =
                Arrays.asList(user);

        when(userDAO.getAllUsers())
                .thenReturn(users);

        List<User> result =
                userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("admin",
                result.get(0).getUsername());

        verify(userDAO)
                .getAllUsers();
    }

    @Test
    void getAllUsers_shouldReturnEmptyList() {

        when(userDAO.getAllUsers())
                .thenReturn(Collections.emptyList());

        List<User> result =
                userService.getAllUsers();

        assertTrue(result.isEmpty());

        verify(userDAO)
                .getAllUsers();
    }

    @Test
    void usernameExists_shouldReturnTrue() {

        when(userDAO.usernameExists("admin"))
                .thenReturn(true);

        boolean result =
                userService.usernameExists("admin");

        assertTrue(result);

        verify(userDAO)
                .usernameExists("admin");
    }

    @Test
    void usernameExists_shouldReturnFalse() {

        when(userDAO.usernameExists("newuser"))
                .thenReturn(false);

        boolean result =
                userService.usernameExists("newuser");

        assertFalse(result);

        verify(userDAO)
                .usernameExists("newuser");
    }

    @Test
    void addUser_shouldReturnFalse_whenUsernameAlreadyExists() {

        when(userDAO.usernameExists("admin"))
                .thenReturn(true);

        boolean result =
                userService.addUser(user);

        assertFalse(result);

        verify(userDAO)
                .usernameExists("admin");

        verify(userDAO, never())
                .addUser(any(User.class));
    }

    @Test
    void addUser_shouldCallDao_whenUsernameDoesNotExist() {

        when(userDAO.usernameExists("admin"))
                .thenReturn(false);

        when(userDAO.addUser(user))
                .thenReturn(true);

        boolean result =
                userService.addUser(user);

        assertTrue(result);

        verify(userDAO)
                .usernameExists("admin");

        verify(userDAO)
                .addUser(user);
    }

    @Test
    void addUser_shouldReturnFalse_whenDaoFails() {

        when(userDAO.usernameExists("admin"))
                .thenReturn(false);

        when(userDAO.addUser(user))
                .thenReturn(false);

        boolean result =
                userService.addUser(user);

        assertFalse(result);

        verify(userDAO)
                .usernameExists("admin");

        verify(userDAO)
                .addUser(user);
    }

    @Test
    void getUserById_shouldReturnUser() {

        when(userDAO.getUserById(1))
                .thenReturn(user);

        User result =
                userService.getUserById(1);

        assertNotNull(result);
        assertEquals("admin",
                result.getUsername());

        verify(userDAO)
                .getUserById(1);
    }

    @Test
    void getUserById_shouldReturnNull_whenNotFound() {

        when(userDAO.getUserById(999))
                .thenReturn(null);

        User result =
                userService.getUserById(999);

        assertNull(result);

        verify(userDAO)
                .getUserById(999);
    }

    @Test
    void updateUser_shouldReturnTrue_whenDaoSucceeds() {

        when(userDAO.updateUser(user))
                .thenReturn(true);

        boolean result =
                userService.updateUser(user);

        assertTrue(result);

        verify(userDAO)
                .updateUser(user);
    }

    @Test
    void updateUser_shouldReturnFalse_whenDaoFails() {

        when(userDAO.updateUser(user))
                .thenReturn(false);

        boolean result =
                userService.updateUser(user);

        assertFalse(result);

        verify(userDAO)
                .updateUser(user);
    }

    @Test
    void updateStatus_shouldReturnTrue_whenDaoSucceeds() {

        when(userDAO.updateStatus(1, "ACTIVE"))
                .thenReturn(true);

        boolean result =
                userService.updateStatus(1, "ACTIVE");

        assertTrue(result);

        verify(userDAO)
                .updateStatus(1, "ACTIVE");
    }

    @Test
    void updateStatus_shouldReturnFalse_whenDaoFails() {

        when(userDAO.updateStatus(1, "INACTIVE"))
                .thenReturn(false);

        boolean result =
                userService.updateStatus(1, "INACTIVE");

        assertFalse(result);

        verify(userDAO)
                .updateStatus(1, "INACTIVE");
    }
}
