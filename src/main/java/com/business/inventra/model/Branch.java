package com.business.inventra.model;

import com.business.inventra.util.KeyGenerator;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "branches",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"orgID", "name"})
    }
)
public class Branch extends BaseModel {

    private static final String BRANCH_PREFIX = KeyGenerator.BRANCH_PREFIX;

    @Column(name = "orgID", nullable = false)
    private String orgID;

    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "gst_number", nullable = true)
    private String gstNumber; // Optional GST number

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orgID", insertable = false, updatable = false)
    private Organization organization;

    public Branch() {
        this.prefix = BRANCH_PREFIX;
    }

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}
    
    
}
