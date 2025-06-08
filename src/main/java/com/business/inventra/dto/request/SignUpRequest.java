package com.business.inventra.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "Organization name is required")
    private String orgName;

    @NotBlank(message = "Organization email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "First name is required")
    private String firstName;
    
    private String middleName;

    @NotBlank(message = "Last name is required")
    private String lastName;
    
    private String userName;
    
    @NotBlank(message = "Password is required")
    private String password;

	public String getOrgName() {
		return orgName;
	}

	public void setorgName(String orgName) {
		this.orgName = orgName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "SignUpnRequest [orgName=" + orgName + ", email=" + email + ", firstName=" + firstName + ", middleName="
				+ middleName + ", lastName=" + lastName + ", userName=" + userName + ", password=" + password + "]";
	}

	  
    
} 