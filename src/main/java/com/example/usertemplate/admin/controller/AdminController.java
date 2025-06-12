package com.example.usertemplate.admin.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.usertemplate.admin.service.AdminService;
import com.example.usertemplate.global.common.ApiResponse;
import com.example.usertemplate.global.common.PageResponse;
import com.example.usertemplate.user.dto.UserResponse;
import com.example.usertemplate.user.dto.UserUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "Admin management APIs")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

  private final AdminService adminService;

  @GetMapping("/users")
  @Operation(summary = "Get all users", description = "Get all users with pagination")
  public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAllUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDir) {
    try {
      log.info("Admin: Getting all users - page: {}, size: {}", page, size);

      Sort sort =
          sortDir.equalsIgnoreCase("desc")
              ? Sort.by(sortBy).descending()
              : Sort.by(sortBy).ascending();
      Pageable pageable = PageRequest.of(page, size, sort);

      PageResponse<UserResponse> users = adminService.getAllUsers(pageable);
      return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    } catch (Exception ex) {
      log.error("Admin: Failed to get all users: ", ex);
      throw ex;
    }
  }

  @GetMapping("/users/search")
  @Operation(summary = "Search users", description = "Search users by username or email")
  public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> searchUsers(
      @RequestParam String keyword,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDir) {
    try {
      log.info("Admin: Searching users with keyword: {}", keyword);

      Sort sort =
          sortDir.equalsIgnoreCase("desc")
              ? Sort.by(sortBy).descending()
              : Sort.by(sortBy).ascending();
      Pageable pageable = PageRequest.of(page, size, sort);

      PageResponse<UserResponse> users = adminService.searchUsers(keyword, pageable);
      return ResponseEntity.ok(ApiResponse.success("Users found successfully", users));
    } catch (Exception ex) {
      log.error("Admin: Failed to search users: ", ex);
      throw ex;
    }
  }

  @GetMapping("/users/{id}")
  @Operation(summary = "Get user by ID", description = "Get a specific user by their ID")
  public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
    try {
      log.info("Admin: Getting user by ID: {}", id);
      UserResponse user = adminService.getUserById(id);
      return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    } catch (Exception ex) {
      log.error("Admin: Failed to get user by ID: ", ex);
      throw ex;
    }
  }

  @PutMapping("/users/{id}")
  @Operation(summary = "Update user", description = "Update a user's information")
  public ResponseEntity<ApiResponse<UserResponse>> updateUser(
      @PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
    try {
      log.info("Admin: Updating user ID: {}", id);
      UserResponse user = adminService.updateUser(id, request);
      return ResponseEntity.ok(ApiResponse.success("User updated successfully", user));
    } catch (Exception ex) {
      log.error("Admin: Failed to update user: ", ex);
      throw ex;
    }
  }

  @DeleteMapping("/users/{id}")
  @Operation(summary = "Delete user", description = "Delete a user account")
  public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
    try {
      log.info("Admin: Deleting user ID: {}", id);
      adminService.deleteUser(id);
      return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    } catch (Exception ex) {
      log.error("Admin: Failed to delete user: ", ex);
      throw ex;
    }
  }

  @PostMapping("/users/{id}/enable")
  @Operation(summary = "Enable user", description = "Enable a user account")
  public ResponseEntity<ApiResponse<UserResponse>> enableUser(@PathVariable Long id) {
    try {
      log.info("Admin: Enabling user ID: {}", id);
      UserResponse user = adminService.enableUser(id);
      return ResponseEntity.ok(ApiResponse.success("User enabled successfully", user));
    } catch (Exception ex) {
      log.error("Admin: Failed to enable user: ", ex);
      throw ex;
    }
  }

  @PostMapping("/users/{id}/disable")
  @Operation(summary = "Disable user", description = "Disable a user account")
  public ResponseEntity<ApiResponse<UserResponse>> disableUser(@PathVariable Long id) {
    try {
      log.info("Admin: Disabling user ID: {}", id);
      UserResponse user = adminService.disableUser(id);
      return ResponseEntity.ok(ApiResponse.success("User disabled successfully", user));
    } catch (Exception ex) {
      log.error("Admin: Failed to disable user: ", ex);
      throw ex;
    }
  }
}
