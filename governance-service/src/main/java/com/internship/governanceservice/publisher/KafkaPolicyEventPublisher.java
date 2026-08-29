package com.internship.governanceservice.publisher;

import com.internship.governanceservice.event.PolicyEvent;

public interface KafkaPolicyEventPublisher {

    void publish(PolicyEvent event);

}