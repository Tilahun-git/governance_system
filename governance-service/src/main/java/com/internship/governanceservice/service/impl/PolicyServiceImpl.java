package com.internship.governanceservice.service.impl;

import com.internship.governanceservice.dto.request.CreatePolicyRequest;
import com.internship.governanceservice.dto.response.PolicyResponse;
import com.internship.governanceservice.entity.Policy;
import com.internship.governanceservice.enums.EventType;
import com.internship.governanceservice.enums.PolicyStatus;
import com.internship.governanceservice.event.PolicyEvent;
import com.internship.governanceservice.kafka.EventPublisher;
import com.internship.governanceservice.mapper.PolicyMapper;
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
    private final PolicyMapper policyMapper;
    private final EventPublisher eventPublisher;

    @Override
    public PolicyResponse createPolicy(CreatePolicyRequest request) {
       Policy policy = policyMapper.mapToPolicyEntity(request);
       Policy createdPolicy = policyRepository.save(policy);

        // policy-created Kafka event publish
        publishPolicyEvent(createdPolicy,EventType.POLICY_CREATED);

       return  policyMapper.mapToPolicyResponseDto(createdPolicy);

    }

    @Override
    public List<PolicyResponse> getAllPolicies() {

        return policyRepository.findAll()
                .stream()
                .map(policyMapper::mapToPolicyResponseDto)
                .toList();

    }


    @Override
    public PolicyResponse getPolicyById(Long id) {
        return policyMapper.mapToPolicyResponseDto(getPolicyByIdOrThrow(id));
    }

    @Override
    public PolicyResponse submitPolicy(Long id) {

        Policy policy = getPolicyByIdOrThrow(id);
        if(policy.getStatus() != PolicyStatus.DRAFT){
            throw  new RuntimeException("Only DRAFT policies can be submitted");
        }
        policy.setStatus(PolicyStatus.PENDING_APPROVAL);
        Policy submittedPolicy = policyRepository.save(policy);

        // policy-submitted Kafka event publish
        publishPolicyEvent(submittedPolicy,EventType.POLICY_SUBMITTED);

        return policyMapper.mapToPolicyResponseDto(submittedPolicy);
    }

    @Override
    public PolicyResponse approvePolicy(Long id) {
        Policy policy = getPolicyByIdOrThrow(id);
        if(policy.getStatus() != PolicyStatus.PENDING_APPROVAL){
            throw  new RuntimeException("Only PENDING_APPROVAL policies can be approved");
        }
        policy.setStatus(PolicyStatus.APPROVED);
        Policy approvedPolicy = policyRepository.save(policy);

        // policy-approved Kafka event publish
        publishPolicyEvent(approvedPolicy,EventType.POLICY_APPROVED);

        return policyMapper.mapToPolicyResponseDto(approvedPolicy);
    }

    @Override
    public PolicyResponse rejectPolicy(Long id) {

        Policy policy = getPolicyByIdOrThrow(id);
        if (policy.getStatus() != PolicyStatus.PENDING_APPROVAL) {
            throw new RuntimeException("Only PENDING_APPROVAL policies can be rejected.");
        }
        policy.setStatus(PolicyStatus.REJECTED);
        Policy rejectedPolicy = policyRepository.save(policy);

        // policy-rejected Kafka event publish
        publishPolicyEvent(rejectedPolicy,EventType.POLICY_REJECTED);

        return policyMapper.mapToPolicyResponseDto(rejectedPolicy);
    }





     // Helper method to avoid repeating findById logic.
    private Policy getPolicyByIdOrThrow(Long id) {

        return policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found with id: " + id));
    }

    // Helper method to publish policy event
    private void publishPolicyEvent(Policy policy, EventType eventType) {
        PolicyEvent event = PolicyEvent.builder()
                .eventType(eventType)
                .policyId(policy.getId())
                .actor(policy.getCreatedBy())
                .timestamp(LocalDateTime.now())
                .build();

        eventPublisher.publish(event);
    }

}