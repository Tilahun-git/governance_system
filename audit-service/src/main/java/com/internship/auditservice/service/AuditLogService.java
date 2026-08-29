package com.internship.auditservice.service;


import com.internship.auditservice.entity.AuditLog;
import com.internship.auditservice.event.PolicyEvent;

import java.util.List;


public interface AuditLogService {


    void saveAudit(PolicyEvent event);
    public List<AuditLog> getPolicyHistory(Long policyId);




}