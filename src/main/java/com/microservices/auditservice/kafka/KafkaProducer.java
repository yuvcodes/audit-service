package com.microservices.auditservice.kafka;

import com.microservices.auditservice.entity.AuditLog;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, AuditLog> kafkaTemplate;

    public void sendAuditLog(AuditLog log) {
        kafkaTemplate.send("audit_logs", log);
    }
}

