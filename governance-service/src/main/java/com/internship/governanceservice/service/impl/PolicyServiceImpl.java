package com.internship.governanceservice.service.impl;

import com.internship.governanceservice.dto.request.CreatePolicyRequest;
import com.internship.governanceservice.dto.request.UpdatePolicyRequest;
import com.internship.governanceservice.dto.response.PolicyResponse;
import com.internship.governanceservice.entity.Policy;
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

        Policy policy = policyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Policy not found"));

        return policyMapper.mapToPolicyResponseDto(policy);

    }

    @Override
    public PolicyResponse updatePolicy(Long id,
                                       UpdatePolicyRequest request) {

        Policy policy = policyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Policy not found"));

        policyMapper.updateEntity(policy, request);

        Policy updatedPolicy = policyRepository.save(policy);

        return policyMapper.mapToPolicyResponseDto(updatedPolicy);

    }
    @Override
    public void deletePolicy(Long id) {

        Policy policy = policyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Policy not found"));

        policyRepository.delete(policy);

    }
}