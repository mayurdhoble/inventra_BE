package com.business.inventra.repository;

import com.business.inventra.model.ProductPricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPricingRepository extends JpaRepository<ProductPricing, String> {
    @Query("SELECT p FROM ProductPricing p WHERE p.pCode = :pCode")
    List<ProductPricing> findBypCode(@Param("pCode") String pCode);

    @Query("SELECT p FROM ProductPricing p WHERE p.pCode = :pCode AND p.pUnit = :pUnit")
    List<ProductPricing> findBypCodeAndpUnit(@Param("pCode") String pCode, @Param("pUnit") String pUnit);

    @Query("SELECT p FROM ProductPricing p WHERE p.pCode = :pCode AND p.pUnit = :pUnit AND p.isDefault = true")
    Optional<ProductPricing> findBypCodeAndpUnitAndIsDefaultTrue(@Param("pCode") String pCode, @Param("pUnit") String pUnit);

    @Query("SELECT p FROM ProductPricing p WHERE p.pCode = :pCode AND p.effectiveDate <= :date AND (p.endDate IS NULL OR p.endDate >= :sameDate)")
    List<ProductPricing> findBypCodeAndEffectiveDateLessThanEqualAndEndDateIsNullOrEndDateGreaterThanEqual(
        @Param("pCode") String pCode, @Param("date") LocalDate date, @Param("sameDate") LocalDate sameDate);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM ProductPricing p WHERE p.pCode = :pCode AND p.pUnit = :pUnit AND p.effectiveDate = :effectiveDate")
    boolean existsBypCodeAndpUnitAndEffectiveDate(@Param("pCode") String pCode, @Param("pUnit") String pUnit, @Param("effectiveDate") LocalDate effectiveDate);
} 