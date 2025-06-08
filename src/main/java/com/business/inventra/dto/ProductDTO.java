package com.business.inventra.dto;


public class ProductDTO {
    private String id;
    
    private String pCode;
    
    private String pName;
    
    private String pCatCode;
    
    private String description;
    
    private Boolean isActive;
    
    private String orgId;
    
    private ProductCategoryDTO category;

    public ProductDTO() {
    }

    public ProductDTO(String id, String pCode, String pName, String pCatCode, String description, 
                     Boolean isActive, String orgId, ProductCategoryDTO category) {
        this.id = id;
        this.pCode = pCode;
        this.pName = pName;
        this.pCatCode = pCatCode;
        this.description = description;
        this.isActive = isActive;
        this.orgId = orgId;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ProductCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(ProductCategoryDTO category) {
        this.category = category;
    }
} 