package com.microservices.auditservice.visitors.auditLogs;

import com.microservices.auditservice.entity.AuditLog;
import com.microservices.auditservice.repository.AuditLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRoleVisitor {
    Page<AuditLog> visit(AuditLogRepository repository, String userId, Pageable pageable);
}

