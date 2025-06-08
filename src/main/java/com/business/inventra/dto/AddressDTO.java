package com.business.inventra.dto;

public class AddressDTO {
    private String id;
    private String categoryCode;
    private String line1;
    private String line2;
    private String line3;
    private String country;
    private String state;
    private String city;
    private Integer postalCode;
    private Boolean isActive;
    private Boolean isCurrent;
    private String type;

    public AddressDTO() {
    }

    public AddressDTO(String id, String categoryCode, String line1, String line2, String line3,
                     String country, String state, String city, Integer postalCode,
                     Boolean isActive, Boolean isCurrent, String type) {
        this.id = id;
        this.categoryCode = categoryCode;
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.country = country;
        this.state = state;
        this.city = city;
        this.postalCode = postalCode;
        this.isActive = isActive;
        this.isCurrent = isCurrent;
        this.type = type;
    }

    // Getters
    public String getId() { return id; }
    public String getCategoryCode() { return categoryCode; }
    public String getLine1() { return line1; }
    public String getLine2() { return line2; }
    public String getLine3() { return line3; }
    public String getCountry() { return country; }
    public String getState() { return state; }
    public String getCity() { return city; }
    public Integer getPostalCode() { return postalCode; }
    public Boolean getIsActive() { return isActive; }
    public Boolean getIsCurrent() { return isCurrent; }
    public String getType() { return type; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }
    public void setLine1(String line1) { this.line1 = line1; }
    public void setLine2(String line2) { this.line2 = line2; }
    public void setLine3(String line3) { this.line3 = line3; }
    public void setCountry(String country) { this.country = country; }
    public void setState(String state) { this.state = state; }
    public void setCity(String city) { this.city = city; }
    public void setPostalCode(Integer postalCode) { this.postalCode = postalCode; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public void setIsCurrent(Boolean isCurrent) { this.isCurrent = isCurrent; }
    public void setType(String type) { this.type = type; }
} 