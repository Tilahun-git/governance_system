package com.internship.governanceservice.kafka;

import com.internship.governanceservice.config.KafkaTopics;
import com.internship.governanceservice.event.PolicyEvent;
import com.internship.governanceservice.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate <String, PolicyEvent> kafkaTemplate;
    private static final Logger logger =
            LoggerFactory.getLogger(KafkaEventPublisher.class);

    @Override
    public void publish(PolicyEvent event) {

        kafkaTemplate.send(KafkaTopics.POLICY_EVENTS,event);

        System.out.println("PUBLISHED POLICY EVENT IS : " + event.toString());

        logger.info("Publishing event to Kafka topic '{}': {}", KafkaTopics.POLICY_EVENTS, event);


    }

}