package com.internship.governanceservice.event;

import com.internship.governanceservice.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyEvent {

    private EventType eventType;

    private Long policyId;

    private String actor;

    private LocalDateTime timestamp;

}