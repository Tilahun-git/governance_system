package com.internship.auditservice.controller;

import com.internship.auditservice.entity.AuditLog;
import com.internship.auditservice.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/policies/{policyId}")
    public ResponseEntity<List<AuditLog>> getPolicyHistory(
            @PathVariable Long policyId) {

        return ResponseEntity.ok(auditLogService.getPolicyHistory(policyId));
    }
}