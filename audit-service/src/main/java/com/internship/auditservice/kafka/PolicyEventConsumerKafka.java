package com.internship.auditservice.kafka;

import com.internship.auditservice.config.KafkaTopics;
import com.internship.auditservice.event.PolicyEvent;
import com.internship.auditservice.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PolicyEventConsumerKafka {
    private final AuditLogService auditLogService;
    private static final Logger logger =
            LoggerFactory.getLogger(PolicyEventConsumerKafka.class);

    @KafkaListener(topics = KafkaTopics.POLICY_EVENTS_TOPIC)
    public void consume(PolicyEvent event){

        logger.info("Received policy event is : {}", event);
        logger.info("Processing policy event. policyId={}, eventType={}, performedBy={}",
                event.getPolicyId(),
                event.getEventType(),
                event.getActor());

        if (event.getPolicyId() == null) {
            logger.error("Received null PolicyEvent");
            return;
        }
        auditLogService.saveAudit(event);

        logger.info(
                "Policy event successfully saved as audit record log : eventId={}",
                event.getPolicyId()
        );



    }

}