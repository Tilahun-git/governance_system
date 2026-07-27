package com.internship.governanceservice.config;

import com.internship.governanceservice.event.PolicyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaTemplate<String, PolicyEvent> kafkaTemplate(ProducerFactory<String, PolicyEvent> producerFactory) {

        return new KafkaTemplate<>(producerFactory);

    }

}