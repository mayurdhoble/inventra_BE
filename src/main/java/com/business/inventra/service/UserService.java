package com.business.inventra.service;

import com.business.inventra.constant.EntityType;
import com.business.inventra.dto.AddressDTO;
import com.business.inventra.dto.BranchDTO;
import com.business.inventra.dto.ContactDTO;
import com.business.inventra.dto.UserDTO;
import com.business.inventra.dto.request.UserRequest;
import com.business.inventra.dto.response.UserResponse;
import com.business.inventra.exception.DuplicateResourceException;
import com.business.inventra.exception.ResourceNotFoundException;
import com.business.inventra.model.*;
import com.business.inventra.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private AddressCategoryRepository addressCategoryRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserBranchMappingRepository userBranchMappingRepository;

    @Transactional
    public UserResponse createUser(UserRequest request) {
        // Validate unique constraints
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        if (userBranchMappingRepository.existsByUserNameAndOrgID(request.getUserName(), request.getOrgID())) {
            throw new DuplicateResourceException("Username already exists for this organization");
        }

        // Validate phone numbers for users only
        for (ContactDTO contactDTO : request.getContacts()) {
            if (contactDTO.getIsPrimary() && contactRepository.findByMobileNumberAndTypeAndOrgId(
                contactDTO.getPhoneNumber(),
                EntityType.USER,
                request.getOrgID()
            ).stream().anyMatch(Contact::getIsPrimary)) {
                throw new DuplicateResourceException("Phone number already exists for another user in this organization: " + contactDTO.getPhoneNumber());
            }
        }

        // Validate organization
        Organization organization = organizationRepository.findById(request.getOrgID())
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        // Validate role
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        // Validate branches if provided
        if (request.getBranchIDs() != null && !request.getBranchIDs().isEmpty()) {
            for (String branchId : request.getBranchIDs()) {
                branchRepository.findByIdAndOrgID(branchId, request.getOrgID())
                        .orElseThrow(() -> new ResourceNotFoundException("Branch not found or does not belong to the organization: " + branchId));
            }
        }

        // Create user
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setUserName(request.getUserName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoleID(role.getId());
        user.setStatus("Y"); // Set status to active (Y)

        user = userRepository.save(user);

        // Create user-branch mappings
        List<UserBranchMapping> mappings = new ArrayList<>();
        if (request.getBranchIDs() != null && !request.getBranchIDs().isEmpty()) {
            for (String branchId : request.getBranchIDs()) {
                UserBranchMapping mapping = new UserBranchMapping();
                mapping.setUser(user);
                mapping.setBranchID(branchId);
                mapping.setOrgID(request.getOrgID());
                mappings.add(userBranchMappingRepository.save(mapping));
            }
        } else {
            // If no branches specified, create a default mapping with just orgID
            UserBranchMapping mapping = new UserBranchMapping();
            mapping.setUser(user);
            mapping.setOrgID(request.getOrgID());
            mappings.add(userBranchMappingRepository.save(mapping));
        }

        // Create and save addresses
        List<Address> addresses = new ArrayList<>();
        for (AddressDTO addressDTO : request.getAddresses()) {
            // Validate address category
            AddressCategory addressCategory = addressCategoryRepository.findByName(addressDTO.getCategoryCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid address category: " + addressDTO.getCategoryCode()));

            Address address = new Address();
            address.setCategoryCode(addressCategory.getId());
            address.setLinkageId(user.getId());
            address.setLinkageType(EntityType.USER);
            address.setLine1(addressDTO.getLine1());
            address.setLine2(addressDTO.getLine2());
            address.setLine3(addressDTO.getLine3());
            address.setCountry(addressDTO.getCountry());
            address.setState(addressDTO.getState());
            address.setCity(addressDTO.getCity());
            address.setPostalCode(addressDTO.getPostalCode());
            address.setIsActive(addressDTO.getIsActive());
            address.setIsCurrent(addressDTO.getIsCurrent());
            address.setType(addressDTO.getType());
            addresses.add(addressRepository.save(address));
        }

        // Create and save contacts
        List<Contact> contacts = new ArrayList<>();
        boolean hasPrimaryContact = false;
        for (ContactDTO contactDTO : request.getContacts()) {
            // Validate contact category
            AddressCategory addressCategory = addressCategoryRepository.findByName(contactDTO.getCategoryCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid contact category: " + contactDTO.getCategoryCode()));

            // Ensure only one primary contact
            if (contactDTO.getIsPrimary() && hasPrimaryContact) {
                throw new DuplicateResourceException("Only one primary contact is allowed");
            }
            if (contactDTO.getIsPrimary()) {
                hasPrimaryContact = true;
            }

            Contact contact = createContact(contactDTO, user, addressCategory);
            contacts.add(contactRepository.save(contact));
        }

        // Fetch complete branch information
        List<BranchDTO> branches = mappings.stream()
            .map(bid -> {
                Branch branch = branchRepository.findById(bid.getBranchID())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found: " + bid));
                return new BranchDTO(
                    branch.getId(),
                    branch.getOrgID(),
                    branch.getName(),
                    branch.getGstNumber()
                );
            })
            .collect(Collectors.toList());

        // Create UserDTO with all information
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setUserName(user.getUserName());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setMiddleName(user.getMiddleName());
        userDTO.setLastName(user.getLastName());
        userDTO.setRoleName(role.getName());
        userDTO.setBranches(branches);
        userDTO.setAddresses(role.getName().equals("ORG_USER") ? null : addresses.stream()
            .map(address -> new AddressDTO(
                address.getId(),
                address.getCategoryCode(),
                address.getLine1(),
                address.getLine2(),
                address.getLine3(),
                address.getCountry(),
                address.getState(),
                address.getCity(),
                address.getPostalCode(),
                address.getIsActive(),
                address.getIsCurrent(),
                address.getType()
            ))
            .collect(Collectors.toList()));
        userDTO.setContacts(role.getName().equals("ORG_USER") ? null : convertToContactDTOs(contacts));

        return new UserResponse(userDTO);
    }

    public UserResponse getUserById(String id, String orgId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if ("N".equals(user.getStatus())) {
            throw new ResourceNotFoundException("User is inactive");
        }

        // Validate if user belongs to the organization
        if (!userBranchMappingRepository.existsByUserIdAndOrgID(id, orgId)) {
            throw new ResourceNotFoundException("User not found in this organization");
        }

        Role role = roleRepository.findById(user.getRoleID())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        // Get branch IDs from mappings
        List<String> branchIDs = userBranchMappingRepository.findByUserIdAndOrgID(id, orgId)
            .stream()
            .map(UserBranchMapping::getBranchID)
            .filter(branchid -> branchid != null)
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

        // Fetch addresses and contacts
        List<AddressDTO> addresses = addressRepository.findByLinkageId(id).stream()
                .map(address -> new AddressDTO(
                    address.getId(),
                    address.getCategoryCode(),
                    address.getLine1(),
                    address.getLine2(),
                    address.getLine3(),
                    address.getCountry(),
                    address.getState(),
                    address.getCity(),
                    address.getPostalCode(),
                    address.getIsActive(),
                    address.getIsCurrent(),
                    address.getType()
                ))
                .collect(Collectors.toList());

        List<ContactDTO> contacts = contactRepository.findByLinkageId(id).stream()
                .map(contact -> new ContactDTO(
                    contact.getId(),
                    contact.getCategoryCode(),
                    contact.getDialCode(),
                    contact.getPhoneNumber(),
                    contact.getIsPrimary(),
                    contact.isActive()
                ))
                .collect(Collectors.toList());

        // Create UserDTO
        UserDTO userDTO = new UserDTO(
            user.getId(),
            user.getEmail(),
            user.getUserName(),
            user.getFirstName(),
            user.getMiddleName(),
            user.getLastName(),
            role.getName(),
            branches,
            role.getName().equals("ORG_USER") ? null : addresses,
            role.getName().equals("ORG_USER") ? null : contacts
        );

        return new UserResponse(userDTO);
    }

    public List<UserDTO> getAllUsers(String orgId, String branchId) {
        List<User> users;
        if (branchId != null) {
            // Get users from mappings for specific branch
            users = userBranchMappingRepository.findByOrgIDAndBranchID(orgId, branchId)
                .stream()
                .map(UserBranchMapping::getUser)
                .filter(user -> "Y".equals(user.getStatus()))
                .collect(Collectors.toList());
        } else {
            // Get all users for the organization
            users = userBranchMappingRepository.findByOrgID(orgId)
                .stream()
                .map(UserBranchMapping::getUser)
                .filter(user -> "Y".equals(user.getStatus()))
                .collect(Collectors.toList());
        }

        return users.stream()
                .map(user -> {
                    Role role = roleRepository.findById(user.getRoleID())
                            .orElseThrow(() -> new ResourceNotFoundException("Role not found for user: " + user.getId()));

                    // Get branch IDs from mappings
                    List<String> branchIDs = userBranchMappingRepository.findByUserIdAndOrgID(user.getId(), orgId)
                        .stream()
                        .map(UserBranchMapping::getBranchID)
                        .filter(id -> id != null)
                        .collect(Collectors.toList());

                    // Fetch complete branch information
                    List<BranchDTO> branches = branchIDs.stream()
                        .map(id -> {
                            Branch branch = branchRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Branch not found: " + id));
                            return new BranchDTO(
                                branch.getId(),
                                branch.getOrgID(),
                                branch.getName(),
                                branch.getGstNumber()
                            );
                        })
                        .collect(Collectors.toList());

                    // Fetch addresses and contacts
                    List<AddressDTO> addresses = addressRepository.findByLinkageId(user.getId()).stream()
                            .map(address -> new AddressDTO(
                                address.getId(),
                                address.getCategoryCode(),
                                address.getLine1(),
                                address.getLine2(),
                                address.getLine3(),
                                address.getCountry(),
                                address.getState(),
                                address.getCity(),
                                address.getPostalCode(),
                                address.getIsActive(),
                                address.getIsCurrent(),
                                address.getType()
                            ))
                            .collect(Collectors.toList());

                    List<ContactDTO> contacts = contactRepository.findByLinkageId(user.getId()).stream()
                            .map(contact -> new ContactDTO(
                                contact.getId(),
                                contact.getCategoryCode(),
                                contact.getDialCode(),
                                contact.getPhoneNumber(),
                                contact.getIsPrimary(),
                                contact.isActive()
                            ))
                            .collect(Collectors.toList());

                    return new UserDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getUserName(),
                        user.getFirstName(),
                        user.getMiddleName(),
                        user.getLastName(),
                        role.getName(),
                        branches,
                        role.getName().equals("ORG_USER") ? null : addresses,
                        role.getName().equals("ORG_USER") ? null : contacts
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateUser(String id, UserRequest request) {
        // 1. Fetch and validate user
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // 2. Fetch and validate role
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        // 3. Validate unique constraints
        validateUniqueConstraints(user, request);

        // 4. Validate primary contact
        validatePrimaryContact(request);

        // 5. Validate branches if provided
        if (request.getBranchIDs() != null && !request.getBranchIDs().isEmpty()) {
            for (String branchId : request.getBranchIDs()) {
                branchRepository.findByIdAndOrgID(branchId, request.getOrgID())
                        .orElseThrow(() -> new ResourceNotFoundException("Branch not found or does not belong to the organization: " + branchId));
            }
        }

        // 6. Update user fields
        updateUserFields(user, request, role);
        user = userRepository.save(user);

        // 7. Update user-branch mappings
        userBranchMappingRepository.deleteByUserId(user.getId());
        List<UserBranchMapping> mappings = new ArrayList<>();
        if (request.getBranchIDs() != null && !request.getBranchIDs().isEmpty()) {
            for (String branchId : request.getBranchIDs()) {
                UserBranchMapping mapping = new UserBranchMapping();
                mapping.setUser(user);
                mapping.setBranchID(branchId);
                mapping.setOrgID(request.getOrgID());
                mappings.add(userBranchMappingRepository.save(mapping));
            }
        } else {
            // If no branches specified, create a default mapping with just orgID
            UserBranchMapping mapping = new UserBranchMapping();
            mapping.setUser(user);
            mapping.setOrgID(request.getOrgID());
            mappings.add(userBranchMappingRepository.save(mapping));
        }

        // 8. Update addresses
        List<Address> addresses = updateAddresses(user.getId(), request.getAddresses());

        // 9. Update contacts
        List<Contact> contacts = updateContacts(user.getId(), request.getContacts());

     // Fetch complete branch information
        List<BranchDTO> branches = mappings.stream()
            .map(bid -> {
                Branch branch = branchRepository.findById(bid.getBranchID())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found: " + bid));
                return new BranchDTO(
                    branch.getId(),
                    branch.getOrgID(),
                    branch.getName(),
                    branch.getGstNumber()
                );
            })
            .collect(Collectors.toList());

        // 11. Create response
        return createUserResponse(user, role, addresses, contacts, branches);
    }

    private void validateUniqueConstraints(User user, UserRequest request) {
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        if (!user.getUserName().equals(request.getUserName()) && 
            userBranchMappingRepository.existsByUserNameAndOrgID(request.getUserName(), request.getOrgID())) {
            throw new DuplicateResourceException("Username already exists for this organization");
        }

        // Validate phone numbers for users only
        for (ContactDTO contactDTO : request.getContacts()) {
            if (contactDTO.getIsPrimary() && contactRepository.findByMobileNumberAndTypeAndOrgId(
                contactDTO.getPhoneNumber(),
                EntityType.USER,
                request.getOrgID()
            ).stream().anyMatch(Contact::getIsPrimary)) {
                throw new DuplicateResourceException("Primary phone number already exists for another user in this organization: " + contactDTO.getPhoneNumber());
            }
        }
    }

    private void validatePrimaryContact(UserRequest request) {
        long primaryContactCount = request.getContacts().stream()
                .filter(ContactDTO::getIsPrimary)
                .count();
        if (primaryContactCount > 1) {
            throw new DuplicateResourceException("Only one primary contact is allowed");
        }
    }

    private void updateUserFields(User user, UserRequest request, Role role) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setUserName(request.getUserName());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setRoleID(role.getId());
        user.setStatus("Y"); // Keep user active on update
    }

    private List<Address> updateAddresses(String userId, List<AddressDTO> addressDTOs) {
        addressRepository.deleteByLinkageId(userId);
        return addressDTOs.stream()
                .map(addressDTO -> {
                    Address address = new Address();
                    address.setCategoryCode(addressDTO.getCategoryCode());
                    address.setLinkageId(userId);
                    address.setLinkageType(EntityType.USER);
                    address.setLine1(addressDTO.getLine1());
                    address.setLine2(addressDTO.getLine2());
                    address.setLine3(addressDTO.getLine3());
                    address.setCountry(addressDTO.getCountry());
                    address.setState(addressDTO.getState());
                    address.setCity(addressDTO.getCity());
                    address.setPostalCode(addressDTO.getPostalCode());
                    address.setIsActive(addressDTO.getIsActive());
                    address.setIsCurrent(addressDTO.getIsCurrent());
                    address.setType(addressDTO.getType());
                    return addressRepository.save(address);
                })
                .collect(Collectors.toList());
    }

    private List<Contact> updateContacts(String userId, List<ContactDTO> contactDTOs) {
        contactRepository.deleteByLinkageId(userId);
        return contactDTOs.stream()
                .map(contactDTO -> {
                    // Get address category
                    AddressCategory addressCategory = addressCategoryRepository.findByName(contactDTO.getCategoryCode())
                            .orElseThrow(() -> new ResourceNotFoundException("Invalid contact category: " + contactDTO.getCategoryCode()));
                    
                    Contact contact = createContact(contactDTO, userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found")), addressCategory);
                    return contactRepository.save(contact);
                })
                .collect(Collectors.toList());
    }

    private Contact createContact(ContactDTO contactDTO, User user, AddressCategory addressCategory) {
        Contact contact = new Contact();
        contact.setCategoryCode(addressCategory.getId());
        contact.setDialCode(contactDTO.getDialCode());
        contact.setPhoneNumber(contactDTO.getPhoneNumber());
        contact.setPrimary(contactDTO.getIsPrimary());
        contact.setActive(contactDTO.getIsActive());
        contact.setLinkageId(user.getId());
        contact.setLinkageType(EntityType.USER);
        return contact;
    }

    private List<ContactDTO> convertToContactDTOs(List<Contact> contacts) {
        return contacts.stream()
                .map(contact -> new ContactDTO(
                        contact.getId(),
                        contact.getCategoryCode(),
                        contact.getDialCode(),
                        contact.getPhoneNumber(),
                        contact.getIsPrimary(),
                        contact.isActive()))
                .collect(Collectors.toList());
    }

    UserResponse createUserResponse(User user, Role role, List<Address> addresses, List<Contact> contacts, List<BranchDTO> branches) {
        List<AddressDTO> addressDTOs = addresses.stream()
                .map(address -> new AddressDTO(
                    address.getId(),
                    address.getCategoryCode(),
                    address.getLine1(),
                    address.getLine2(),
                    address.getLine3(),
                    address.getCountry(),
                    address.getState(),
                    address.getCity(),
                    address.getPostalCode(),
                    address.getIsActive(),
                    address.getIsCurrent(),
                    address.getType()
                ))
                .collect(Collectors.toList());

        List<ContactDTO> contactDTOs = convertToContactDTOs(contacts);

        // Create UserDTO with all information
        UserDTO userDTO = new UserDTO(
            user.getId(),
            user.getEmail(),
            user.getUserName(),
            user.getFirstName(),
            user.getMiddleName(),
            user.getLastName(),
            role.getName(),
            branches,
            role.getName().equals("ORG_USER") ? null : addressDTOs,
            role.getName().equals("ORG_USER") ? null : contactDTOs
        );

        return new UserResponse(userDTO);
    }

    @Transactional
    public void deleteUser(String id, String orgId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Validate if user belongs to the organization
        if (!userBranchMappingRepository.existsByUserIdAndOrgID(id, orgId)) {
            throw new ResourceNotFoundException("User not found in this organization");
        }
        
        // Delete user-branch mappings
        userBranchMappingRepository.deleteByUserId(id);
        
        user.setStatus("N"); // Soft delete by setting status to N
        userRepository.save(user);
    }

    public List<AddressDTO> getAddressesForUser(String userId) {
        return addressRepository.findByLinkageId(userId).stream()
                .map(address -> new AddressDTO(
                    address.getId(),
                    address.getCategoryCode(),
                    address.getLine1(),
                    address.getLine2(),
                    address.getLine3(),
                    address.getCountry(),
                    address.getState(),
                    address.getCity(),
                    address.getPostalCode(),
                    address.getIsActive(),
                    address.getIsCurrent(),
                    address.getType()
                ))
                .collect(Collectors.toList());
    }

    public List<ContactDTO> getContactsForUser(String userId) {
        return contactRepository.findByLinkageId(userId).stream()
                .map(contact -> new ContactDTO(
                    contact.getId(),
                    contact.getCategoryCode(),
                    contact.getDialCode(),
                    contact.getPhoneNumber(),
                    contact.getIsPrimary(),
                    contact.isActive()
                ))
                .collect(Collectors.toList());
    }
} 