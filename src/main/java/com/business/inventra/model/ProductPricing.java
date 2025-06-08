package com.business.inventra.model;

import jakarta.persistence.*;
import com.business.inventra.util.KeyGenerator;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "product_pricing",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"p_code", "p_unit", "effective_date", "org_id"})
    }
)
public class ProductPricing extends BaseModel {
    private static final String PRODUCT_PRICING_PREFIX = "PRC";

    @Column(name = "p_code", nullable = false)
    private String pCode;

    @Column(name = "p_unit", nullable = false)
    private String pUnit;

    @Column(name = "price_per_unit", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerUnit;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_default")
    private Boolean isDefault = false;

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

    public ProductPricing() {
        this.prefix = PRODUCT_PRICING_PREFIX;
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

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
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