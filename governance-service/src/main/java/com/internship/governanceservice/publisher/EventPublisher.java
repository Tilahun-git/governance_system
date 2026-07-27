package com.internship.governanceservice.publisher;

import com.internship.governanceservice.event.PolicyEvent;

public interface EventPublisher {

    void publish(PolicyEvent event);

}