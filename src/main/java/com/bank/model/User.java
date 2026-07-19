package com.bank.model;

import java.sql.Timestamp;

public class User {

    private int id;
    private String username;
    private String password;
    private int roleId;
    private String roleName;
    private String status;
    private Timestamp createdAt;

    // Default Constructor
    public User() {
    }

    // Parameterized Constructor
    public User(int id, String username, String password, String roleName, String status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roleName = roleName;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
    return roleId;
}

public void setRoleId(int roleId) {
    this.roleId = roleId;
}

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
    return createdAt;
}

public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
}

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
		", roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
