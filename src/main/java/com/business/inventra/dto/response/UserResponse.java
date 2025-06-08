package com.business.inventra.dto.response;


import com.business.inventra.dto.UserDTO;

public class UserResponse {
    private UserDTO user;

    public UserResponse() {
    }

    public UserResponse(UserDTO userDTO) {
        this.user = userDTO;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
} 