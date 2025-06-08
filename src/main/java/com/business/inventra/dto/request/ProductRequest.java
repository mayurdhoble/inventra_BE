package com.business.inventra.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductRequest {
    @NotBlank(message = "Product name is required")
    @JsonProperty("pName")
    private String pName;

    @NotBlank(message = "Product category code is required")
    @JsonProperty("pCatCode")
    private String pCatCode;

    @JsonProperty("description")
    private String description;

    @NotNull(message = "Active status is required")
    @JsonProperty("isActive")
    private Boolean isActive;

    @NotBlank(message = "Organization ID is required")
    @JsonProperty("orgId")
    private String orgId;

    public ProductRequest() {
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

    @Override
    public String toString() {
        return "ProductRequest{" +
                "pName='" + pName + '\'' +
                ", pCatCode='" + pCatCode + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                ", orgId='" + orgId + '\'' +
                '}';
    }
} 