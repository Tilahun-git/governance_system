package com.internship.auditservice.kafka;


import com.internship.auditservice.event.PolicyEvent;
import com.internship.auditservice.service.AuditLogService;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class PolicyEventConsumerKafka {


    private final AuditLogService auditLogService;



    @KafkaListener(topics = "policy-events", groupId = "audit-service")
    public void consume(PolicyEvent event){

        System.out.println("Received Kafka Event: " + event);

        auditLogService.saveAudit(event);

    }

}