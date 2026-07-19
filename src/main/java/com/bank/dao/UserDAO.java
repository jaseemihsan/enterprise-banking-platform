package com.bank.dao;

import com.bank.config.DBConnection;
import com.bank.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
                user.setRoleName(rs.getString("role_name"));
                user.setStatus(rs.getString("status"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    
     public List<User> getAllUsers() {

    List<User> users = new ArrayList<>();

    String sql = """
            SELECT
                u.id,
                u.username,
                u.role_id,
                r.role_name,
                u.status,
		u.created_at
            FROM users u
            JOIN roles r
                ON u.role_id = r.id
            ORDER BY u.id
            """;

    try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
    ) {

        while (rs.next()) {

            User user = new User();

            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setRoleId(rs.getInt("role_id"));
            user.setRoleName(rs.getString("role_name"));
            user.setStatus(rs.getString("status"));
	    user.setCreatedAt(rs.getTimestamp("created_at"));

            users.add(user);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return users;
   }

   
    public boolean addUser(User user) {

    String sql = """
            INSERT INTO users
            (username, password, role_id, status)
            VALUES (?, ?, ?, ?)
            """;

    try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
    ) {

        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setInt(3, user.getRoleId());
        statement.setString(4, user.getStatus());

        return statement.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
   }
 

   public boolean usernameExists(String username) {

    String sql = """
            SELECT COUNT(*)
            FROM users
            WHERE username = ?
            """;

    try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
    ) {

        statement.setString(1, username);

        ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            return rs.getInt(1) > 0;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
  }

   
    public User getUserById(int id) {

    User user = null;

    String sql = """
            SELECT
                u.id,
                u.username,
                u.role_id,
                r.role_name,
                u.status
            FROM users u
            JOIN roles r
                ON u.role_id = r.id
            WHERE u.id = ?
            """;

    try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
    ) {

        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();

        if (rs.next()) {

            user = new User();

            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setRoleId(rs.getInt("role_id"));
            user.setRoleName(rs.getString("role_name"));
            user.setStatus(rs.getString("status"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return user;
}

    public boolean updateUser(User user) {

    String sql = """
            UPDATE users
            SET username = ?,
                role_id = ?,
                status = ?
            WHERE id = ?
            """;

    try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
    ) {

        statement.setString(1, user.getUsername());
        statement.setInt(2, user.getRoleId());
        statement.setString(3, user.getStatus());
        statement.setInt(4, user.getId());

        return statement.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
}

}
