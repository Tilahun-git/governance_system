package com.internship.governanceservice.service;

import com.internship.governanceservice.dto.request.CreatePolicyRequest;
import com.internship.governanceservice.dto.response.PolicyResponse;
import com.internship.governanceservice.entity.Policy;
import com.internship.governanceservice.enums.PolicyStatus;
import com.internship.governanceservice.event.PolicyEvent;
import com.internship.governanceservice.mapper.PolicyMapper;
import com.internship.governanceservice.publisher.EventPublisher;
import com.internship.governanceservice.repository.PolicyRepository;
import com.internship.governanceservice.service.impl.PolicyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PolicyServiceImplTest {

    private PolicyRepository policyRepository;
    private PolicyMapper policyMapper;
    private EventPublisher eventPublisher;
    private PolicyService policyService;

    @BeforeEach
    void setUp() {
        policyRepository = mock(PolicyRepository.class);
        policyMapper = mock(PolicyMapper.class);
        eventPublisher = mock(EventPublisher.class);

        policyService = new PolicyServiceImpl(
                policyRepository,
                policyMapper,
                eventPublisher
        );
    }

    @Test
    void createPolicy_ShouldCreatePolicy() {

        CreatePolicyRequest request = CreatePolicyRequest.builder()
                .title("Security Policy")
                .description("Company security rules")
                .createdBy("admin")
                .build();

        Policy policy = Policy.builder()
                .id(1L)
                .title("Security Policy")
                .description("Company security rules")
                .status(PolicyStatus.DRAFT)
                .createdBy("admin")
                .build();

        PolicyResponse response = PolicyResponse.builder()
                .id(1L)
                .title("Security Policy")
                .description("Company security rules")
                .status(PolicyStatus.DRAFT)
                .createdBy("admin")
                .build();

        when(policyMapper.mapToPolicyEntity(request))
                .thenReturn(policy);

        when(policyRepository.save(policy))
                .thenReturn(policy);

        when(policyMapper.mapToPolicyResponseDto(policy))
                .thenReturn(response);

        PolicyResponse result = policyService.createPolicy(request);

        assertThat(result.getTitle())
                .isEqualTo("Security Policy");
        verify(policyRepository)
                .save(policy);
        verify(eventPublisher).publish(any(PolicyEvent.class));
    }

    @Test
    void getAllPolicies_ShouldReturnPolicies() {

        Policy policy1 = Policy.builder()
                .id(1L)
                .title("Policy One")
                .build();

        Policy policy2 = Policy.builder()
                .id(2L)
                .title("Policy Two")
                .build();
        when(policyRepository.findAll())
                .thenReturn(List.of(policy1, policy2));
        when(policyMapper.mapToPolicyResponseDto(any()))
                .thenReturn(
                        PolicyResponse.builder()
                                .title("Policy")
                                .build());

        List<PolicyResponse> result = policyService.getAllPolicies();
        assertThat(result).hasSize(2);
        verify(policyRepository).findAll();
    }

    @Test
    void getPolicyById_ShouldReturnPolicy() {

        Policy policy = Policy.builder()
                .id(1L)
                .title("Test Policy")
                .build();
        PolicyResponse response = PolicyResponse.builder()
                .id(1L)
                .title("Test Policy")
                .build();
        when(policyRepository.findById(1L))
                .thenReturn(Optional.of(policy));
        when(policyMapper.mapToPolicyResponseDto(policy))
                .thenReturn(response);
        PolicyResponse result = policyService.getPolicyById(1L);
        assertThat(result.getId())
                .isEqualTo(1L);
    }

    @Test
    void submitPolicy_ShouldUpdateStatusToPendingApproval() {

        Policy policy = Policy.builder()
                .id(1L)
                .status(PolicyStatus.DRAFT)
                .createdBy("admin")
                .build();

        when(policyRepository.findById(1L))
                .thenReturn(Optional.of(policy));
        when(policyRepository.save(policy))
                .thenReturn(policy);
        when(policyMapper.mapToPolicyResponseDto(policy))
                .thenReturn(
                        PolicyResponse.builder()
                                .status(PolicyStatus.PENDING_APPROVAL)
                                .build());

        PolicyResponse result = policyService.submitPolicy(1L);

        assertThat(result.getStatus())
                .isEqualTo(PolicyStatus.PENDING_APPROVAL);
        verify(eventPublisher)
                .publish(any(PolicyEvent.class));
    }

    @Test
    void approvePolicy_ShouldUpdateStatusToApproved() {

        Policy policy = Policy.builder()
                .id(1L)
                .status(PolicyStatus.PENDING_APPROVAL)
                .createdBy("admin")
                .build();

        when(policyRepository.findById(1L))
                .thenReturn(Optional.of(policy));

        when(policyRepository.save(policy))
                .thenReturn(policy);
        when(policyMapper.mapToPolicyResponseDto(policy))
                .thenReturn(
                        PolicyResponse.builder()
                                .status(PolicyStatus.APPROVED)
                                .build());
        PolicyResponse result = policyService.approvePolicy(1L);
        assertThat(result.getStatus())
                .isEqualTo(PolicyStatus.APPROVED);
        verify(eventPublisher)
                .publish(any(PolicyEvent.class));
    }
    @Test
    void rejectPolicy_ShouldUpdateStatusToRejected() {

        Policy policy = Policy.builder()
                .id(1L)
                .status(PolicyStatus.PENDING_APPROVAL)
                .createdBy("admin")
                .build();

        when(policyRepository.findById(1L))
                .thenReturn(Optional.of(policy));

        when(policyRepository.save(policy))
                .thenReturn(policy);

        when(policyMapper.mapToPolicyResponseDto(policy))
                .thenReturn(
                        PolicyResponse.builder()
                                .status(PolicyStatus.REJECTED)
                                .build()
                );

        PolicyResponse result = policyService.rejectPolicy(1L);

        assertThat(result.getStatus())
                .isEqualTo(PolicyStatus.REJECTED);

        verify(eventPublisher)
                .publish(any(PolicyEvent.class));
    }

    @Test
    void getPolicyById_ShouldThrowException_WhenPolicyNotFound() {

        when(policyRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> policyService.getPolicyById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Policy not found with id: 99");
    }

    @Test
    void submitPolicy_ShouldThrowException_WhenPolicyIsNotDraft() {

        Policy policy = Policy.builder()
                .id(1L)
                .status(PolicyStatus.APPROVED)
                .build();

        when(policyRepository.findById(1L))
                .thenReturn(Optional.of(policy));

        assertThatThrownBy(() -> policyService.submitPolicy(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Only DRAFT policies can be submitted");
    }
}