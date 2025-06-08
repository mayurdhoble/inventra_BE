package com.business.inventra.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductCategoryDTO {
    private String id;
    private String pCatCode;
    private String pCatName;
    private String description;
    private String orgId;
    

	public ProductCategoryDTO(String id, String pCatCode, String pCatName, String description, String orgId) {
		super();
		this.id = id;
		this.pCatCode = pCatCode;
		this.pCatName = pCatName;
		this.description = description;
		this.orgId = orgId;
	}
	public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getpCatCode() {
        return pCatCode;
    }
    public void setpCatCode(String pCatCode) {
        this.pCatCode = pCatCode;
    }
    public String getpCatName() {
        return pCatName;
    }
    public void setpCatName(String pCatName) {
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