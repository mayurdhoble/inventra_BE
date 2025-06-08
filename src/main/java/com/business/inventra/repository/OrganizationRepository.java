package com.business.inventra.repository;

import com.business.inventra.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, String> {
    boolean existsByEmail(String email);
    Optional<Organization> findByEmail(String email);
} 