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

    public User getUserById(int id) {
    return userDAO.getUserById(id);
}

   public boolean updateUser(User user) {
    return userDAO.updateUser(user);
}
}
