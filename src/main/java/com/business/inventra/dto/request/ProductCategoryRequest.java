package com.business.inventra.dto.request;

import lombok.Data;

@Data
public class ProductCategoryRequest {
    private String pCatName;
    private String description;
    private String orgId;
	public String getpCatName() {
		return pCatName;
	}
	public void setPCatName(String pCatName) {
		this.pCatName = pCatName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
    
    
    
} 