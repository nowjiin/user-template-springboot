package com.example.usertemplate.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,
    @Email(message = "Email should be valid") @NotBlank(message = "Email is required") String email,
    @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password) {
  public RegisterRequest {
    if (username != null && username.trim().isEmpty()) {
      throw new IllegalArgumentException("Username cannot be blank");
    }
    if (email != null && email.trim().isEmpty()) {
      throw new IllegalArgumentException("Email cannot be blank");
    }
    if (password != null && password.trim().isEmpty()) {
      throw new IllegalArgumentException("Password cannot be blank");
    }
  }
}
