package com.business.inventra.dto.response;

import com.business.inventra.dto.CustomerDTO;

public class CustomerResponse {
    private CustomerDTO customer;

    public CustomerResponse() {
    }

    public CustomerResponse(CustomerDTO customer) {
        this.customer = customer;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }
} 