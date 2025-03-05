package com.microservices.auditservice.entity;

import com.microservices.auditservice.util.DigitalSignatureUtil;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Map;

@Document(collection = "audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    @Id
    private ObjectId id;
    private String serviceName;
    private String eventType;
    private Instant auditTimestamp;
    private String userId;
    private String userIp;
    private String userRole;
    private String sourceService;
    private String requestId;
    private Map<String, Map<String, Object>> changes;
    private Map<String, String> metadata;
    private Instant createdTimestamp;
    private Instant updatedTimestamp;
    private String signature;

    public void sign(PrivateKey privateKey) throws Exception {
        String data = serviceName + eventType + auditTimestamp + userId + userIp + changes.toString();
        this.signature = DigitalSignatureUtil.signData(data, privateKey);
    }

    public boolean isValid(PublicKey publicKey) throws Exception {
        String data = serviceName + eventType + auditTimestamp + userId + userIp + changes.toString();
        return DigitalSignatureUtil.verifySignature(data, this.signature, publicKey);
    }
}