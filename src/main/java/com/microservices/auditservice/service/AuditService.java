package com.microservices.auditservice.service;

import com.microservices.auditservice.entity.AuditLog;
import com.microservices.auditservice.repository.AuditLogRepository;
import com.microservices.auditservice.util.RSAKeyGenerator;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;

@Service
public class AuditService {
    private final AuditLogRepository auditRepository;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public AuditService(AuditLogRepository auditLogRepository) throws Exception {
        auditRepository = auditLogRepository;
        KeyPair keyPair = RSAKeyGenerator.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    public void createAuditLog(AuditLog log) throws Exception {
        log.setCreatedTimestamp(Instant.now());
        log.setUpdatedTimestamp(Instant.now());
        log.sign(privateKey);  // Sign the log
        auditRepository.save(log);
    }

    public AuditLog getAuditLog(ObjectId id) throws Exception {
        AuditLog log = auditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found"));

        if (!log.isValid(publicKey)) {
            throw new SecurityException("Audit log is tampered!");
        }
        return log;
    }
}
