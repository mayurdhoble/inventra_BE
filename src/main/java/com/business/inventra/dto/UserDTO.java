package com.business.inventra.dto;

import java.util.List;

public class UserDTO {
    private String id;
    private String email;
    private String userName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String roleName;
    private List<BranchDTO> branches;
    private List<AddressDTO> addresses;
    private List<ContactDTO> contacts;

    public UserDTO() {
    }

    public UserDTO(String id, String email, String userName, String firstName, String middleName, String lastName, String roleName, List<BranchDTO> branches, List<AddressDTO> addresses, List<ContactDTO> contacts) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.roleName = roleName;
        this.branches = branches;
        this.addresses = addresses;
        this.contacts = contacts;
    }

    public UserDTO(String id, String email, String userName, String firstName, String middleName, String lastName, String roleName) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.roleName = roleName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<BranchDTO> getBranches() {
        return branches;
    }

    public void setBranches(List<BranchDTO> branches) {
        this.branches = branches;
    }

    public List<AddressDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDTO> addresses) {
        this.addresses = addresses;
    }

    public List<ContactDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactDTO> contacts) {
        this.contacts = contacts;
    }
}
