package com.internship.auditservice.service.impl;


import com.internship.auditservice.entity.AuditLog;
import com.internship.auditservice.event.PolicyEvent;
import com.internship.auditservice.repository.AuditLogRepository;
import com.internship.auditservice.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository repository;

    @Override
    public void saveAudit(PolicyEvent event) {

        AuditLog log = AuditLog.builder()

                .policyId(event.getPolicyId())

                .eventType(event.getEventType().name())

                .actor(event.getActor())

                .timestamp(event.getTimestamp())

                .build();

        repository.save(log);

        System.out.println("Audit saved: " + log);

    }
}