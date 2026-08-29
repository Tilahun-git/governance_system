package com.internship.auditservice.service.impl;


import com.internship.auditservice.entity.AuditLog;
import com.internship.auditservice.event.PolicyEvent;
import com.internship.auditservice.repository.AuditLogRepository;
import com.internship.auditservice.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {
    private final AuditLogRepository auditLogRepository;

    @Override
    public void saveAudit(PolicyEvent event) {

        AuditLog audiLog = AuditLog.builder()
                .policyId(event.getPolicyId())
                .eventType(event.getEventType().name())
                .actor(event.getActor())
                .timestamp(event.getTimestamp())
                .build();

        auditLogRepository.save(audiLog);
        System.out.println("Audit saved: " + audiLog);
    }

    @Override
    public List<AuditLog> getPolicyHistory(Long policyId) {
            return auditLogRepository.findByPolicyIdOrderByTimestampAsc(policyId);
    }
}