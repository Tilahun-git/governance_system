package com.internship.governanceservice.repository;

import com.internship.governanceservice.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
}
