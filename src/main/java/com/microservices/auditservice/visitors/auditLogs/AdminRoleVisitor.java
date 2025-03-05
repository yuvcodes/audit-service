package com.microservices.auditservice.visitors.auditLogs;

import com.microservices.auditservice.entity.AuditLog;
import com.microservices.auditservice.repository.AuditLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class AdminRoleVisitor implements UserRoleVisitor {
    @Override
    public Page<AuditLog> visit(AuditLogRepository repository, String userId, Pageable pageable) {
        return repository.findAll(pageable);
    }
}

