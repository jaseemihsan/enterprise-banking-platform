package com.bank.service;

import com.bank.dao.UserDAO;
import com.bank.model.User;

import java.util.List;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public boolean usernameExists(String username) {
        return userDAO.usernameExists(username);
    }

    public boolean addUser(User user) {

        if (usernameExists(user.getUsername())) {
            return false;
        }

        return userDAO.addUser(user);
    }
}
