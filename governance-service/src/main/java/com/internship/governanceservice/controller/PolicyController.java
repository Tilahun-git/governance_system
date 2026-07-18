package com.internship.governanceservice.controller;

import com.internship.governanceservice.dto.request.CreatePolicyRequest;
import com.internship.governanceservice.dto.response.PolicyResponse;
import com.internship.governanceservice.service.PolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
public class PolicyController {
    private final PolicyService policyService;

    @GetMapping
    public ResponseEntity <List<PolicyResponse>> getAllPolicies() {
        return  ResponseEntity.ok().body(policyService.getAllPolicies());
    }
    @PostMapping
    public ResponseEntity<PolicyResponse> createPolicy(@Valid @RequestBody CreatePolicyRequest request){
        PolicyResponse savedPolicy = policyService.createPolicy(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPolicy);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolicyResponse> getPolicyById(@PathVariable Long id) {

        return ResponseEntity.ok(policyService.getPolicyById(id));

    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<PolicyResponse> submitPolicy(@PathVariable Long id) {

        return ResponseEntity.ok(policyService.submitPolicy(id));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<PolicyResponse> approvePolicy(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.approvePolicy(id));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<PolicyResponse> rejectPolicy(@PathVariable Long id) {

        return ResponseEntity.ok(policyService.rejectPolicy(id));
    }


}
