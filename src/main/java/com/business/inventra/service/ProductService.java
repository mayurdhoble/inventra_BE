package com.business.inventra.service;

import com.business.inventra.dto.ProductCategoryDTO;
import com.business.inventra.dto.ProductDTO;
import com.business.inventra.dto.request.ProductRequest;
import com.business.inventra.dto.response.ProductResponse;
import com.business.inventra.exception.ResourceNotFoundException;
import com.business.inventra.model.Organization;
import com.business.inventra.model.Product;
import com.business.inventra.model.ProductCategory;
import com.business.inventra.repository.OrganizationRepository;
import com.business.inventra.repository.ProductCategoryRepository;
import com.business.inventra.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        
        // Validate organization
        Organization organization = organizationRepository.findById(request.getOrgId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        // Validate product category
        ProductCategory category = productCategoryRepository.findByPCatCodeAndOrgId(request.getPCatCode(), request.getOrgId())
                .orElseThrow(() -> new ResourceNotFoundException("Product category not found with code: " + request.getPCatCode() + " for organization: " + request.getOrgId()));

        // Create new product
        Product product = new Product();
        product.setPName(request.getPName());
        product.setPCatCode(request.getPCatCode());
        product.setDescription(request.getDescription());
        product.setIsActive(request.getIsActive());
        product.setOrgId(request.getOrgId());
        product.setProductCategory(category);
        product.setOrganization(organization);

        // Generate product code
        String productCode = generateProductCode(request.getOrgId());
        product.setPCode(productCode);

        // Save and return
        Product savedProduct = productRepository.save(product);
        return new ProductResponse(convertToDTO(savedProduct));
    }

    private String generateProductCode(String orgId) {
        // Get the count of products for this organization
        long count = productRepository.countByOrgId(orgId);
        // Generate code in format PRD001, PRD002, etc.
        return String.format("PRD%03d", count + 1);
    }

    public List<ProductResponse> getAllProducts(String orgId) {
        List<Product> products = productRepository.findByOrgId(orgId);
        return products.stream()
                .map(this::convertToDTO)
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsByCategory(String orgId, String categoryId) {
        List<Product> products = productRepository.findBypCatCodeAndOrgId(categoryId, orgId);
        return products.stream()
                .map(this::convertToDTO)
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> searchProducts(String orgId, String searchTerm) {
        List<Product> products = productRepository.searchByPNameAndOrgId(orgId, searchTerm);
        return products.stream()
                .map(this::convertToDTO)
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return new ProductResponse(convertToDTO(product));
    }

    @Transactional
    public ProductResponse updateProduct(String id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Validate organization
        if (!product.getOrgId().equals(request.getOrgId())) {
            throw new ResourceNotFoundException("Product does not belong to the specified organization");
        }

        // Validate product category if changed
        if (!product.getPCatCode().equals(request.getPCatCode())) {
            ProductCategory category = productCategoryRepository.findByPCatCodeAndOrgId(request.getPCatCode(), request.getOrgId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product category not found"));
            product.setProductCategory(category);
        }

        // Update fields
        product.setPName(request.getPName());
        product.setPCatCode(request.getPCatCode());
        product.setDescription(request.getDescription());
        product.setIsActive(request.getIsActive());

        // Save and return
        Product updatedProduct = productRepository.save(product);
        return new ProductResponse(convertToDTO(updatedProduct));
    }

    @Transactional
    public void deleteProduct(String id, String orgId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.getOrgId().equals(orgId)) {
            throw new ResourceNotFoundException("Product does not belong to the specified organization");
        }

        productRepository.delete(product);
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setPCode(product.getPCode());
        dto.setPName(product.getPName());
        dto.setPCatCode(product.getPCatCode());
        dto.setDescription(product.getDescription());
        dto.setIsActive(product.getIsActive());
        dto.setOrgId(product.getOrgId());
        
        // Set category details if available
        if (product.getProductCategory() != null) {
            ProductCategory category = product.getProductCategory();
            ProductCategoryDTO categoryDTO = new ProductCategoryDTO(
                category.getId(),
                category.getPCatCode(),
                category.getpCatName(),
                category.getDescription(),
                category.getOrgId()
            );
            dto.setCategory(categoryDTO);
        }
        
        return dto;
    }
} 