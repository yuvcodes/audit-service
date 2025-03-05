package com.microservices.auditservice.service;

import com.microservices.auditservice.entity.AuditLog;
import com.microservices.auditservice.model.enums.RoleEnum;
import com.microservices.auditservice.repository.AuditLogRepository;
import com.microservices.auditservice.visitors.auditLogs.AdminRoleVisitor;
import com.microservices.auditservice.visitors.auditLogs.UserBasicRoleVisitor;
import com.microservices.auditservice.visitors.auditLogs.UserRoleVisitor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    @Autowired
    AuditLogRepository auditLogRepository;

    public Page<AuditLog> getAuditLogs(String userId, RoleEnum role, Pageable pageable) {
        UserRoleVisitor visitor = switch (role) {
            case ADMIN, SUPER_ADMIN -> new AdminRoleVisitor();
            default -> new UserBasicRoleVisitor();
        };

        return visitor.visit(auditLogRepository, userId, pageable);
    }
}

