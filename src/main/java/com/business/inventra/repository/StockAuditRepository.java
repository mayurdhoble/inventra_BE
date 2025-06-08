package com.business.inventra.repository;

import com.business.inventra.model.StockAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockAuditRepository extends JpaRepository<StockAudit, String> {
    @Query("SELECT s FROM StockAudit s WHERE s.pCode = :pCode")
    List<StockAudit> findBypCode(@Param("pCode") String pCode);

    @Query("SELECT s FROM StockAudit s WHERE s.branchId = :branchId")
    List<StockAudit> findByBranchId(@Param("branchId") String branchId);

    @Query("SELECT s FROM StockAudit s WHERE s.userId = :userId")
    List<StockAudit> findByUserId(@Param("userId") String userId);

    @Query("SELECT s FROM StockAudit s WHERE s.pCode = :pCode AND s.branchId = :branchId")
    List<StockAudit> findBypCodeAndBranchId(@Param("pCode") String pCode, @Param("branchId") String branchId);

    @Query("SELECT s FROM StockAudit s WHERE s.operationType = :operationType")
    List<StockAudit> findByOperationType(@Param("operationType") StockAudit.OperationType operationType);

    @Query("SELECT s FROM StockAudit s WHERE s.operationDate BETWEEN :startDate AND :endDate")
    List<StockAudit> findByOperationDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT s FROM StockAudit s WHERE s.pCode = :pCode AND s.branchId = :branchId AND s.operationDate BETWEEN :startDate AND :endDate")
    List<StockAudit> findBypCodeAndBranchIdAndOperationDateBetween(
        @Param("pCode") String pCode,
        @Param("branchId") String branchId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);
} 