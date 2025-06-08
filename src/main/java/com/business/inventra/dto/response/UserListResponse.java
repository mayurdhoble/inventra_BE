package com.business.inventra.dto.response;

import java.util.List;

import com.business.inventra.dto.UserDTO;

public class UserListResponse {
    private List<UserDTO> users;

    public UserListResponse() {
    }

    public UserListResponse(List<UserDTO> users) {
        this.users = users;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }
} 