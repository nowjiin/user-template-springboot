package com.example.usertemplate.user.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.usertemplate.global.common.ApiResponse;
import com.example.usertemplate.user.dto.UserResponse;
import com.example.usertemplate.user.dto.UserUpdateRequest;
import com.example.usertemplate.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User management APIs")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  @Operation(
      summary = "Get current user profile",
      description = "Get the profile of the currently authenticated user")
  public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(Authentication authentication) {
    try {
      if (authentication == null || authentication.getPrincipal() == null) {
        throw new IllegalStateException("User is not authenticated");
      }

      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String username = userDetails.getUsername();
      log.info("Getting profile for user: {}", username);

      UserResponse userResponse = userService.getUserByUsername(username);
      return ResponseEntity.ok(
          ApiResponse.success("User profile retrieved successfully", userResponse));
    } catch (Exception ex) {
      log.error("Failed to get current user profile: ", ex);
      throw ex;
    }
  }

  @PutMapping("/me")
  @Operation(
      summary = "Update current user profile",
      description = "Update the profile of the currently authenticated user")
  public ResponseEntity<ApiResponse<UserResponse>> updateCurrentUser(
      @Valid @RequestBody UserUpdateRequest request, Authentication authentication) {
    try {
      if (authentication == null || authentication.getPrincipal() == null) {
        throw new IllegalStateException("User is not authenticated");
      }

      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String username = userDetails.getUsername();
      log.info("Updating profile for user: {}", username);

      UserResponse userResponse = userService.updateCurrentUser(username, request);
      return ResponseEntity.ok(
          ApiResponse.success("User profile updated successfully", userResponse));
    } catch (Exception ex) {
      log.error("Failed to update user profile: ", ex);
      throw ex;
    }
  }

  @DeleteMapping("/me")
  @Operation(
      summary = "Delete current user account",
      description = "Delete the account of the currently authenticated user")
  public ResponseEntity<ApiResponse<Void>> deleteCurrentUser(Authentication authentication) {
    try {
      if (authentication == null || authentication.getPrincipal() == null) {
        throw new IllegalStateException("User is not authenticated");
      }

      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String username = userDetails.getUsername();
      log.info("Deleting account for user: {}", username);

      userService.deleteCurrentUser(username);
      return ResponseEntity.ok(ApiResponse.success("User account deleted successfully", null));
    } catch (Exception ex) {
      log.error("Failed to delete user account: ", ex);
      throw ex;
    }
  }
}
