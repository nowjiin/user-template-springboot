package com.example.usertemplate.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
    @NotBlank(message = "Refresh token is required") String refreshToken) {
  public RefreshTokenRequest {
    if (refreshToken != null && refreshToken.trim().isEmpty()) {
      throw new IllegalArgumentException("Refresh token cannot be blank");
    }
  }
}
