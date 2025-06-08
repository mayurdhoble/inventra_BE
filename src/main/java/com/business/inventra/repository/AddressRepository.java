package com.business.inventra.repository;

import com.business.inventra.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    List<Address> findByLinkageId(String linkageId);
    void deleteByLinkageId(String linkageId);
} 