package com.business.inventra.model;

import jakarta.persistence.*;
import com.business.inventra.util.KeyGenerator;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_maintenance",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"p_code", "branch_id", "p_unit", "org_id"})
    }
)
public class StockMaintenance extends BaseModel {
    private static final String STOCK_MAINTENANCE_PREFIX = "STM";

    @Column(name = "p_code", nullable = false)
    private String pCode;

    @Column(name = "branch_id", nullable = false)
    private String branchId;

    @Column(name = "p_unit", nullable = false)
    private String pUnit;

    @Column(name = "current_stock", nullable = false, precision = 10, scale = 2)
    private BigDecimal currentStock = BigDecimal.ZERO;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    @Column(name = "threshold_level", precision = 10, scale = 2)
    private BigDecimal thresholdLevel;

    @Column(name = "org_id", nullable = false)
    private String orgId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_code", referencedColumnName = "p_code", insertable = false, updatable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_unit", referencedColumnName = "id", insertable = false, updatable = false)
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Organization organization;

    public StockMaintenance() {
        this.prefix = STOCK_MAINTENANCE_PREFIX;
    }

    public String getPCode() {
        return pCode;
    }

    public void setPCode(String pCode) {
        this.pCode = pCode;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getPUnit() {
        return pUnit;
    }

    public void setPUnit(String pUnit) {
        this.pUnit = pUnit;
    }

    public BigDecimal getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(BigDecimal currentStock) {
        this.currentStock = currentStock;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public BigDecimal getThresholdLevel() {
        return thresholdLevel;
    }

    public void setThresholdLevel(BigDecimal thresholdLevel) {
        this.thresholdLevel = thresholdLevel;
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

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
        if (branch != null) {
            this.branchId = branch.getId();
            this.orgId = branch.getOrgID();
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