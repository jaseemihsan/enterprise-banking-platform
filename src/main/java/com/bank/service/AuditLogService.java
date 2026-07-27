package com.bank.service;

import com.bank.dao.AuditLogDAO;
import com.bank.model.AuditLog;

public class AuditLogService {

    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    public void log(String username,
                    String action,
                    String module,
                    String details,
                    String ipAddress) {

        AuditLog auditLog = new AuditLog();

        auditLog.setUsername(username);
        auditLog.setAction(action);
        auditLog.setModuleName(module);
        auditLog.setDetails(details);
        auditLog.setIpAddress(ipAddress);

        auditLogDAO.save(auditLog);
    }
}
