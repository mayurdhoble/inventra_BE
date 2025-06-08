package com.business.inventra.service;

import com.business.inventra.dto.BranchDTO;
import com.business.inventra.dto.OrgDTO;
import com.business.inventra.dto.UserDTO;
import com.business.inventra.dto.request.SignUpRequest;
import com.business.inventra.dto.response.SignUpResponse;
import com.business.inventra.model.Organization;
import com.business.inventra.model.Role;
import com.business.inventra.model.User;
import com.business.inventra.model.UserBranchMapping;
import com.business.inventra.repository.OrganizationRepository;
import com.business.inventra.repository.RoleRepository;
import com.business.inventra.repository.UserRepository;
import com.business.inventra.repository.UserBranchMappingRepository;
import com.business.inventra.repository.BranchRepository;
import com.business.inventra.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.business.inventra.exception.ResourceNotFoundException;
import com.business.inventra.exception.DuplicateResourceException;
import com.business.inventra.model.Branch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserBranchMappingRepository userBranchMappingRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Transactional(rollbackOn = Exception.class)
    public SignUpResponse registerOrganizationWithAdmin(SignUpRequest request) {
        // Check if organization email already exists
        if (organizationRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Organization with email " + request.getEmail() + " already exists");
        }
        
        Role role = roleRepository.findByName("ORG_ADMIN")
                .orElseThrow(() -> new ResourceNotFoundException("Default role ORG_ADMIN not found"));
                
        // Save organization
        Organization organization = new Organization();
        organization.setName(request.getOrgName());
        organization.setEmail(request.getEmail());
        organization = organizationRepository.save(organization);

        // Save admin user
        User adminUser = new User();
        adminUser.setFirstName(request.getFirstName());
        adminUser.setLastName(request.getLastName());
        adminUser.setUserName(request.getUserName());
        adminUser.setEmail(request.getEmail());
        adminUser.setPassword(passwordEncoder.encode(request.getPassword()));
        adminUser.setRoleID(role.getId());
        adminUser.setStatus("Y");
        adminUser = userRepository.save(adminUser);

        // Create user-organization mapping (no branch assignment)
        UserBranchMapping mapping = new UserBranchMapping();
        mapping.setUser(adminUser);
        mapping.setOrgID(organization.getId());
        mapping = userBranchMappingRepository.save(mapping);
        
        // Create empty branches list since this is an org admin without branch assignment
        List<BranchDTO> branches = new ArrayList<>();
        
        // Create UserDTO
        UserDTO userDTO = new UserDTO(
            adminUser.getId(),
            adminUser.getEmail(),
            adminUser.getUserName(),
            adminUser.getFirstName(),
            adminUser.getMiddleName(),
            adminUser.getLastName(),
            role.getName(),
            branches,  // Empty branches list for org admin
            null,      // No addresses for org admin
            null       // No contacts for org admin
        );

        // Generate JWT token
        String token = jwtUtil.generateToken(adminUser, organization);

        // Create response
        SignUpResponse response = new SignUpResponse(token, userDTO, new OrgDTO(organization.getId(), organization.getName()));
        logger.info("Created response: {}", response);
        return response;
    }
}
