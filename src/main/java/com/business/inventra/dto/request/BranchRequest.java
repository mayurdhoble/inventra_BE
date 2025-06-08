package com.business.inventra.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BranchRequest {
    @NotBlank(message = "Organization ID is required")
    private String orgID;

    @NotBlank(message = "Branch name is required")
    private String name;

    @NotBlank(message = "GST number is required")
    private String gstNumber;

	public String getOrgID() {
		return orgID;
	}

	public void setOrgID(String orgID) {
		this.orgID = orgID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}
    
    
    
} 