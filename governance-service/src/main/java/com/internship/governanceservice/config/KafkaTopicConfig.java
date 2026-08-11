package com.internship.governanceservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic policyEventsTopic() {
        return new NewTopic(
                KafkaTopics.POLICY_EVENTS_TOPIC,
                1,
                (short) 1
        );
    }
}