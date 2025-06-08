package com.business.inventra.service;

import com.business.inventra.constant.EntityType;
import com.business.inventra.dto.AddressDTO;
import com.business.inventra.dto.ContactDTO;
import com.business.inventra.dto.CustomerDTO;
import com.business.inventra.dto.request.CustomerRequest;
import com.business.inventra.dto.response.CustomerResponse;
import com.business.inventra.exception.DuplicateResourceException;
import com.business.inventra.exception.ResourceNotFoundException;
import com.business.inventra.model.*;
import com.business.inventra.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private AddressCategoryRepository addressCategoryRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private CustomerBranchMappingRepository customerBranchMappingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BranchRepository branchRepository;

    /**
     * Creates a new customer or retrieves an existing one based on the provided request.
     * If a customer with the same mobile number exists, returns the existing customer.
     * If a user with the same mobile number exists, creates a customer from the user.
     * Otherwise, creates a new customer with the provided details.
     *
     * @param request The customer creation request containing all necessary details
     * @return CustomerResponse containing the created or existing customer details
     */
    @Transactional(rollbackOn = Exception.class)
    public CustomerResponse createCustomer(CustomerRequest request) {
        // Validate organization
        validateOrganization(request.getOrgID());

        // Validate branch if provided
        if (request.getBranchId() != null && !request.getBranchId().isEmpty()) {
            validateBranch(request.getBranchId(), request.getOrgID());
        }

        // Validate and get primary contact
        String mobileNumber = getPrimaryContactMobile(request.getContacts());
        if (mobileNumber == null) {
            throw new ResourceNotFoundException("Primary contact with mobile number is required");
        }

        // Check if mobile number exists in the organization
        checkMobileNumberExists(mobileNumber, request.getOrgID());

        try {
            // Create new customer
            Customer customer = createAndSaveCustomer(request, mobileNumber);
            
            // Save all related entities in a single transaction
            saveRelatedEntities(customer, request);
            
            return createCustomerResponse(customer);
        } catch (Exception e) {
            // This will trigger rollback of all database operations
            throw new RuntimeException("Failed to create customer: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if a mobile number already exists in the organization (either as a user or customer).
     *
     * @param mobileNumber The mobile number to check
     * @param orgId The organization ID to check in
     * @throws DuplicateResourceException if the mobile number is already in use
     */
    private void checkMobileNumberExists(String mobileNumber, String orgId) {
        // Extract dial code and phone number from mobile number using # separator
        String[] parts = mobileNumber.split("#");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid mobile number format. Must be in format: dialCode#phoneNumber (e.g., 91#1234567890)");
        }
        
        int dialCode = Integer.parseInt(parts[0]);
        Long phoneNumber = Long.parseLong(parts[1]);

        // Check for existing primary contact in contacts table for CUSTOMER type only within the same organization
        List<Contact> existingContacts = contactRepository.findByPhoneNumberAndLinkageTypeAndOrgId(phoneNumber, EntityType.CUSTOMER, orgId);
        if (!existingContacts.isEmpty() && existingContacts.stream().anyMatch(Contact::getIsPrimary)) {
            throw new DuplicateResourceException("Mobile number is already registered with a customer in this organization");
        }
    }

    /**
     * Finds a user by their mobile number and organization ID.
     *
     * @param mobileNumber The mobile number to search for
     * @param orgId The organization ID to search in
     * @return Optional containing the user if found
     */
    private Optional<User> findUserByMobile(String mobileNumber, String orgId) {
        List<Contact> contacts = contactRepository.findByLinkageId(orgId);
        return contacts.stream()
                .filter(contact -> {
                    String contactMobile = contact.getDialCode() + "#" + contact.getPhoneNumber().toString();
                    return contactMobile.equals(mobileNumber) && 
                           contact.getIsPrimary() && 
                           contact.getLinkageType() == EntityType.USER;
                })
                .findFirst()
                .map(contact -> userRepository.findById(contact.getLinkageId()).orElse(null));
    }

    /**
     * Validates that the branch exists and belongs to the organization.
     * Throws ResourceNotFoundException if the branch is not found or doesn't belong to the organization.
     *
     * @param branchId The branch ID to validate
     * @param orgId The organization ID to validate against
     * @throws ResourceNotFoundException if branch is not found or doesn't belong to the organization
     */
    private void validateBranch(String branchId, String orgId) {
        branchRepository.findByIdAndOrgID(branchId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found or does not belong to the organization"));
    }

    /**
     * Extracts the mobile number from the primary contact in the list of contacts.
     * Returns null if no primary contact is found or if the contacts list is empty.
     *
     * @param contacts List of contact DTOs to search through
     * @return The mobile number of the primary contact, or null if not found
     */
    private String getPrimaryContactMobile(List<ContactDTO> contacts) {
        if (contacts == null || contacts.isEmpty()) {
            return null;
        }
        return contacts.stream()
                .filter(ContactDTO::getIsPrimary)
                .findFirst()
                .map(contact -> contact.getDialCode() + "#" + contact.getPhoneNumber().toString())
                .orElse(null);
    }

    /**
     * Validates that the organization exists in the database.
     * Throws ResourceNotFoundException if the organization is not found.
     *
     * @param orgId The organization ID to validate
     * @throws ResourceNotFoundException if organization is not found
     */
    private void validateOrganization(String orgId) {
        organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
    }

    /**
     * Retrieves a user by their mobile number and organization ID.
     * Returns null if no user is found.
     *
     * @param mobileNumber The mobile number to search for
     * @param orgId The organization ID to search in
     * @return User object if found, null otherwise
     */
    private User getUserByMobileAndOrg(String mobileNumber, String orgId) {
        // Find the contact with the given mobile number
        List<Contact> contacts = contactRepository.findByLinkageId(orgId);
        return contacts.stream()
                .filter(contact -> {
                    String contactMobile = contact.getDialCode() + "#" + contact.getPhoneNumber().toString();
                    return contactMobile.equals(mobileNumber) && 
                           contact.getIsPrimary() && 
                           contact.getLinkageType() == EntityType.USER;
                })
                .findFirst()
                .map(contact -> userRepository.findById(contact.getLinkageId()).orElse(null))
                .orElse(null);
    }

    /**
     * Retrieves all addresses associated with a given linkage ID.
     *
     * @param linkageId The ID to search addresses for
     * @return List of addresses associated with the linkage ID
     */
    private List<Address> getAddressesByLinkageId(String linkageId) {
        return addressRepository.findByLinkageId(linkageId);
    }

    /**
     * Retrieves all contacts associated with a given linkage ID.
     *
     * @param linkageId The ID to search contacts for
     * @return List of contacts associated with the linkage ID
     */
    private List<Contact> getContactsByLinkageId(String linkageId) {
        return contactRepository.findByLinkageId(linkageId);
    }

    /**
     * Creates a customer response object with all associated data (addresses, contacts, branch mappings).
     * Uses parallel processing to fetch related data efficiently.
     *
     * @param customer The customer entity to create response for
     * @return CustomerResponse containing the customer and all related data
     */
     CustomerResponse createCustomerResponse(Customer customer) {
        // Get addresses and contacts
        List<Address> addresses = getAddressesByLinkageId(customer.getId());
        List<Contact> contacts = getContactsByLinkageId(customer.getId());

        // Get branch mapping
        List<CustomerBranchMapping> branchMappings = customerBranchMappingRepository.findByCustomerID(customer.getId());
        String branchId = branchMappings.isEmpty() ? null : branchMappings.get(0).getBranchID();

        // Create customer DTO
            CustomerDTO customerDTO = new CustomerDTO(
            customer.getId(),
            customer.getFirstName(),
            customer.getMiddleName(),
            customer.getLastName(),
            customer.getEmail(),
            customer.getOrgID(),
            convertToAddressDTOs(addresses),
            convertToContactDTOs(contacts),
            branchId  // Pass single branchId
            );

            return new CustomerResponse(customerDTO);
        }

    /**
     * Creates branch mappings for a customer.
     * Links the customer to branches and stores user ID if customer is also a user.
     *
     * @param customerId The ID of the customer to create mappings for
     * @param request The request containing branch IDs and organization details
     * @return List of created branch mapping entities
     */
    private List<CustomerBranchMapping> createBranchMappings(String customerId, CustomerRequest request) {
        if (request.getBranchId() == null || request.getBranchId().isEmpty()) {
            return new ArrayList<>();
        }

        // Validate branch exists
        validateBranch(request.getBranchId(), request.getOrgID());

        // Create branch mapping
        CustomerBranchMapping mapping = new CustomerBranchMapping();
        mapping.setCustomerID(customerId);
        mapping.setBranchID(request.getBranchId());
        mapping.setOrgID(request.getOrgID());
        
        return Collections.singletonList(customerBranchMappingRepository.save(mapping));
    }

    /**
     * Creates and saves a new customer entity with the provided request details.
     *
     * @param request The customer creation request
     * @param mobileNumber The mobile number to associate with the customer
     * @return The created and saved customer entity
     */
    private Customer createAndSaveCustomer(CustomerRequest request, String mobileNumber) {
        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setMiddleName(request.getMiddleName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setMobile(mobileNumber);
        customer.setOrgID(request.getOrgID());
        return customerRepository.save(customer);
    }

    /**
     * Saves all entities related to a customer (addresses, contacts, branch mappings) in parallel.
     *
     * @param customer The customer entity to save related data for
     * @param request The request containing the related data
     */
    private void saveRelatedEntities(Customer customer, CustomerRequest request) {
        final String customerId = customer.getId();
        
        // Save addresses
        List<Address> addresses = new ArrayList<>();
        if (request.getAddresses() != null && !request.getAddresses().isEmpty()) {
            addresses = saveAddresses(customerId, request.getAddresses());
            if (addresses.isEmpty()) {
                throw new RuntimeException("Failed to save addresses");
            }
        }
        
        // Save contacts
        List<Contact> contacts = new ArrayList<>();
        if (request.getContacts() != null && !request.getContacts().isEmpty()) {
            contacts = saveContacts(customerId, request.getContacts());
            if (contacts.isEmpty()) {
                throw new RuntimeException("Failed to save contacts");
            }
        }
        
        // Create branch mappings
        List<CustomerBranchMapping> mappings = new ArrayList<>();
        if (request.getBranchId() != null && !request.getBranchId().isEmpty()) {
            mappings = createBranchMappings(customerId, request);
            if (mappings.isEmpty()) {
                throw new RuntimeException("Failed to save branch mappings");
            }
        }
    }

    /**
     * Saves a list of addresses for a customer.
     * Validates address categories and creates address entities.
     *
     * @param customerId The ID of the customer to save addresses for
     * @param addressDTOs List of address DTOs to save
     * @return List of saved address entities
     */
    private List<Address> saveAddresses(String customerId, List<AddressDTO> addressDTOs) {
        if (addressDTOs == null || addressDTOs.isEmpty()) {
            return new ArrayList<>();
        }

        List<Address> addresses = addressDTOs.stream()
                .map(addressDTO -> {
                    AddressCategory category = addressCategoryRepository.findByName(addressDTO.getCategoryCode())
                            .orElseThrow(() -> new ResourceNotFoundException("Invalid address category: " + addressDTO.getCategoryCode()));
                    
                    Address address = new Address();
                    address.setCategoryCode(category.getId());
                    address.setLinkageId(customerId);
                    address.setLinkageType(EntityType.CUSTOMER);
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
                    return address;
                })
                .collect(Collectors.toList());

        return addressRepository.saveAll(addresses);
    }

    /**
     * Saves a list of contacts for a customer.
     * Validates contact categories and ensures only one primary contact exists.
     *
     * @param customerId The ID of the customer to save contacts for
     * @param contactDTOs List of contact DTOs to save
     * @return List of saved contact entities
     * @throws DuplicateResourceException if multiple primary contacts are found
     */
    private List<Contact> saveContacts(String customerId, List<ContactDTO> contactDTOs) {
        if (contactDTOs == null || contactDTOs.isEmpty()) {
            return new ArrayList<>();
        }

        validatePrimaryContact(contactDTOs);

        List<Contact> contacts = contactDTOs.stream()
                .map(contactDTO -> {
                    AddressCategory category = addressCategoryRepository.findByName(contactDTO.getCategoryCode())
                            .orElseThrow(() -> new ResourceNotFoundException("Invalid contact category: " + contactDTO.getCategoryCode()));
                    
                    Contact contact = new Contact();
                    contact.setCategoryCode(category.getId());
                    contact.setDialCode(contactDTO.getDialCode());
                    contact.setPhoneNumber(contactDTO.getPhoneNumber());
                    contact.setPrimary(contactDTO.getIsPrimary());
                    contact.setActive(contactDTO.getIsActive());
                    contact.setLinkageId(customerId);
                    contact.setLinkageType(EntityType.CUSTOMER);
                    return contact;
                })
                .collect(Collectors.toList());

        return contactRepository.saveAll(contacts);
    }

    /**
     * Validates that there is only one primary contact in the list.
     *
     * @param contactDTOs List of contact DTOs to validate
     * @throws DuplicateResourceException if multiple primary contacts are found
     */
    private void validatePrimaryContact(List<ContactDTO> contactDTOs) {
        long primaryCount = contactDTOs.stream()
                .filter(ContactDTO::getIsPrimary)
                .count();
        if (primaryCount > 1) {
            throw new DuplicateResourceException("Only one primary contact is allowed");
        }
    }

    /**
     * Updates an existing customer with new details.
     * Updates all related entities (addresses, contacts, branch mappings) in parallel.
     *
     * @param id The ID of the customer to update
     * @param request The request containing updated details
     * @return CustomerResponse containing the updated customer details
     */
    @Transactional
    public CustomerResponse updateCustomer(String id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        validateUpdateConstraints(customer, request);
        updateCustomerFields(customer, request);
        customer = customerRepository.save(customer);

        final String customerId = customer.getId();
        
        CompletableFuture<List<Address>> addressesFuture = CompletableFuture.supplyAsync(() -> 
            updateAddresses(customerId, request.getAddresses()));
        
        CompletableFuture<List<Contact>> contactsFuture = CompletableFuture.supplyAsync(() -> 
            updateContacts(customerId, request.getContacts()));
        
        CompletableFuture<List<CustomerBranchMapping>> mappingsFuture = CompletableFuture.supplyAsync(() -> 
            updateBranchMappings(customerId, request));

        try {
            CompletableFuture.allOf(addressesFuture, contactsFuture, mappingsFuture).get();
        } catch (Exception e) {
            throw new RuntimeException("Error updating customer related entities", e);
        }

        return createCustomerResponse(customer);
    }

    /**
     * Validates constraints for customer update.
     * Ensures email and mobile number uniqueness within the organization.
     *
     * @param customer The existing customer entity
     * @param request The update request containing new details
     * @throws DuplicateResourceException if email or mobile number already exists
     */
    private void validateUpdateConstraints(Customer customer, CustomerRequest request) {
        String mobileNumber = getPrimaryContactMobile(request.getContacts());
        if (mobileNumber == null) {
            throw new ResourceNotFoundException("Primary contact with mobile number is required");
        }

        if (!customer.getEmail().equals(request.getEmail()) && 
            customerRepository.existsByEmailAndOrgID(request.getEmail(), request.getOrgID())) {
            throw new DuplicateResourceException("Email already exists for this organization");
        }

        // Check if mobile number exists in contacts for any user in the organization
        List<Contact> existingContacts = contactRepository.findByLinkageId(request.getOrgID());
        boolean mobileExists = existingContacts.stream()
                .anyMatch(contact -> {
                    String contactMobile = contact.getDialCode() + "#" + contact.getPhoneNumber().toString();
                    return contactMobile.equals(mobileNumber) && contact.getIsPrimary();
                });

        if (!customer.getMobile().equals(mobileNumber) && mobileExists) {
            throw new DuplicateResourceException("Mobile number already exists for this organization");
        }
    }

    /**
     * Updates the fields of an existing customer entity.
     *
     * @param customer The customer entity to update
     * @param request The request containing new details
     */
    private void updateCustomerFields(Customer customer, CustomerRequest request) {
        String mobileNumber = getPrimaryContactMobile(request.getContacts());
        if (mobileNumber == null) {
            throw new ResourceNotFoundException("Primary contact with mobile number is required");
        }

        customer.setFirstName(request.getFirstName());
        customer.setMiddleName(request.getMiddleName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setMobile(mobileNumber);
    }

    /**
     * Updates addresses for a customer by deleting existing ones and saving new ones.
     *
     * @param customerId The ID of the customer to update addresses for
     * @param addressDTOs List of new address DTOs
     * @return List of updated address entities
     */
    private List<Address> updateAddresses(String customerId, List<AddressDTO> addressDTOs) {
        addressRepository.deleteByLinkageId(customerId);
        return saveAddresses(customerId, addressDTOs);
    }

    /**
     * Updates contacts for a customer by deleting existing ones and saving new ones.
     *
     * @param customerId The ID of the customer to update contacts for
     * @param contactDTOs List of new contact DTOs
     * @return List of updated contact entities
     */
    private List<Contact> updateContacts(String customerId, List<ContactDTO> contactDTOs) {
        contactRepository.deleteByLinkageId(customerId);
        return saveContacts(customerId, contactDTOs);
    }

    /**
     * Updates branch mappings for a customer by deleting existing ones and saving new ones.
     *
     * @param customerId The ID of the customer to update mappings for
     * @param request The request containing new branch IDs
     * @return List of updated branch mapping entities
     */
    private List<CustomerBranchMapping> updateBranchMappings(String customerId, CustomerRequest request) {
        customerBranchMappingRepository.deleteByCustomerID(customerId);
        return createBranchMappings(customerId, request);
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param id The ID of the customer to retrieve
     * @return CustomerResponse containing the customer details
     * @throws ResourceNotFoundException if customer is not found
     */
    public CustomerResponse getCustomerById(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return createCustomerResponse(customer);
    }

    /**
     * Retrieves all customers for a given organization.
     *
     * @param orgId The organization ID to get customers for
     * @return List of CustomerResponse objects containing customer details
     */
    public List<CustomerResponse> getAllCustomers(String orgId) {
        return customerRepository.findByOrgID(orgId).stream()
                .map(this::createCustomerResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all customers for a given organization and branch.
     *
     * @param orgId The organization ID to get customers for
     * @param branchId The branch ID to get customers for
     * @return List of CustomerResponse objects containing customer details
     */
    public List<CustomerResponse> getAllCustomersByBranch(String orgId, String branchId) {
        // Get all branch mappings for the given branch
        List<CustomerBranchMapping> branchMappings = customerBranchMappingRepository.findByBranchID(branchId);
        
        // Get all customer IDs from the mappings
        List<String> customerIds = branchMappings.stream()
                .map(CustomerBranchMapping::getCustomerID)
                .collect(Collectors.toList());
        
        // Get all customers for the organization
        List<Customer> customers = customerRepository.findByOrgID(orgId);
        
        // Filter customers that belong to the specified branch
        return customers.stream()
                .filter(customer -> customerIds.contains(customer.getId()))
                .map(this::createCustomerResponse)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a customer and all their related data (addresses, contacts, branch mappings).
     * Uses parallel processing for efficient deletion.
     *
     * @param id The ID of the customer to delete
     * @throws ResourceNotFoundException if customer is not found
     */
    @Transactional
    public void deleteCustomer(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        
        CompletableFuture<Void> addressesFuture = CompletableFuture.runAsync(() -> 
            addressRepository.deleteByLinkageId(id));
        
        CompletableFuture<Void> contactsFuture = CompletableFuture.runAsync(() -> 
            contactRepository.deleteByLinkageId(id));
        
        CompletableFuture<Void> mappingsFuture = CompletableFuture.runAsync(() -> 
            customerBranchMappingRepository.deleteByCustomerID(id));

        try {
            CompletableFuture.allOf(addressesFuture, contactsFuture, mappingsFuture).get();
            customerRepository.delete(customer);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting customer data", e);
        }
    }

    /**
     * Converts a list of address entities to address DTOs.
     *
     * @param addresses List of address entities to convert
     * @return List of address DTOs
     */
    private List<AddressDTO> convertToAddressDTOs(List<Address> addresses) {
        return addresses.stream()
                .map(this::convertToAddressDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of contact entities to contact DTOs.
     *
     * @param contacts List of contact entities to convert
     * @return List of contact DTOs
     */
    private List<ContactDTO> convertToContactDTOs(List<Contact> contacts) {
        return contacts.stream()
                .map(this::convertToContactDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts an address entity to an address DTO.
     *
     * @param address The address entity to convert
     * @return AddressDTO containing the address details
     */
    private AddressDTO convertToAddressDTO(Address address) {
        return new AddressDTO(
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
        );
    }

    /**
     * Converts a contact entity to a contact DTO.
     *
     * @param contact The contact entity to convert
     * @return ContactDTO containing the contact details
     */
    private ContactDTO convertToContactDTO(Contact contact) {
        return new ContactDTO(
                contact.getId(),
                contact.getCategoryCode(),
                contact.getDialCode(),
                contact.getPhoneNumber(),
                contact.getIsPrimary(),
                contact.isActive()
        );
    }

    /**
     * Extracts dial code and phone number from a mobile number string.
     *
     * @param mobileNumber The mobile number string in format "dialCode#phoneNumber" (e.g., "91#1234567890")
     * @return Array containing [dialCode, phoneNumber]
     */
    private Object[] extractMobileComponents(String mobileNumber) {
        String[] parts = mobileNumber.split("#");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid mobile number format. Must be in format: dialCode#phoneNumber (e.g., 91#1234567890)");
        }
        int dialCode = Integer.parseInt(parts[0]);
        Long phoneNumber = Long.parseLong(parts[1]);
        return new Object[]{dialCode, phoneNumber};
    }
} 