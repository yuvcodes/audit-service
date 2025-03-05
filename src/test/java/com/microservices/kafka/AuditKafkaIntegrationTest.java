package com.microservices.kafka;

import com.microservices.auditservice.AuditServiceApplication;
import com.microservices.auditservice.entity.AuditLog;
import com.microservices.auditservice.kafka.KafkaProducer;
import com.microservices.auditservice.repository.AuditLogRepository;
import com.microservices.auditservice.util.RSAKeyGenerator;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

import java.security.KeyPair;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
@ContextConfiguration(classes = AuditServiceApplication.class)
@DirtiesContext
@EmbeddedKafka(partitions = 3, topics = {"audit_logs"})
public class AuditKafkaIntegrationTest {

    @Autowired
    private KafkaProducer auditProducer;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Test
    public void testAuditLogKafkaToMongoDB() throws InterruptedException {
        // Create a test audit log
        AuditLog log = AuditLog.builder()
                .serviceName("OrderService")
                .eventType("CREATE")
                .auditTimestamp(Instant.now())
                .userId("user-123")
                .userIp("192.168.1.1")
                .userRole("USER")
                .sourceService("OrderService")
                .requestId(UUID.randomUUID().toString())
                .changes(new HashMap<>(Map.of("status", Map.of("old", "null", "new", "Created"))))
                .metadata(new HashMap<>(Map.of("traceId", "abcd-1234-xyz")))
                .createdTimestamp(Instant.now())
                .updatedTimestamp(Instant.now())
                .build();



        try {
            KeyPair keyPair = RSAKeyGenerator.generateKeyPair();
            log.sign(keyPair.getPrivate());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        // Publish to Kafka
        auditProducer.sendAuditLog(log);

        // Wait for Kafka to process
        Thread.sleep(5000);

        // Check if audit log is in MongoDB
        AuditLog storedLog = auditLogRepository.findByRequestId(log.getRequestId()).orElse(null);
        assertNotNull(storedLog, "Audit Log was not stored in MongoDB!");
        assertEquals("OrderService", storedLog.getServiceName());
    }
}
