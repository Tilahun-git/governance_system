package com.internship.governanceservice.service.impl;

import com.internship.governanceservice.dto.request.CreatePolicyRequest;
import com.internship.governanceservice.dto.response.PolicyResponse;
import com.internship.governanceservice.entity.Policy;
import com.internship.governanceservice.enums.PolicyStatus;
import com.internship.governanceservice.mapper.PolicyMapper;
import com.internship.governanceservice.repository.PolicyRepository;
import com.internship.governanceservice.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final PolicyMapper policyMapper;

    @Override
    public PolicyResponse createPolicy(CreatePolicyRequest request) {

       Policy policy = policyMapper.mapToPolicyEntity(request);
       Policy savedPolicy = policyRepository.save(policy);

       //kafka will be later implemented here
       return  policyMapper.mapToPolicyResponseDto(savedPolicy);

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
        Policy updatedPolicy = policyRepository.save(policy);

        // kafka will be implemented here later
        return policyMapper.mapToPolicyResponseDto(updatedPolicy);
    }

    @Override
    public PolicyResponse approvePolicy(Long id) {
        Policy policy = getPolicyByIdOrThrow(id);
        if(policy.getStatus() != PolicyStatus.PENDING_APPROVAL){
            throw  new RuntimeException("Only PENDING_APPROVAL policies can be approved");
        }
        policy.setStatus(PolicyStatus.APPROVED);
        Policy updatedPolicy = policyRepository.save(policy);

        //policy approved kafka will be implemented here later
        return policyMapper.mapToPolicyResponseDto(updatedPolicy);
    }

    @Override
    public PolicyResponse rejectPolicy(Long id) {

        Policy policy = getPolicyByIdOrThrow(id);

        if (policy.getStatus() != PolicyStatus.PENDING_APPROVAL) {
            throw new RuntimeException("Only PENDING_APPROVAL policies can be rejected.");
        }

        policy.setStatus(PolicyStatus.REJECTED);

        Policy updatedPolicy = policyRepository.save(policy);

        // policy-rejected Kafka event will be implemented later

        return policyMapper.mapToPolicyResponseDto(updatedPolicy);
    }





     // Helper method to avoid repeating findById logic.
    private Policy getPolicyByIdOrThrow(Long id) {

        return policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found with id: " + id));
    }

}