package com.business.inventra.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import com.business.inventra.util.KeyGenerator;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})
    }
)
public class User extends BaseModel {
    private static final String USER_PREFIX = KeyGenerator.USER_PREFIX;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "middleName")
    private String middleName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "userName", nullable = true)
    private String userName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status", nullable = false, length = 1)
    private String status = "Y";  // Default to active (Y)

    @Column(name = "roleID", nullable = false)
    private String roleID;

    @Column(name = "type", nullable = false)
    private String type = "USER";  // Default type for User

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleID", referencedColumnName = "id", insertable = false, updatable = false)
    private Role role;

    public User() {
        this.prefix = USER_PREFIX;
    }

    // Getters and Setters
    
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

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password; // encode outside service layer
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoleID() {
        return roleID;
    }
    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Convenience method to get full name
    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(firstName);
        if (middleName != null && !middleName.isEmpty()) {
            sb.append(" ").append(middleName);
        }
        sb.append(" ").append(lastName);
        return sb.toString();
    }
}
