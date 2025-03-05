package com.microservices.auditservice.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.auditservice.entity.AuditLog;
import com.microservices.auditservice.repository.AuditLogRepository;
import com.microservices.auditservice.util.JsonMapperUtil;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@KafkaListener(id = "temp-id",topics = "#{'${spring.kafka.incoming.topic}'.split(',')}")
public class AuditConsumer {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private JsonMapperUtil jsonMapperUtil;

    @KafkaHandler
    public void consumeAuditLog(String auditLog) {
        System.out.println("Received Audit Log: " + auditLog);

        try {
           AuditLog log = jsonMapperUtil.fromJson(auditLog, AuditLog.class);
           auditLogRepository.save(log);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}

