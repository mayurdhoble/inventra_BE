package com.business.inventra.model;

import com.business.inventra.constant.EntityType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "contact_details")
@EqualsAndHashCode(callSuper = true)
public class Contact extends BaseModel {
    private static final String CONTACT_PREFIX = "CON";

    @Column(name = "category_code", nullable = false)
    private String categoryCode;

    @Column(name = "linkage_id", nullable = false)
    private String linkageId;

    @Column(name = "linkage_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EntityType linkageType;

    @Column(name = "dial_code")
    private Integer dialCode;

    @Column(name = "phone_number")
    private Long phoneNumber;

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_code", referencedColumnName = "id", insertable = false, updatable = false)
    private AddressCategory category;

    public Contact() {
        this.prefix = CONTACT_PREFIX;
    }

    // Getters and Setters
    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getLinkageId() {
        return linkageId;
    }

    public void setLinkageId(String linkageId) {
        this.linkageId = linkageId;
    }

    public EntityType getLinkageType() {
        return linkageType;
    }

    public void setLinkageType(EntityType linkageType) {
        this.linkageType = linkageType;
    }

    public Integer getDialCode() {
        return dialCode;
    }

    public void setDialCode(Integer dialCode) {
        this.dialCode = dialCode;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean getIsPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public AddressCategory getCategory() {
        return category;
    }

    public void setCategory(AddressCategory category) {
        this.category = category;
    }
} 