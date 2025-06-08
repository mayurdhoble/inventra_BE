package com.business.inventra.model;

import jakarta.persistence.*;
import com.business.inventra.util.KeyGenerator;

@Entity
@Table(name = "product",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"org_id", "p_code"})
    }
)
public class Product extends BaseModel {
    private static final String PRODUCT_PREFIX = KeyGenerator.PRODUCT_PREFIX;

    @Column(name = "p_code", nullable = false)
    private String pCode;

    @Column(name = "p_name", nullable = false)
    private String pName;

    @Column(name = "p_cat_code", nullable = false)
    private String pCatCode;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "org_id", nullable = false)
    private String orgId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_cat_code", referencedColumnName = "p_cat_code", insertable = false, updatable = false)
    private ProductCategory productCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Organization organization;

    public Product() {
        this.prefix = PRODUCT_PREFIX;
    }

    public String getPCode() {
        return pCode;
    }

    public void setPCode(String pCode) {
        this.pCode = pCode;
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }

    public String getPCatCode() {
        return pCatCode;
    }

    public void setPCatCode(String pCatCode) {
        this.pCatCode = pCatCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
        if (productCategory != null) {
            this.pCatCode = productCategory.getPCatCode();
        }
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