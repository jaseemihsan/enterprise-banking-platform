package com.bank.service;

import com.bank.dao.UserDAO;
import com.bank.model.User;

import java.util.List;

public class UserService {

    private final UserDAO userDAO;

    /*
     * Default constructor used by the application.
     */
    public UserService() {
        this.userDAO = new UserDAO();
    }

    /*
     * Constructor used for unit testing.
     */
    UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

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

    public boolean updateStatus(int id, String status) {
        return userDAO.updateStatus(id, status);
    }
}
