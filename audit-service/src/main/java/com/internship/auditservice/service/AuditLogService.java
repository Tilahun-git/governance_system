package com.internship.auditservice.service;


import com.internship.auditservice.event.PolicyEvent;


public interface AuditLogService {


    void saveAudit(PolicyEvent event);


}