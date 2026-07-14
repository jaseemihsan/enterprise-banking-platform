package com.bank.service;

import com.bank.dao.UserDAO;
import com.bank.model.User;

public class LoginService {

    private final UserDAO userDAO = new UserDAO();

    public User authenticate(String username, String password) {

        User user = userDAO.findUserByUsername(username);

        if (user == null) {
            System.out.println("User not found");
            return null;
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            System.out.println("User inactive");
            return null;
        }

        if (user.getPassword().equals(password)) {
            System.out.println("Login Success : " + username);
            return user;
        }

        System.out.println("Wrong Password");
        return null;
    }
}
