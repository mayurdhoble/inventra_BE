package com.business.inventra.repository;

import com.business.inventra.model.UserBranchMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBranchMappingRepository extends JpaRepository<UserBranchMapping, String> {
    List<UserBranchMapping> findByUserId(String userId);
    List<UserBranchMapping> findByBranchID(String branchId);
    List<UserBranchMapping> findByOrgID(String orgId);
    List<UserBranchMapping> findByUserIdAndOrgID(String userId, String orgId);
    Optional<UserBranchMapping> findByUserIdAndBranchID(String userId, String branchId);
    void deleteByUserIdAndBranchID(String userId, String branchId);
    void deleteByUserId(String userId);
    boolean existsByUserIdAndOrgID(String userId, String orgId);
    List<UserBranchMapping> findByOrgIDAndBranchID(String orgId, String branchId);

    @Query("SELECT CASE WHEN COUNT(ubm) > 0 THEN true ELSE false END FROM UserBranchMapping ubm " +
           "JOIN User u ON ubm.userId = u.id " +
           "WHERE u.userName = :userName AND ubm.orgID = :orgId")
    boolean existsByUserNameAndOrgID(@Param("userName") String userName, @Param("orgId") String orgId);
	boolean existsByUserIdAndBranchId(String userId, String branchId);
} 