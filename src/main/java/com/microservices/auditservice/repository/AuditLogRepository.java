package com.microservices.auditservice.repository;

import com.microservices.auditservice.entity.AuditLog;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuditLogRepository extends MongoRepository<AuditLog, ObjectId> {
    Optional<AuditLog> findById(ObjectId id);

    Optional<AuditLog> findByRequestId(String requestId);

    List<AuditLog> findAllByUserId(String userId);

    Page<AuditLog> findByUserId(String userId, Pageable pageable);

    List<AuditLog> findByAuditTimestampBefore(Instant auditTimestamp);
    void deleteByAuditTimestampBefore(Instant auditTimestamp);
}