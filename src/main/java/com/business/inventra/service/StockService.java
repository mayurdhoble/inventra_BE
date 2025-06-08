package com.business.inventra.service;

import com.business.inventra.dto.request.StockRequest;
import com.business.inventra.dto.response.ProductDetails;
import com.business.inventra.dto.response.StockResponse;
import com.business.inventra.dto.response.UnitDetails;
import com.business.inventra.exception.ResourceNotFoundException;
import com.business.inventra.model.*;
import com.business.inventra.repository.*;
import com.business.inventra.util.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockMaintenanceRepository stockMaintenanceRepository;

    @Autowired
    private StockAuditRepository stockAuditRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ProductPricingRepository productPricingRepository;

    @Autowired
    private UserBranchMappingRepository userBranchRepository;

    @Autowired
    private ProductUnitDetailsRepository productUnitDetailsRepository;

    @Transactional
    public StockResponse updateStock(StockRequest request) {
        // Validate organization
        Organization organization = organizationRepository.findById(request.getOrgId())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        // Validate product
        Product product = productRepository.findBypCodeAndOrgId(request.getPCode(), request.getOrgId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Validate branch
        Branch branch = branchRepository.findByIdAndOrgID(request.getBranchId(), request.getOrgId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        // Validate unit - only check by unit_code
        Unit unit = unitRepository.findById(request.getPUnit())
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found with code: " + request.getPUnit()));

        // Validate user and check branch access
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check if user has access to the branch
        boolean hasBranchAccess = userBranchRepository.existsByUserIdAndBranchId(request.getUserId(), request.getBranchId());
        if (!hasBranchAccess) {
            throw new ResourceNotFoundException("User does not have access to the specified branch");
        }

        // Check if product pricing already exists for this combination
        boolean pricingExists = productPricingRepository.existsBypCodeAndpUnitAndEffectiveDate(
            request.getPCode(), 
            request.getPUnit(), 
            request.getEffectiveDate()
        );

        if (pricingExists) {
            throw new ResourceNotFoundException("Cannot add price for the stock as pricing already exists for this product, unit, and effective date");
        }

        // Create or update product pricing
        ProductPricing pricing = new ProductPricing();
        pricing.setPCode(request.getPCode());
        pricing.setPUnit(request.getPUnit());
        pricing.setPricePerUnit(request.getPPrice());
        pricing.setEffectiveDate(request.getEffectiveDate());
        pricing.setIsDefault(true);
        pricing.setOrgId(request.getOrgId());
        productPricingRepository.save(pricing);

        // Create or update product unit details
        boolean productUnitExists = productUnitDetailsRepository.existsBypCodeAndpUnitAndOrgId(
            request.getPCode(), 
            request.getPUnit(),
            request.getOrgId()
        );

        if (!productUnitExists) {
            ProductUnitDetails newDetails = new ProductUnitDetails();
            newDetails.setPCode(request.getPCode());
            newDetails.setPUnit(request.getPUnit());
            newDetails.setDefault(true);
            newDetails.setProduct(product);
            newDetails.setUnit(unit);
            newDetails.setOrganization(organization);
            productUnitDetailsRepository.save(newDetails);
        }

        // Get or create stock maintenance
        StockMaintenance stockMaintenance = stockMaintenanceRepository
                .findByPCodeAndBranchIdAndPUnitAndOrgId(
                        request.getPCode(),
                        request.getBranchId(),
                        request.getPUnit(),
                        request.getOrgId())
                .orElseGet(() -> {
                    StockMaintenance newStock = new StockMaintenance();
                    newStock.setPCode(request.getPCode());
                    newStock.setBranchId(request.getBranchId());
                    newStock.setPUnit(request.getPUnit());
                    newStock.setOrgId(request.getOrgId());
                    newStock.setCurrentStock(BigDecimal.ZERO);
                    return newStock;
                });

        // Update stock
        BigDecimal currentStock = stockMaintenance.getCurrentStock();
        BigDecimal newStock = request.getOperationType().equalsIgnoreCase("IN")
                ? currentStock.add(request.getPQuantity())
                : currentStock.subtract(request.getPQuantity());

        if (newStock.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient stock");
        }

        stockMaintenance.setCurrentStock(newStock);
        stockMaintenance.setLastUpdatedAt(LocalDateTime.now());
        stockMaintenanceRepository.save(stockMaintenance);

        // Create stock audit
        StockAudit stockAudit = new StockAudit();
        stockAudit.setPCode(request.getPCode());
        stockAudit.setBranchId(request.getBranchId());
        stockAudit.setPUnit(request.getPUnit());
        stockAudit.setQuantity(request.getPQuantity());
        stockAudit.setOperationType(StockAudit.OperationType.valueOf(request.getOperationType()));
        stockAudit.setOperationDate(LocalDateTime.now());
        stockAudit.setOperatorName(user.getFullName());
        stockAudit.setInvoiceNumber(request.getInvoiceNumber());
        stockAudit.setUserId(request.getUserId());
        stockAudit.setRemarks(request.getRemark());
        stockAudit.setOrgId(request.getOrgId());
        stockAuditRepository.save(stockAudit);

        // Create response
        StockResponse response = new StockResponse();
        response.setBranchId(request.getBranchId());
        response.setCurrentStock(newStock);
        response.setLastUpdatedAt(stockMaintenance.getLastUpdatedAt());
        response.setOperationType(request.getOperationType());
        response.setQuantity(request.getPQuantity());
        response.setOperatorName(user.getFullName());
        response.setInvoiceNumber(request.getInvoiceNumber());
        response.setRemarks(request.getRemark());

        // Set product details
        ProductDetails productDetails = new ProductDetails();
        productDetails.setPCode(product.getPCode());
        productDetails.setPName(product.getPName());
        productDetails.setPCategory(product.getPCatCode());
        productDetails.setPCategoryName(product.getPName());
        response.setProduct(productDetails);

        // Set unit details
        UnitDetails unitDetails = new UnitDetails();
        unitDetails.setId(unit.getId());
        unitDetails.setUnitCode(unit.getUnitCode());
        unitDetails.setUnitDesc(unit.getUnitDesc());
        response.setUnit(unitDetails);

        return response;
    }
} 