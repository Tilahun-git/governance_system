package com.internship.governanceservice.kafka;

import com.internship.governanceservice.event.PolicyEvent;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventPublisher implements EventPublisher {

    @Override
    public void publish(PolicyEvent event) {

        System.out.println("=================================");
        System.out.println("EVENT PUBLISHED");
        System.out.println(event);
        System.out.println("=================================");

    }

}