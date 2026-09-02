package com.bank.config;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionProvider implements ConnectionProvider {

    @Override
    public Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }
}
