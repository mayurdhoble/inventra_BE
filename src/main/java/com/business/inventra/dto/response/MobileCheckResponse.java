package com.business.inventra.dto.response;

import com.business.inventra.dto.UserDTO;
import com.business.inventra.dto.CustomerDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MobileCheckResponse {
    private boolean exists;
    private String ownerType;
    private UserDTO user;
    private CustomerDTO customer;

    // Constructor for when number is not registered
    public MobileCheckResponse(boolean exists) {
        this.exists = exists;
    }

    // Constructor for when number is registered
    public MobileCheckResponse(boolean exists, String ownerType, UserDTO user, CustomerDTO customer) {
        this.exists = exists;
        this.ownerType = ownerType;
        this.user = user;
        this.customer = customer;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }
} 