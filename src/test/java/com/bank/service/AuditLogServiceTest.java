package com.bank.service;

import com.bank.dao.AuditLogDAO;
import com.bank.model.AuditLog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuditLogServiceTest {

    private AuditLogDAO auditLogDAO;
    private AuditLogService auditLogService;

    @BeforeEach
    void setUp() {

        auditLogDAO = mock(AuditLogDAO.class);

        auditLogService =
                new AuditLogService(auditLogDAO);
    }

    @Test
    void log_shouldCreateAndSaveAuditLog() {

        auditLogService.log(
                "admin",
                "LOGIN",
                "Authentication",
                "User logged in",
                "192.168.1.10"
        );

        var captor =
                org.mockito.ArgumentCaptor
                        .forClass(AuditLog.class);

        verify(auditLogDAO)
                .save(captor.capture());

        AuditLog savedLog =
                captor.getValue();

        assertEquals(
                "admin",
                savedLog.getUsername());

        assertEquals(
                "LOGIN",
                savedLog.getAction());

        assertEquals(
                "Authentication",
                savedLog.getModuleName());

        assertEquals(
                "User logged in",
                savedLog.getDetails());

        assertEquals(
                "192.168.1.10",
                savedLog.getIpAddress());
    }
}
