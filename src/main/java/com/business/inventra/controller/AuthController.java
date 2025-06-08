package com.business.inventra.controller;

import com.business.inventra.dto.AddressDTO;
import com.business.inventra.dto.BranchDTO;
import com.business.inventra.dto.ContactDTO;
import com.business.inventra.dto.OrgDTO;
import com.business.inventra.dto.UserDTO;
import com.business.inventra.dto.request.AuthRequest;
import com.business.inventra.dto.request.SignUpRequest;
import com.business.inventra.dto.response.ApiResponse;
import com.business.inventra.dto.response.SignUpResponse;
import com.business.inventra.exception.DuplicateResourceException;
import com.business.inventra.exception.ResourceNotFoundException;
import com.business.inventra.exception.AuthenticationFailedException;
import com.business.inventra.model.Organization;
import com.business.inventra.model.Role;
import com.business.inventra.model.User;
import com.business.inventra.model.UserBranchMapping;
import com.business.inventra.repository.OrganizationRepository;
import com.business.inventra.repository.RoleRepository;
import com.business.inventra.repository.UserRepository;
import com.business.inventra.repository.UserBranchMappingRepository;
import com.business.inventra.service.AuthService;
import com.business.inventra.service.CustomUserDetailsService;
import com.business.inventra.service.UserService;
import com.business.inventra.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.business.inventra.model.Branch;
import com.business.inventra.repository.BranchRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserBranchMappingRepository userBranchMappingRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponse>> registerOrganizationWithAdmin(@RequestBody SignUpRequest request) {
        SignUpResponse response = authService.registerOrganizationWithAdmin(request);
        return ResponseEntity.ok(ApiResponse.success("Organization and admin created successfully.", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<SignUpResponse>> login(@Valid @RequestBody AuthRequest request) {
        try {
            // Try to find user by email or username
            User user = userRepository.findByEmailOrUserName(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new AuthenticationFailedException("Invalid credentials"));

            // Authenticate using the found user's username
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Get user's organization
            UserBranchMapping mapping = userBranchMappingRepository.findByUserId(user.getId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User organization mapping not found"));

            Organization organization = organizationRepository.findById(mapping.getOrgID())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

            // Get user's role
            Role role = roleRepository.findById(user.getRoleID())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

            // Get user's branch IDs
            List<String> branchIDs = userBranchMappingRepository.findByUserId(user.getId())
                .stream()
                .map(UserBranchMapping::getBranchID)
                .filter(branchId -> branchId != null)
                .collect(Collectors.toList());
            
         // Fetch complete branch information
            List<BranchDTO> branches = branchIDs.stream()
                .map(bid -> {
                    Branch branch = branchRepository.findById(bid)
                        .orElseThrow(() -> new ResourceNotFoundException("Branch not found: " + bid));
                    return new BranchDTO(
                        branch.getId(),
                        branch.getOrgID(),
                        branch.getName(),
                        branch.getGstNumber()
                    );
                })
                .collect(Collectors.toList());

            // Get addresses and contacts
            List<AddressDTO> addresses = userService.getAddressesForUser(user.getId());
            List<ContactDTO> contacts = userService.getContactsForUser(user.getId());

            // Create UserDTO
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setEmail(user.getEmail());
            userDTO.setUserName(user.getUserName());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setMiddleName(user.getMiddleName());
            userDTO.setLastName(user.getLastName());
            userDTO.setRoleName(role.getName());
            userDTO.setBranches(branches);
            userDTO.setAddresses(role.getName().equals("ORG_USER") ? null : addresses);
            userDTO.setContacts(role.getName().equals("ORG_USER") ? null : contacts);

            // Generate JWT token
            String token = jwtUtil.generateToken(user, organization);

            // Create response
            SignUpResponse response = new SignUpResponse(token, userDTO, new OrgDTO(organization.getId(), organization.getName()));
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));

        } catch (AuthenticationException e) {
            throw new AuthenticationFailedException("Invalid credentials");
        }
    }

    @PostMapping(value = "/signup", produces = "application/json")
    public ResponseEntity<ApiResponse<SignUpResponse>> registerUser(@Valid @RequestBody SignUpRequest request) {
        if (organizationRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Organization email is already registered");
        }

        if (userRepository.existsByUserName(request.getUserName())) {
            throw new DuplicateResourceException("Username is already taken");
        }

        SignUpResponse response = authService.registerOrganizationWithAdmin(request);
        return ResponseEntity.ok(ApiResponse.success("Organization and admin created successfully.", response));
    }
}