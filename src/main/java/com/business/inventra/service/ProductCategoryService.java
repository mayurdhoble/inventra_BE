package com.business.inventra.service;

import com.business.inventra.dto.ProductCategoryDTO;
import com.business.inventra.dto.request.ProductCategoryRequest;
import com.business.inventra.dto.response.ProductCategoryResponse;
import com.business.inventra.exception.DuplicateResourceException;
import com.business.inventra.exception.ResourceNotFoundException;
import com.business.inventra.model.Organization;
import com.business.inventra.model.ProductCategory;
import com.business.inventra.repository.OrganizationRepository;
import com.business.inventra.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Transactional
    public ProductCategoryResponse createProductCategory(ProductCategoryRequest request) {
        // Validate if organization exists
        Organization organization = organizationRepository.findById(request.getOrgId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        // Check if category name already exists for this organization
        if (productCategoryRepository.existsByPCatNameAndOrgId(request.getpCatName(), request.getOrgId())) {
            throw new DuplicateResourceException("Category name already exists for this organization");
        }

        // Generate category code
        String categoryCode = generateCategoryCode(request.getOrgId());

        // Create new category
        ProductCategory category = new ProductCategory();
        category.setPCatCode(categoryCode);
        category.setpCatName(request.getpCatName());
        category.setDescription(request.getDescription());
        category.setOrgId(request.getOrgId());
        
        category = productCategoryRepository.save(category);

        // Create response
        ProductCategoryDTO categoryDTO = new ProductCategoryDTO(
            category.getId(),
            category.getPCatCode(),
            category.getpCatName(),
            category.getDescription(),
            category.getOrgId()
        );

        return new ProductCategoryResponse(categoryDTO);
    }

    private String generateCategoryCode(String orgId) {
        // Get the count of categories for this organization
        long count = productCategoryRepository.countByOrgId(orgId);
        // Generate code in format CAT001, CAT002, etc.
        return String.format("CAT%03d", count + 1);
    }

    public List<ProductCategoryResponse> getAllProductCategories(String orgId) {
        // Validate if organization exists
        if (!organizationRepository.existsById(orgId)) {
            throw new ResourceNotFoundException("Organization not found");
        }

        // Get all categories for the organization
        List<ProductCategory> categories = productCategoryRepository.findByOrgId(orgId);

        // Convert to DTOs
        return categories.stream()
            .map(category -> {
                ProductCategoryDTO categoryDTO = new ProductCategoryDTO(
                    category.getId(),
                    category.getPCatCode(),
                    category.getpCatName(),
                    category.getDescription(),
                    category.getOrgId()
                );
                return new ProductCategoryResponse(categoryDTO);
            })
            .collect(Collectors.toList());
    }

    public ProductCategoryResponse getProductCategoryById(String id) {
        ProductCategory productCategory = productCategoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product category not found"));

        ProductCategoryDTO dto = new ProductCategoryDTO(
        		productCategory.getId(),
        		productCategory.getPCatCode(),
            productCategory.getPCatCode(),
            productCategory.getpCatName(),
            productCategory.getDescription()
        );

        return new ProductCategoryResponse(dto);
    }

    @Transactional
    public ProductCategoryResponse updateProductCategory(String id, ProductCategoryRequest request) {
        // Find and validate category
        ProductCategory category = productCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product category not found"));

        // Validate if category belongs to the organization
        if (!category.getOrgId().equals(request.getOrgId())) {
            throw new ResourceNotFoundException("Product category not found in this organization");
        }

        // Check if new name already exists (excluding current category)
        if (!category.getpCatName().equals(request.getpCatName()) &&
            productCategoryRepository.existsByPCatNameAndOrgId(request.getpCatName(), request.getOrgId())) {
            throw new DuplicateResourceException("Category name already exists for this organization");
        }

        // Update category
        category.setpCatName(request.getpCatName());
        category.setDescription(request.getDescription());

        category = productCategoryRepository.save(category);

        // Create response
        ProductCategoryDTO categoryDTO = new ProductCategoryDTO(
            category.getId(),
            category.getPCatCode(),
            category.getpCatName(),
            category.getDescription(),
            category.getOrgId()
        );

        return new ProductCategoryResponse(categoryDTO);
    }

    @Transactional
    public void deleteProductCategory(String id, String orgId) {
        // Find and validate category
        ProductCategory category = productCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product category not found"));

        // Validate if category belongs to the organization
        if (!category.getOrgId().equals(orgId)) {
            throw new ResourceNotFoundException("Product category not found in this organization");
        }

        // Delete category
        productCategoryRepository.delete(category);
    }

    /**
     * Searches product categories by name within an organization.
     * The search is case-insensitive and uses partial matching.
     *
     * @param orgId The organization ID to search in
     * @param searchTerm The term to search for in category names
     * @return List of ProductCategoryResponse objects containing matching categories
     */
    public List<ProductCategoryResponse> searchProductCategories(String orgId, String searchTerm) {
        // Validate if organization exists
        if (!organizationRepository.existsById(orgId)) {
            throw new ResourceNotFoundException("Organization not found");
        }

        // Search categories
        List<ProductCategory> categories = productCategoryRepository.searchByPCatNameAndOrgId(orgId, searchTerm);

        // Convert to DTOs
        return categories.stream()
            .map(category -> {
                ProductCategoryDTO categoryDTO = new ProductCategoryDTO(
                    category.getId(),
                    category.getPCatCode(),
                    category.getpCatName(),
                    category.getDescription(),
                    category.getOrgId()
                );
                return new ProductCategoryResponse(categoryDTO);
            })
            .collect(Collectors.toList());
    }
} 