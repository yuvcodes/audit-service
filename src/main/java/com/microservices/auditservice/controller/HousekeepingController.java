package com.microservices.auditservice.controller;

import com.microservices.auditservice.entity.AuditLog;
import com.microservices.auditservice.kafka.KafkaProducer;
import com.microservices.auditservice.util.RSAKeyGenerator;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.time.Instant;

@RestController
@RequestMapping("/housekeeping")
public class HousekeepingController {

    @Autowired
    private KafkaProducer kafkaProducer;

    /**
     * Only for testing integration through pushing events to kafka
     */
    @PostMapping("/logs")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AuditLog> pushAuditLogs(@RequestBody AuditLog log) {
        log.setId(ObjectId.get());
        log.setAuditTimestamp(Instant.now());
        log.setCreatedTimestamp(Instant.now());
        log.setUpdatedTimestamp(Instant.now());

        try {
            KeyPair keyPair = RSAKeyGenerator.generateKeyPair();
            log.sign(keyPair.getPrivate());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        kafkaProducer.sendAuditLog(log);
        return ResponseEntity.ok(log);
    }
}
