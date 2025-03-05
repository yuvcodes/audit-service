package com.microservices.auditservice.controller;

import com.microservices.auditservice.entity.AuditLog;
import com.microservices.auditservice.kafka.KafkaProducer;
import com.microservices.auditservice.model.enums.RoleEnum;
import com.microservices.auditservice.service.AuditLogService;
import com.microservices.auditservice.service.AuditService;
import com.microservices.auditservice.service.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/audit")
public class AuditController {

    @Autowired
    private AuditLogService auditService;

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/logs")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<AuditLog>> getAuditLogs(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(auditService.getAuditLogs(authenticationService.getUserName(), authenticationService.getRole(), PageRequest.of(page, size)));
    }
}