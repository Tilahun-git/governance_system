package com.internship.auditservice.repository;


import com.internship.auditservice.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByPolicyIdOrderByTimestampAsc(Long policyId);



}