package com.example.usertemplate.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "Username is required") String username,
    @NotBlank(message = "Password is required") String password) {
  public LoginRequest {
    if (username != null && username.trim().isEmpty()) {
      throw new IllegalArgumentException("Username cannot be blank");
    }
    if (password != null && password.trim().isEmpty()) {
      throw new IllegalArgumentException("Password cannot be blank");
    }
  }
}
