package com.business.inventra.repository;

import com.business.inventra.model.CustomerBranchMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerBranchMappingRepository extends JpaRepository<CustomerBranchMapping, String> {
    List<CustomerBranchMapping> findByCustomerID(String customerId);
    List<CustomerBranchMapping> findByBranchID(String branchId);
    List<CustomerBranchMapping> findByOrgID(String orgId);
    Optional<CustomerBranchMapping> findByCustomerIDAndBranchID(String customerId, String branchId);
    void deleteByCustomerID(String customerId);
    boolean existsByCustomerIDAndBranchID(String customerId, String branchId);
} 