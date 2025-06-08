package com.business.inventra.dto.response;

import com.business.inventra.dto.OrgDTO;
import com.business.inventra.dto.UserDTO;

public class SignUpResponse {

	private String token;
	private UserDTO user;
	private OrgDTO organization;
	
	public SignUpResponse() {
	}
	
	public SignUpResponse(String token, UserDTO userDTO, OrgDTO orgDTO) {
		this.token = token;
		this.user = userDTO;
		this.organization = orgDTO;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public OrgDTO getOrganization() {
		return organization;
	}

	public void setOrganization(OrgDTO organization) {
		this.organization = organization;
	}
	
	
	
	
	
	
}
