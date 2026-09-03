package com.internship.governanceservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.internship.governanceservice.enums.PolicyStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyResponse {

    private Long id;

    private String title;

    private String description;

    private PolicyStatus status;

    private String createdBy;

    @JsonFormat(pattern = "dd MMMM yyyy, hh:mm a")
    private LocalDateTime createdAt;
}