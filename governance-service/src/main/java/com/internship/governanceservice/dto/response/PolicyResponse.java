package com.internship.governanceservice.dto.response;

import com.internship.governanceservice.enums.PolicyStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PolicyResponse {

    private Long id;

    private String title;

    private String description;

    private PolicyStatus status;

    private String createdBy;

    private LocalDateTime createdAt;
}