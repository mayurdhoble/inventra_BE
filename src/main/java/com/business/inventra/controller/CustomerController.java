package com.business.inventra.controller;

import com.business.inventra.dto.request.CustomerRequest;
import com.business.inventra.dto.response.ApiResponse;
import com.business.inventra.dto.response.CustomerResponse;
import com.business.inventra.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/customers")
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
        @Valid @RequestBody CustomerRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Customer created successfully", customerService.createCustomer(request)));
    }

    @GetMapping("/organizations/{orgId}/branches/{branchId}/customers")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomersByBranch(
            @PathVariable String orgId,
            @PathVariable String branchId) {
        List<CustomerResponse> customers = customerService.getAllCustomersByBranch(orgId, branchId);
        return ResponseEntity.ok(ApiResponse.success("Customers retrieved successfully", customers));
    }

    @GetMapping("/organizations/{orgId}/customers")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomersByOrg(
            @PathVariable String orgId) {
        List<CustomerResponse> customers = customerService.getAllCustomers(orgId);
        return ResponseEntity.ok(ApiResponse.success("Customers retrieved successfully", customers));
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(
            @PathVariable String id,
            @RequestParam String orgId) {
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success("Customer retrieved successfully", response));
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable String id,
            @Valid @RequestBody CustomerRequest request) {
        CustomerResponse response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(ApiResponse.success("Customer updated successfully", response));
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(
            @PathVariable String id,
            @RequestParam String orgId) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Customer deleted successfully", null));
    }
} 