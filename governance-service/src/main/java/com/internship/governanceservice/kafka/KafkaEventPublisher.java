package com.internship.governanceservice.kafka;

import com.internship.governanceservice.config.KafkaTopics;
import com.internship.governanceservice.event.PolicyEvent;
import com.internship.governanceservice.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate <String, PolicyEvent> kafkaTemplate;

    @Override
    public void publish(PolicyEvent event) {
        kafkaTemplate.send(KafkaTopics.POLICY_EVENTS,event);


        System.out.println("=================================");
        System.out.println(event);
        System.out.println("=================================");

    }

}