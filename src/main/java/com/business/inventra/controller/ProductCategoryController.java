package com.business.inventra.controller;

import com.business.inventra.dto.request.ProductCategoryRequest;
import com.business.inventra.dto.response.ApiResponse;
import com.business.inventra.dto.response.ProductCategoryResponse;
import com.business.inventra.service.ProductCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @PostMapping("/organizations/{orgId}/product-categories")
    public ResponseEntity<ApiResponse<ProductCategoryResponse>> createProductCategory(
            @PathVariable String orgId,
            @Valid @RequestBody ProductCategoryRequest request) {
        request.setOrgId(orgId);
        ProductCategoryResponse response = productCategoryService.createProductCategory(request);
        return ResponseEntity.ok(ApiResponse.success("Product category created successfully", response));
    }

    @GetMapping("/organizations/{orgId}/product-categories")
    public ResponseEntity<ApiResponse<List<ProductCategoryResponse>>> getAllProductCategories(
            @PathVariable String orgId) {
        List<ProductCategoryResponse> response = productCategoryService.getAllProductCategories(orgId);
        return ResponseEntity.ok(ApiResponse.success("Product categories retrieved successfully", response));
    }

    @GetMapping("/organizations/{orgId}/product-categories/search")
    public ResponseEntity<ApiResponse<List<ProductCategoryResponse>>> searchProductCategories(
            @PathVariable String orgId,
            @RequestParam String searchTerm) {
        List<ProductCategoryResponse> response = productCategoryService.searchProductCategories(orgId, searchTerm);
        return ResponseEntity.ok(ApiResponse.success("Product categories retrieved successfully", response));
    }

    @PutMapping("/organizations/{orgId}/product-categories/{id}")
    public ResponseEntity<ApiResponse<ProductCategoryResponse>> updateProductCategory(
            @PathVariable String orgId,
            @PathVariable String id,
            @Valid @RequestBody ProductCategoryRequest request) {
        request.setOrgId(orgId);
        ProductCategoryResponse response = productCategoryService.updateProductCategory(id, request);
        return ResponseEntity.ok(ApiResponse.success("Product category updated successfully", response));
    }

    @DeleteMapping("/organizations/{orgId}/product-categories/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProductCategory(
            @PathVariable String orgId,
            @PathVariable String id) {
        productCategoryService.deleteProductCategory(id, orgId);
        return ResponseEntity.ok(ApiResponse.success("Product category deleted successfully", null));
    }
} 