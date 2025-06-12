package com.example.usertemplate.user.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.usertemplate.global.common.PageResponse;
import com.example.usertemplate.global.exception.BusinessException;
import com.example.usertemplate.user.dto.UserCreateRequest;
import com.example.usertemplate.user.dto.UserResponse;
import com.example.usertemplate.user.dto.UserUpdateRequest;
import com.example.usertemplate.user.entity.User;
import com.example.usertemplate.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public UserResponse createUser(UserCreateRequest request) {
    log.info("Creating user with username: {}", request.username());

    if (userRepository.existsByUsername(request.username())) {
      throw new BusinessException("Username already exists", 409, "DUPLICATE_USERNAME");
    }

    if (userRepository.existsByEmail(request.email())) {
      throw new BusinessException("Email already exists", 409, "DUPLICATE_EMAIL");
    }

    User user =
        User.builder()
            .username(request.username())
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .build();

    User savedUser = userRepository.save(user);
    log.info("User created successfully with ID: {}", savedUser.getId());

    return UserResponse.from(savedUser);
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse getUserById(Long id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new BusinessException("User not found", 404, "USER_NOT_FOUND"));
    return UserResponse.from(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse getUserByUsername(String username) {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new BusinessException("User not found", 404, "USER_NOT_FOUND"));
    return UserResponse.from(user);
  }

  @Override
  @Transactional
  public UserResponse updateCurrentUser(String username, UserUpdateRequest request) {
    log.info("Updating user: {}", username);

    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new BusinessException("User not found", 404, "USER_NOT_FOUND"));

    if (request.username() != null && !request.username().equals(user.getUsername())) {
      if (userRepository.existsByUsername(request.username())) {
        throw new BusinessException("Username already exists", 409, "DUPLICATE_USERNAME");
      }
      user.setUsername(request.username());
    }

    if (request.email() != null && !request.email().equals(user.getEmail())) {
      if (userRepository.existsByEmail(request.email())) {
        throw new BusinessException("Email already exists", 409, "DUPLICATE_EMAIL");
      }
      user.setEmail(request.email());
    }

    if (request.password() != null) {
      user.setPassword(passwordEncoder.encode(request.password()));
    }

    User updatedUser = userRepository.save(user);
    log.info("User updated successfully: {}", username);

    return UserResponse.from(updatedUser);
  }

  @Override
  @Transactional
  public void deleteCurrentUser(String username) {
    log.info("Deleting user: {}", username);

    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new BusinessException("User not found", 404, "USER_NOT_FOUND"));

    userRepository.delete(user);
    log.info("User deleted successfully: {}", username);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<UserResponse> getAllUsers(Pageable pageable) {
    Page<User> userPage = userRepository.findAll(pageable);
    List<UserResponse> userResponses =
        userPage.getContent().stream().map(UserResponse::from).toList();

    return PageResponse.of(
        userResponses, userPage.getNumber(), userPage.getSize(), userPage.getTotalElements());
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<UserResponse> searchUsers(String keyword, Pageable pageable) {
    Page<User> userPage =
        userRepository.findByUsernameContainingOrEmailContaining(keyword, pageable);
    List<UserResponse> userResponses =
        userPage.getContent().stream().map(UserResponse::from).toList();

    return PageResponse.of(
        userResponses, userPage.getNumber(), userPage.getSize(), userPage.getTotalElements());
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }
}
