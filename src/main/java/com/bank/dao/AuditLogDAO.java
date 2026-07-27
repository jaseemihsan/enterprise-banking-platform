package com.bank.dao;

import com.bank.config.DBConnection;
import com.bank.model.AuditLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuditLogDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(AuditLogDAO.class);

    private static final String INSERT_LOG = """
        INSERT INTO audit_logs
        (username, action, module_name, details, ip_address)
        VALUES (?, ?, ?, ?, ?)
        """;

    public void save(AuditLog log) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_LOG)) {

            stmt.setString(1, log.getUsername());
            stmt.setString(2, log.getAction());
            stmt.setString(3, log.getModuleName());
            stmt.setString(4, log.getDetails());
            stmt.setString(5, log.getIpAddress());

            stmt.executeUpdate();

            logger.info("Audit log saved: {} - {}",
                    log.getUsername(),
                    log.getAction());

        } catch (SQLException e) {

            logger.error("Error saving audit log", e);
        }
    }
}
