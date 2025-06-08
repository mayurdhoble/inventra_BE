package com.business.inventra.repository;

import com.business.inventra.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, String> {
    List<Branch> findByOrgID(String orgID);
    Optional<Branch> findByIdAndOrgID(String id, String orgID);
    boolean existsByNameAndOrgID(String name, String orgID);
    boolean existsByGstNumber(String gstNumber);
} 