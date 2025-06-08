package com.business.inventra.model;

import com.business.inventra.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import com.business.inventra.util.KeyGenerator;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import com.business.inventra.model.Organization;

@Entity
@Table(name = "product_category_master",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"org_id", "p_cat_name"})
    }
)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ProductCategory extends BaseModel {
    private static final String PRODUCT_CATEGORY_PREFIX = KeyGenerator.PRODUCT_CATEGORY_PREFIX;

    @Column(name = "p_cat_code", nullable = false, unique = true)
    private String pCatCode;

    @Column(name = "p_cat_name", nullable = false)
    private String pCatName;

    @Column(name = "description")
    private String description;

    @Column(name = "org_id", nullable = false)
    private String orgId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Organization organization;

    public ProductCategory() {
        this.prefix = PRODUCT_CATEGORY_PREFIX;
    }

    public String getPCatCode() {
        return pCatCode;
    }

    public void setPCatCode(String pCatCode) {
        this.pCatCode = pCatCode;
    }

    public String getpCatName() {
        return pCatName;
    }

    public void setpCatName(String pCatName) {
        this.pCatName = pCatName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
        if (organization != null) {
            this.orgId = organization.getId();
        }
    }
} 