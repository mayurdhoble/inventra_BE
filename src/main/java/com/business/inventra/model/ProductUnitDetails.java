package com.business.inventra.model;

import jakarta.persistence.*;
import com.business.inventra.util.KeyGenerator;

@Entity
@Table(name = "product_unit_details",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"p_code", "p_unit", "org_id"})
    }
)
public class ProductUnitDetails extends BaseModel {
    private static final String PRODUCT_UNIT_DETAILS_PREFIX = "PUD";

    @Column(name = "p_code", nullable = false)
    private String pCode;

    @Column(name = "p_unit", nullable = false)
    private String pUnit;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @Column(name = "org_id", nullable = false)
    private String orgId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_code", referencedColumnName = "p_code", insertable = false, updatable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_unit", referencedColumnName = "id", insertable = false, updatable = false)
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Organization organization;

    public ProductUnitDetails() {
        this.prefix = PRODUCT_UNIT_DETAILS_PREFIX;
    }

    public String getPCode() {
        return pCode;
    }

    public void setPCode(String pCode) {
        this.pCode = pCode;
    }

    public String getPUnit() {
        return pUnit;
    }

    public void setPUnit(String pUnit) {
        this.pUnit = pUnit;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            this.pCode = product.getPCode();
            this.orgId = product.getOrgId();
        }
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
        if (unit != null) {
            this.pUnit = unit.getId();
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