package com.business.inventra.dto.request;

import com.business.inventra.constant.EntityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MobileCheckRequest {
    @NotBlank(message = "Organization ID is required")
    private String orgId;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobileNumber;
    
    @NotNull(message = "Type of user is required")
    private EntityType userType;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public EntityType getUserType() {
        return userType;
    }

    public void setUserType(EntityType userType) {
        this.userType = userType;
    }
} 