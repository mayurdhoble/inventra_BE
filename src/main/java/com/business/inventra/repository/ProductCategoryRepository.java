package com.business.inventra.repository;

import com.business.inventra.model.Organization;
import com.business.inventra.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String> {
    Optional<ProductCategory> findByPCatName(String pCatName);
    boolean existsByPCatName(String pCatName);
    boolean existsByPCatNameAndOrgId(String pCatName, String orgId);
    long countByOrgId(String orgId);
    List<ProductCategory> findByOrgId(String orgId);
    
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.orgId = :orgId AND LOWER(pc.pCatName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<ProductCategory> searchByPCatNameAndOrgId(@Param("orgId") String orgId, @Param("searchTerm") String searchTerm);
    
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.pCatCode = :pCatCode AND pc.orgId = :orgId")
    Optional<ProductCategory> findByPCatCodeAndOrgId(@Param("pCatCode") String pCatCode, @Param("orgId") String orgId);
} 