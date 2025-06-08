package com.business.inventra.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import com.business.inventra.util.KeyGenerator;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_branch_mappings",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "branchID"})
    }
)
public class UserBranchMapping extends BaseModel {
    private static final String USER_BRANCH_PREFIX = KeyGenerator.USER_BRANCH_PREFIX;

    @Column(name = "userId", nullable = false)
    private String userId;

    @Column(name = "branchID", nullable = true)
    private String branchID;

    @Column(name = "orgID", nullable = false)
    private String orgID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branchID", referencedColumnName = "id", insertable = false, updatable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orgID", referencedColumnName = "id", insertable = false, updatable = false)
    private Organization organization;

    public UserBranchMapping() {
        this.prefix = USER_BRANCH_PREFIX;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.userId = user.getId();
        }
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
        if (branch != null) {
            this.branchID = branch.getId();
            this.orgID = branch.getOrgID();
        }
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
        if (organization != null) {
            this.orgID = organization.getId();
        }
    }
} 