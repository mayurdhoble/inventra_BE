package com.business.inventra.controller;

import com.business.inventra.dto.UserDTO;
import com.business.inventra.dto.request.UserRequest;
import com.business.inventra.dto.response.ApiResponse;
import com.business.inventra.dto.response.UserListResponse;
import com.business.inventra.dto.response.UserResponse;
import com.business.inventra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.ok(ApiResponse.success("User created successfully", response));
    }

    @GetMapping("/{id}/organizations/{orgId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(
            @PathVariable String id,
            @PathVariable String orgId) {
        UserResponse response = userService.getUserById(id, orgId);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", response));
    }

    @GetMapping("/organizations/{orgId}/branches/{branchId}")
    public ResponseEntity<ApiResponse<UserListResponse>> getAllUsersByBranch(
            @PathVariable String orgId,
            @PathVariable String branchId) {
        List<UserDTO> users = userService.getAllUsers(orgId, branchId);
        UserListResponse response = new UserListResponse(users);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", response));
    }

    @GetMapping("/organizations/{orgId}")
    public ResponseEntity<ApiResponse<UserListResponse>> getAllUsersByOrg(
            @PathVariable String orgId) {
        List<UserDTO> users = userService.getAllUsers(orgId, null);
        UserListResponse response = new UserListResponse(users);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable String id,
            @RequestBody UserRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", response));
    }

    @DeleteMapping("/{id}/organizations/{orgId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable String id,
            @PathVariable String orgId) {
        userService.deleteUser(id, orgId);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }
} 