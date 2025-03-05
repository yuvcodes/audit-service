package com.microservices.auditservice.service.logRotation;

import com.microservices.auditservice.entity.AuditLog;
import com.microservices.auditservice.repository.AuditLogRepository;
import com.microservices.auditservice.util.JsonMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class MongoLogRotationService {
    @Autowired AuditLogRepository auditLogRepository;
    @Autowired
    JsonMapperUtil jsonMapperUtil;

    @Value("${mongodb.log-rotation.days:7}")
    private int retentionDays;

    @Value("${mongodb.log-rotation.archive-path:./logs}")
    private String archivePath;

    @Scheduled(cron = "${mongodb.log-rotation.cron:-0 0 2 * * *}") // Run daily at 2 AM
    public void archiveAndDeleteOldLogs() {
        Instant cutoffTime = Instant.now().minus(retentionDays, ChronoUnit.DAYS);
        List<AuditLog> oldLogs = auditLogRepository.findByAuditTimestampBefore(cutoffTime);

        if (oldLogs.isEmpty()) {
            System.out.println("No logs to archive.");
            return;
        }

        try {
            File archiveDir = new File(archivePath);
            if (!archiveDir.exists()) archiveDir.mkdirs();

            String filename = archivePath + "/audit_logs_" + Instant.now().toEpochMilli() + ".json";
            jsonMapperUtil.writeValue(filename, oldLogs);

            System.out.println("Logs archived to: " + filename);

            auditLogRepository.deleteByAuditTimestampBefore(cutoffTime);
            System.out.println("Old logs deleted after archiving.");
        } catch (IOException e) {
            System.err.println("Error writing logs to file: " + e.getMessage());
        }
    }
}
