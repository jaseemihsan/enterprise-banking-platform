package com.bank.service;

public class LoginService {

    public boolean authenticate(String username, String password) {

        return "admin".equals(username)
                && "admin123".equals(password);

    }

}
