package com.business.inventra.service;

import com.business.inventra.constant.EntityType;
import com.business.inventra.dto.AddressDTO;
import com.business.inventra.dto.BranchDTO;
import com.business.inventra.dto.ContactDTO;
import com.business.inventra.dto.CustomerDTO;
import com.business.inventra.dto.UserDTO;
import com.business.inventra.dto.request.MobileCheckRequest;
import com.business.inventra.dto.response.MobileCheckResponse;
import com.business.inventra.exception.ResourceNotFoundException;
import com.business.inventra.model.Address;
import com.business.inventra.model.Branch;
import com.business.inventra.model.Contact;
import com.business.inventra.model.Customer;
import com.business.inventra.model.User;
import com.business.inventra.model.UserBranchMapping;
import com.business.inventra.repository.AddressRepository;
import com.business.inventra.repository.BranchRepository;
import com.business.inventra.repository.ContactRepository;
import com.business.inventra.repository.CustomerRepository;
import com.business.inventra.repository.RoleRepository;
import com.business.inventra.repository.UserBranchMappingRepository;
import com.business.inventra.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import com.business.inventra.model.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CustomerService customerService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AddressRepository addressRepository;
    
    @Autowired
    private UserBranchMappingRepository userBranchMappingRepository;

    @Autowired
    BranchRepository branchRepository;
    
    @Transactional(readOnly = true)
    public MobileCheckResponse checkMobileNumber(MobileCheckRequest request) {
        Long phoneNumber = Long.parseLong(request.getMobileNumber());

        // Check for the specified user type
        List<Contact> contacts = contactRepository.findByMobileNumberAndTypeAndOrgId(
            phoneNumber, request.getUserType(), request.getOrgId()
        );

        if (contacts.isEmpty()) {
            return new MobileCheckResponse(false);
        }

        Contact contact = contacts.get(0);
        if (request.getUserType() == EntityType.USER) {
            User user = userRepository.findById(contact.getLinkageId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            Role role = roleRepository.findById(user.getRoleID())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
            List<Address> addresses = addressRepository.findByLinkageId(user.getId());
            List<Contact> userContacts = contactRepository.findByLinkageId(user.getId());
            
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

            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setEmail(user.getEmail());
            userDTO.setUserName(user.getUserName());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setMiddleName(user.getMiddleName());
            userDTO.setLastName(user.getLastName());
            userDTO.setRoleName(role.getName());
            userDTO.setBranches(branches);
            userDTO.setAddresses(addresses.stream()
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
            userDTO.setContacts(userContacts.stream()
                .map(c -> new ContactDTO(
                    c.getId(),
                    c.getCategoryCode(),
                    c.getDialCode(),
                    c.getPhoneNumber(),
                    c.getIsPrimary(),
                    c.isActive()
                ))
                .collect(Collectors.toList()));

            return new MobileCheckResponse(true, "USER", userDTO, null);
        } else if (request.getUserType() == EntityType.CUSTOMER) {
            Customer customer = customerRepository.findById(contact.getLinkageId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
            
            // Get customer's addresses and contacts
            List<Address> addresses = addressRepository.findByLinkageId(customer.getId());
            List<Contact> customerContacts = contactRepository.findByLinkageId(customer.getId());
            
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(customer.getId());
            customerDTO.setFirstName(customer.getFirstName());
            customerDTO.setMiddleName(customer.getMiddleName());
            customerDTO.setLastName(customer.getLastName());
            customerDTO.setEmail(customer.getEmail());
            customerDTO.setOrgID(customer.getOrgID());
            customerDTO.setAddresses(addresses.stream()
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
            customerDTO.setContacts(customerContacts.stream()
                .map(c -> new ContactDTO(
                    c.getId(),
                    c.getCategoryCode(),
                    c.getDialCode(),
                    c.getPhoneNumber(),
                    c.getIsPrimary(),
                    c.isActive()
                ))
                .collect(Collectors.toList()));
            
            return new MobileCheckResponse(true, "CUSTOMER", null, customerDTO);
        }

        return new MobileCheckResponse(false);
    }

    // Getters and Setters
    public ContactRepository getContactRepository() {
        return contactRepository;
    }

    public void setContactRepository(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }

    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public AddressRepository getAddressRepository() {
        return addressRepository;
    }

    public void setAddressRepository(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
} 