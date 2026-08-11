package com.internship.auditservice.event;


import com.internship.auditservice.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyEvent {

    private EventType eventType;
    private Long policyId;
    private String actor;
    private LocalDateTime timestamp;

}