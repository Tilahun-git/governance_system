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
    private static final Logger logger = LoggerFactory.getLogger(KafkaEventPublisher.class);

    @Override
    public void publish(PolicyEvent event) {

        kafkaTemplate.send(KafkaTopics.POLICY_EVENTS_TOPIC,event).whenComplete((result,exception)->{
            if (exception == null) {

                logger.info(
                        "Event successfully published to topic '{}', partition={}, offset={}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset()
                );

            } else {

                logger.error(
                        "Failed to publish event to Kafka topic '{}'",
                        KafkaTopics.POLICY_EVENTS_TOPIC,
                        exception
                );
            }
        });


    }

}