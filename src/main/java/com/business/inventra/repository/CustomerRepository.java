package com.business.inventra.repository;

import com.business.inventra.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    List<Customer> findByOrgID(String orgId);
    Optional<Customer> findByEmailAndOrgID(String email, String orgId);
    Optional<Customer> findByMobileAndOrgID(String mobile, String orgId);
    boolean existsByEmailAndOrgID(String email, String orgId);
    boolean existsByMobileAndOrgID(String mobile, String orgId);
} 