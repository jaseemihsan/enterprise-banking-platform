package com.bank.dao;

import com.bank.config.DBConnection;
import com.bank.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public User findUserByUsername(String username) {

        User user = null;

        String sql = """
                SELECT u.id,
                       u.username,
                       u.password,
                       r.role_name,
                       u.status
                FROM users u
                JOIN roles r
                    ON u.role_id = r.id
                WHERE u.username = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, username);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {

                user = new User();

                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role_name"));
                user.setStatus(rs.getString("status"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
}
