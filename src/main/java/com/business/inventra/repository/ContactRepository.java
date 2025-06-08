package com.business.inventra.repository;

import com.business.inventra.constant.EntityType;
import com.business.inventra.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
    List<Contact> findByLinkageId(String linkageId);
    void deleteByLinkageId(String linkageId);
    boolean existsByPhoneNumberAndLinkageType(long phoneNumber, EntityType linkageType);

    @Query("SELECT c FROM Contact c " +
           "JOIN User u ON c.linkageId = u.id " +
           "JOIN UserBranchMapping m ON u.id = m.user.id " +
           "WHERE c.phoneNumber = :phoneNumber AND m.orgID = :orgId " +
           "UNION " +
           "SELECT c FROM Contact c " +
           "JOIN Customer cust ON c.linkageId = cust.id " +
           "WHERE c.phoneNumber = :phoneNumber AND cust.orgID = :orgId")
    Contact findByPhoneNumberAndOrgId(@Param("phoneNumber") Long phoneNumber, @Param("orgId") String orgId);

    @Query("SELECT c FROM Contact c " +
           "LEFT JOIN User u ON c.linkageId = u.id AND c.linkageType = 'USER' " +
           "LEFT JOIN Customer cust ON c.linkageId = cust.id AND c.linkageType = 'CUSTOMER' " +
           "LEFT JOIN UserBranchMapping m ON u.id = m.user.id " +
           "LEFT JOIN CustomerBranchMapping cm ON cust.id = cm.customerID " +
           "WHERE c.phoneNumber = :phoneNumber " +
           "AND c.isPrimary = true AND c.linkageType = :linkageType " +
           "AND ((c.linkageType = 'USER' AND m.orgID = :orgId) OR " +
           "     (c.linkageType = 'CUSTOMER' AND cm.orgID = :orgId))")
    List<Contact> findByMobileNumberAndTypeAndOrgId(
        @Param("phoneNumber") Long phoneNumber,
        @Param("linkageType") EntityType linkageType,
        @Param("orgId") String orgId
    );

    @Query("SELECT c FROM Contact c " +
           "JOIN Customer cust ON c.linkageId = cust.id " +
           "WHERE c.phoneNumber = :phoneNumber " +
           "AND c.linkageType = :linkageType " +
           "AND cust.orgID = :orgId")
    List<Contact> findByPhoneNumberAndLinkageTypeAndOrgId(Long phoneNumber, EntityType linkageType, String orgId);
} 