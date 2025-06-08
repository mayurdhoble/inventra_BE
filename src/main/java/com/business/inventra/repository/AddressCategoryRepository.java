package com.business.inventra.repository;

import com.business.inventra.model.AddressCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressCategoryRepository extends JpaRepository<AddressCategory, String> {
    Optional<AddressCategory> findByName(String name);
} 