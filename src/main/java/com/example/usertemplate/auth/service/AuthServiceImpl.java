package com.example.usertemplate.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.usertemplate.auth.dto.LoginRequest;
import com.example.usertemplate.auth.dto.LoginResponse;
import com.example.usertemplate.auth.dto.RefreshTokenRequest;
import com.example.usertemplate.auth.dto.RegisterRequest;
import com.example.usertemplate.auth.security.JwtTokenProvider;
import com.example.usertemplate.global.exception.BusinessException;
import com.example.usertemplate.user.dto.UserResponse;
import com.example.usertemplate.user.entity.Role;
import com.example.usertemplate.user.entity.User;
import com.example.usertemplate.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  @Transactional
  public UserResponse register(RegisterRequest request) {
    log.info("Registering new user with username: {}", request.username());

    // Check if user already exists
    if (userRepository.existsByUsername(request.username())) {
      throw new BusinessException("Username already exists", 409, "DUPLICATE_USERNAME");
    }

    if (userRepository.existsByEmail(request.email())) {
      throw new BusinessException("Email already exists", 409, "DUPLICATE_EMAIL");
    }

    // Create new user
    User user =
        User.builder()
            .username(request.username())
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .role(Role.USER)
            .enabled(true)
            .accountNonExpired(true)
            .accountNonLocked(true)
            .credentialsNonExpired(true)
            .build();

    User savedUser = userRepository.save(user);
    log.info("User registered successfully with ID: {}", savedUser.getId());

    return UserResponse.from(savedUser);
  }

  @Override
  public LoginResponse login(LoginRequest request) {
    log.info("Attempting login for username: {}", request.username());

    try {
      // Authenticate user
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(request.username(), request.password()));

      // Generate tokens
      String accessToken = jwtTokenProvider.generateAccessToken(authentication);
      String refreshToken = jwtTokenProvider.generateRefreshToken(request.username());

      log.info("Login successful for username: {}", request.username());

      return LoginResponse.of(accessToken, refreshToken);
    } catch (Exception ex) {
      log.error("Login failed for username: {}", request.username(), ex);
      throw new BusinessException("Invalid username or password", 401, "AUTHENTICATION_FAILED");
    }
  }

  @Override
  public LoginResponse refreshToken(RefreshTokenRequest request) {
    log.info("Attempting token refresh");

    try {
      String username = jwtTokenProvider.getUsernameFromToken(request.refreshToken());

      if (!jwtTokenProvider.validateToken(request.refreshToken())) {
        throw new BusinessException("Invalid refresh token", 401, "INVALID_TOKEN");
      }

      User user =
          userRepository
              .findByUsername(username)
              .orElseThrow(() -> new BusinessException("User not found", 404, "USER_NOT_FOUND"));

      // Create authentication for token generation
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());

      // Generate new tokens
      String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
      String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);

      log.info("Token refresh successful for username: {}", username);

      return LoginResponse.of(newAccessToken, newRefreshToken);
    } catch (Exception ex) {
      log.error("Token refresh failed", ex);
      throw new BusinessException("Failed to refresh token", 401, "TOKEN_REFRESH_FAILED");
    }
  }
}
