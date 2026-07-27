package com.bank.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {

 private static final Logger logger =
            LoggerFactory.getLogger(DBConnection.class);


    private static final HikariDataSource dataSource;

    static {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://192.168.30.156:3306/bankdb");
        config.setUsername("bankuser");
        config.setPassword("Bank@123");

        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // Pool Configuration
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(300000);
        config.setConnectionTimeout(30000);
        config.setMaxLifetime(1800000);

        // Optional: Name the pool
        config.setPoolName("BankingPool");

        dataSource = new HikariDataSource(config);

	logger.info("HikariCP Connection Pool Initialized");
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
