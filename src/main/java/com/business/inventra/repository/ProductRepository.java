package com.business.inventra.repository;

import com.business.inventra.model.Organization;
import com.business.inventra.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findBypCodeAndOrgId(String pCode, String orgId);
    
    boolean existsBypCodeAndOrgId(String pCode, String orgId);
    
    List<Product> findByOrgId(String orgId);
    
    List<Product> findBypCatCodeAndOrgId(String pCatCode, String orgId);
    
    long countByOrgId(String orgId);
    
    @Query("SELECT p FROM Product p WHERE p.orgId = :orgId AND LOWER(p.pName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> searchByPNameAndOrgId(@Param("orgId") String orgId, @Param("searchTerm") String searchTerm);

} 