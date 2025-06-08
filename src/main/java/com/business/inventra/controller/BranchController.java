package com.business.inventra.controller;

import com.business.inventra.dto.BranchDTO;
import com.business.inventra.dto.request.BranchRequest;
import com.business.inventra.dto.response.BranchResponse;
import com.business.inventra.dto.response.BranchListResponse;
import com.business.inventra.dto.response.ApiResponse;
import com.business.inventra.exception.DuplicateResourceException;
import com.business.inventra.exception.ResourceNotFoundException;
import com.business.inventra.model.Branch;
import com.business.inventra.repository.BranchRepository;
import com.business.inventra.repository.OrganizationRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class BranchController {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @PostMapping("/branches")
    public ResponseEntity<ApiResponse<BranchResponse>> createBranch(@Valid @RequestBody BranchRequest request) {
        // Validate if organization exists
        if (!organizationRepository.existsById(request.getOrgID())) {
            throw new ResourceNotFoundException("Organization not found");
        }

        // Check if branch name already exists for this organization
        if (branchRepository.existsByNameAndOrgID(request.getName(), request.getOrgID())) {
            throw new DuplicateResourceException("Branch name already exists for this organization");
        }

        // Check if GST number is unique
        if (branchRepository.existsByGstNumber(request.getGstNumber())) {
            throw new DuplicateResourceException("GST number already registered");
        }

        // Create new branch
        Branch branch = new Branch();
        branch.setOrgID(request.getOrgID());
        branch.setName(request.getName());
        branch.setGstNumber(request.getGstNumber());
        
        branch = branchRepository.save(branch);

        // Create response
        BranchDTO branchDTO = new BranchDTO(
            branch.getId(),
            branch.getOrgID(),
            branch.getName(),
            branch.getGstNumber()
        );

        BranchResponse response = new BranchResponse(branchDTO);

        return ResponseEntity.ok(ApiResponse.success("Branch created successfully", response));
    }

    @GetMapping("/organizations/{orgId}/branches")
    public ResponseEntity<ApiResponse<BranchListResponse>> getAllBranches(@PathVariable String orgId) {
        // Validate if organization exists
        if (!organizationRepository.existsById(orgId)) {
            throw new ResourceNotFoundException("Organization not found");
        }

        // Get all branches for the organization
        List<Branch> branches = branchRepository.findByOrgID(orgId);

        // Convert to DTOs
        List<BranchDTO> branchDTOs = branches.stream()
            .map(branch -> new BranchDTO(
                branch.getId(),
                branch.getOrgID(),
                branch.getName(),
                branch.getGstNumber()
            ))
            .collect(Collectors.toList());

        BranchListResponse response = new BranchListResponse(branchDTOs);

        return ResponseEntity.ok(ApiResponse.success("Branches retrieved successfully", response));
    }
} 