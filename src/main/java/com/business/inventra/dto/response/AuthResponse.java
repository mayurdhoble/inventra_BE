package com.business.inventra.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AuthResponse {

	private String token;
    private String username;
    private String email;
    
    
	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public AuthResponse(String token, String username, String email) {
		super();
		this.token = token;
		this.username = username;
		this.email = email;
	}
    
    
    
} 