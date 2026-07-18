package com.internship.governanceservice.controller;

import com.internship.governanceservice.dto.request.CreatePolicyRequest;
import com.internship.governanceservice.dto.request.UpdatePolicyRequest;
import com.internship.governanceservice.dto.response.PolicyResponse;
import com.internship.governanceservice.service.PolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePolicy(@PathVariable Long id) {
        policyService.deletePolicy(id);
        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<PolicyResponse> updatePolicy(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePolicyRequest request) {

        return ResponseEntity.ok(policyService.updatePolicy(id, request));

    }
}
