package com.example.usertemplate.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.usertemplate.auth.controller.AuthController;
import com.example.usertemplate.auth.dto.LoginRequest;
import com.example.usertemplate.auth.dto.RegisterRequest;
import com.example.usertemplate.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
@DisplayName("Authentication Controller Tests")
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private AuthService authService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @DisplayName("Should register user successfully with valid request")
  void registerUser_WithValidRequest_ShouldReturnCreated() throws Exception {
    // Given
    RegisterRequest request = new RegisterRequest("testuser", "test@example.com", "password123");

    // When & Then
    mockMvc
        .perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.result").value("SUCCESS"));
  }

  @Test
  @DisplayName("Should return bad request with invalid registration data")
  void registerUser_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
    // Given
    RegisterRequest request = new RegisterRequest("", "invalid-email", "123");

    // When & Then
    mockMvc
        .perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Should login user successfully with valid credentials")
  void loginUser_WithValidRequest_ShouldReturnOk() throws Exception {
    // Given
    LoginRequest request = new LoginRequest("testuser", "password123");

    // When & Then
    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.result").value("SUCCESS"));
  }

  @Test
  @DisplayName("Should return bad request with empty login credentials")
  void loginUser_WithEmptyCredentials_ShouldReturnBadRequest() throws Exception {
    // Given
    LoginRequest request = new LoginRequest("", "");

    // When & Then
    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }
}
