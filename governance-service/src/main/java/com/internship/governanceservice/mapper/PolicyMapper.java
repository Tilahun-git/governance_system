package com.internship.governanceservice.mapper;

import com.internship.governanceservice.dto.request.CreatePolicyRequest;
import com.internship.governanceservice.dto.response.PolicyResponse;
import com.internship.governanceservice.entity.Policy;
import com.internship.governanceservice.enums.PolicyStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
        imports = {LocalDateTime.class, PolicyStatus.class})
public interface PolicyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "status", expression = "java(PolicyStatus.DRAFT)")
    Policy mapToPolicyEntity(CreatePolicyRequest request);

    PolicyResponse mapToPolicyResponseDto(Policy policy);

}