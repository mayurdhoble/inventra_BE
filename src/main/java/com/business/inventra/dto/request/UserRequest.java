package com.business.inventra.dto.request;

import com.business.inventra.dto.AddressDTO;
import com.business.inventra.dto.ContactDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class UserRequest {
    @NotBlank(message = "First name is required")
    private String firstName;

    private String middleName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String userName;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Organization ID is required")
    private String orgID;

    @NotNull(message = "At least one branch is required")
    private List<String> branchIDs;

    @NotBlank(message = "Role is required")
    private String role;

    @Valid
    @NotNull(message = "At least one address is required")
    private List<AddressDTO> addresses;

    @Valid
    @NotNull(message = "At least one contact is required")
    private List<ContactDTO> contacts;

    // Default constructor
    public UserRequest() {
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getOrgID() {
        return orgID;
    }

    public List<String> getBranchIDs() {
        return branchIDs;
    }

    public String getRole() {
        return role;
    }

    public List<AddressDTO> getAddresses() {
        return addresses;
    }

    public List<ContactDTO> getContacts() {
        return contacts;
    }

    // Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    public void setBranchIDs(List<String> branchIDs) {
        this.branchIDs = branchIDs;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAddresses(List<AddressDTO> addresses) {
        this.addresses = addresses;
    }

    public void setContacts(List<ContactDTO> contacts) {
        this.contacts = contacts;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
} 