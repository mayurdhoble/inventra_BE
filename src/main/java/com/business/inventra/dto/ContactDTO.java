package com.business.inventra.dto;

public class ContactDTO {
    private String id;
    private String categoryCode;
    private Integer dialCode;
    private Long phoneNumber;
    private Boolean isPrimary;
    private Boolean isActive;

    public ContactDTO() {
    }

    public ContactDTO(String id, String categoryCode, Integer dialCode, Long phoneNumber, Boolean isPrimary) {
        this.id = id;
        this.categoryCode = categoryCode;
        this.dialCode = dialCode;
        this.phoneNumber = phoneNumber;
        this.isPrimary = isPrimary;
    }

    public ContactDTO(String id, String categoryCode, Integer dialCode, Long phoneNumber, Boolean isPrimary, Boolean isActive) {
        this(id, categoryCode, dialCode, phoneNumber, isPrimary);
        this.isActive = isActive;
    }

    // Getters
    public String getId() { return id; }
    public String getCategoryCode() { return categoryCode; }
    public Integer getDialCode() { return dialCode; }
    public Long getPhoneNumber() { return phoneNumber; }
    public Boolean getIsPrimary() { return isPrimary; }
    public Boolean getIsActive() { return isActive; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }
    public void setDialCode(Integer dialCode) { this.dialCode = dialCode; }
    public void setPhoneNumber(Long phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
} 