package com.internship.governanceservice.service;

import com.internship.governanceservice.dto.request.CreatePolicyRequest;
import com.internship.governanceservice.dto.request.UpdatePolicyRequest;
import com.internship.governanceservice.dto.response.PolicyResponse;

import java.util.List;

public interface PolicyService {

    PolicyResponse createPolicy(CreatePolicyRequest request);

    List<PolicyResponse> getAllPolicies();

    PolicyResponse getPolicyById(Long id);

    PolicyResponse updatePolicy(Long id, UpdatePolicyRequest request);

    void deletePolicy(Long id);
}