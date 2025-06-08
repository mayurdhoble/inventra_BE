package com.business.inventra.model;

import com.business.inventra.constant.EntityType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "address_details")
@EqualsAndHashCode(callSuper = true)
public class Address extends BaseModel {
    private static final String ADDRESS_PREFIX = "ADD";

    @Column(name = "category_code", nullable = false)
    private String categoryCode;

    @Column(name = "linkage_id", nullable = false)
    private String linkageId;

    @Column(name = "linkage_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EntityType linkageType;

    @Column(name = "line1")
    private String line1;

    @Column(name = "line2")
    private String line2;

    @Column(name = "line3")
    private String line3;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "postal_code")
    private Integer postalCode;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_current")
    private Boolean isCurrent;

    @Column(name = "type")
    private String type; // Permanent/Current

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_code", referencedColumnName = "id", insertable = false, updatable = false)
    private AddressCategory addressCategory;

    public Address() {
        this.prefix = ADDRESS_PREFIX;
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

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine3() {
        return line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AddressCategory getAddressCategory() {
        return addressCategory;
    }

    public void setAddressCategory(AddressCategory addressCategory) {
        this.addressCategory = addressCategory;
    }
} 