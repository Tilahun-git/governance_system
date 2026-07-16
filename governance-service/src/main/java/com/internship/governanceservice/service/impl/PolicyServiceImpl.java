package com.internship.governanceservice.service.impl;

import com.internship.governanceservice.dto.request.CreatePolicyRequest;
import com.internship.governanceservice.dto.response.PolicyResponse;
import com.internship.governanceservice.entity.Policy;
import com.internship.governanceservice.enums.PolicyStatus;
import com.internship.governanceservice.repository.PolicyRepository;
import com.internship.governanceservice.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;

    @Override
    public PolicyResponse createPolicy(CreatePolicyRequest request) {

        Policy policy = Policy.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .createdBy(request.getCreatedBy())
                .status(PolicyStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .build();

        Policy savedPolicy = policyRepository.save(policy);

        return PolicyResponse.builder()
                .id(savedPolicy.getId())
                .title(savedPolicy.getTitle())
                .description(savedPolicy.getDescription())
                .status(savedPolicy.getStatus())
                .createdBy(savedPolicy.getCreatedBy())
                .createdAt(savedPolicy.getCreatedAt())
                .build();
    }

    @Override
    public List<PolicyResponse> getAllPolicies() {
        throw new UnsupportedOperationException("Will be implemented next");
    }



    @Override
    public PolicyResponse getPolicyById(Long id) {
        throw new UnsupportedOperationException("Will be implemented next");
    }
}