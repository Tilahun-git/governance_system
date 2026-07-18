package com.internship.governanceservice.mapper;

import com.internship.governanceservice.dto.request.CreatePolicyRequest;
import com.internship.governanceservice.dto.response.PolicyResponse;
import com.internship.governanceservice.entity.Policy;
import com.internship.governanceservice.enums.PolicyStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class PolicyMapper {
    public Policy mapToPolicyEntity(CreatePolicyRequest createPolicyDto) {
        return Policy.builder()
                .title(createPolicyDto.getTitle())
                .description(createPolicyDto.getDescription())
                .createdBy(createPolicyDto.getCreatedBy())
                .createdAt(LocalDateTime.now())
                .status(PolicyStatus.DRAFT)
                .build();
    }

    public PolicyResponse mapToPolicyResponseDto(Policy policy) {
        return PolicyResponse.builder()
                .id(policy.getId())
                .title(policy.getTitle())
                .description(policy.getDescription())
                .status(policy.getStatus())
                .createdBy(policy.getCreatedBy())
                .createdAt(policy.getCreatedAt())
                .build();
    }

}
