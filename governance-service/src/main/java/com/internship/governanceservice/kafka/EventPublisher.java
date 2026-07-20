package com.internship.governanceservice.kafka;

import com.internship.governanceservice.event.PolicyEvent;

public interface EventPublisher {

    void publish(PolicyEvent event);

}