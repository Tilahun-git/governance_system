package com.internship.auditservice.service;

import com.internship.auditservice.entity.AuditLog;
import com.internship.auditservice.enums.EventType;
import com.internship.auditservice.event.PolicyEvent;
import com.internship.auditservice.repository.AuditLogRepository;
import com.internship.auditservice.service.impl.AuditLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuditLogServiceTest {

    private AuditLogRepository auditLogRepository;
    private AuditLogService auditLogService;

    @BeforeEach
    void setUp() {

        auditLogRepository = mock(AuditLogRepository.class);

        auditLogService = new AuditLogServiceImpl(
                auditLogRepository
        );
    }

    @Test
    void saveAudit_ShouldConvertEventToAuditLogAndSaveIt() {

        LocalDateTime timestamp = LocalDateTime.now();

        PolicyEvent event = PolicyEvent.builder()
                .policyId(1L)
                .eventType(EventType.POLICY_CREATED)
                .actor("admin")
                .timestamp(timestamp)
                .build();

        auditLogService.saveAudit(event);

        ArgumentCaptor<AuditLog> auditLogCaptor =
                ArgumentCaptor.forClass(AuditLog.class);

        verify(auditLogRepository)
                .save(auditLogCaptor.capture());

        AuditLog savedAuditLog =
                auditLogCaptor.getValue();

        assertThat(savedAuditLog.getPolicyId())
                .isEqualTo(1L);

        assertThat(savedAuditLog.getEventType())
                .isEqualTo("POLICY_CREATED");

        assertThat(savedAuditLog.getActor())
                .isEqualTo("admin");

        assertThat(savedAuditLog.getTimestamp())
                .isEqualTo(timestamp);
    }

    @Test
    void getPolicyHistory_ShouldReturnAuditLogs() {

        AuditLog auditLog1 = AuditLog.builder()
                .policyId(1L)
                .eventType("POLICY_CREATED")
                .actor("admin")
                .build();

        AuditLog auditLog2 = AuditLog.builder()
                .policyId(1L)
                .eventType("POLICY_SUBMITTED")
                .actor("admin")
                .build();

        List<AuditLog> auditLogs =
                List.of(auditLog1, auditLog2);

        when(auditLogRepository
                .findByPolicyIdOrderByTimestampAsc(1L))
                .thenReturn(auditLogs);

        List<AuditLog> result =
                auditLogService.getPolicyHistory(1L);

        assertThat(result)
                .hasSize(2);

        assertThat(result)
                .isEqualTo(auditLogs);

        verify(auditLogRepository)
                .findByPolicyIdOrderByTimestampAsc(1L);
    }
}