package com.business.inventra.repository;

import com.business.inventra.model.ProductUnitDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductUnitDetailsRepository extends JpaRepository<ProductUnitDetails, String> {
    @Query("SELECT p FROM ProductUnitDetails p WHERE p.pCode = :pCode")
    List<ProductUnitDetails> findBypCode(@Param("pCode") String pCode);

    @Query("SELECT p FROM ProductUnitDetails p WHERE p.pCode = :pCode AND p.isDefault = true")
    Optional<ProductUnitDetails> findBypCodeAndIsDefaultTrue(@Param("pCode") String pCode);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM ProductUnitDetails p WHERE p.pCode = :pCode AND p.pUnit = :pUnit AND p.orgId = :orgId")
    boolean existsBypCodeAndpUnitAndOrgId(@Param("pCode") String pCode, @Param("pUnit") String pUnit, @Param("orgId") String orgId);

    @Query("SELECT p FROM ProductUnitDetails p WHERE p.pCode = :pCode AND p.pUnit = :pUnit AND p.orgId = :orgId")
    List<ProductUnitDetails> findBypCodeAndpUnitAndOrgId(@Param("pCode") String pCode, @Param("pUnit") String pUnit, @Param("orgId") String orgId);
} 