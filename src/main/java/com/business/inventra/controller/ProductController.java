package com.business.inventra.controller;

import com.business.inventra.dto.request.ProductRequest;
import com.business.inventra.dto.response.ApiResponse;
import com.business.inventra.dto.response.ProductResponse;
import com.business.inventra.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping("/organizations/{orgId}/products")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @PathVariable String orgId,
            @RequestBody ProductRequest request) {

        request.setOrgId(orgId);
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.ok(ApiResponse.success("Product created successfully", response));
    }

    @GetMapping("/organizations/{orgId}/products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts(
            @PathVariable String orgId) {
        List<ProductResponse> response = productService.getAllProducts(orgId);
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", response));
    }

    @GetMapping("/organizations/{orgId}/categories/{categoryId}/products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByCategory(
            @PathVariable String orgId,
            @PathVariable String categoryId) {
        List<ProductResponse> response = productService.getProductsByCategory(orgId, categoryId);
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", response));
    }

    @GetMapping("/organizations/{orgId}/products/search")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProducts(
            @PathVariable String orgId,
            @RequestParam String searchTerm) {
        List<ProductResponse> response = productService.searchProducts(orgId, searchTerm);
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", response));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable String id,
            @RequestParam String orgId) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success("Product retrieved successfully", response));
    }

    @PutMapping("/organizations/{orgId}/products/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable String orgId,
            @PathVariable String id,
            @Valid @RequestBody ProductRequest request) {
        request.setOrgId(orgId);
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", response));
    }

    @DeleteMapping("/organizations/{orgId}/products/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable String orgId,
            @PathVariable String id) {
        productService.deleteProduct(id, orgId);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }
} 