package com.bank.dao;

import com.bank.config.DBConnection;
import com.bank.model.AuditLog;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class AuditLogDAOTest {

    private final AuditLogDAO auditLogDAO =
            new AuditLogDAO();

    @Test
    void save_shouldInsertAuditLog() throws Exception {

        AuditLog log = new AuditLog();

        log.setUsername("dao-test-user");
        log.setAction("TEST");
        log.setModuleName("AuditLogDAOTest");
        log.setDetails("DAO integration test");
        log.setIpAddress("127.0.0.1");

        auditLogDAO.save(log);

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement ps =
                     connection.prepareStatement(
                             """
                             SELECT COUNT(*)
                             FROM audit_logs
                             WHERE username=?
                               AND action=?
                               AND module_name=?
                             """)) {

            ps.setString(1, "dao-test-user");
            ps.setString(2, "TEST");
            ps.setString(3, "AuditLogDAOTest");

            try (ResultSet rs = ps.executeQuery()) {

                assertTrue(rs.next());
                assertTrue(rs.getInt(1) > 0);
            }
        }
    }
}
