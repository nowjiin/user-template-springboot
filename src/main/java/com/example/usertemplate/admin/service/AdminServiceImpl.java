package com.example.usertemplate.admin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.usertemplate.global.common.PageResponse;
import com.example.usertemplate.global.exception.BusinessException;
import com.example.usertemplate.user.dto.UserResponse;
import com.example.usertemplate.user.dto.UserUpdateRequest;
import com.example.usertemplate.user.entity.User;
import com.example.usertemplate.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional(readOnly = true)
  public PageResponse<UserResponse> getAllUsers(Pageable pageable) {
    log.info("Admin: Getting all users");
    Page<User> userPage = userRepository.findAll(pageable);
    List<UserResponse> userResponses =
        userPage.getContent().stream().map(UserResponse::from).toList();

    return PageResponse.of(
        userResponses, userPage.getNumber(), userPage.getSize(), userPage.getTotalElements());
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<UserResponse> searchUsers(String keyword, Pageable pageable) {
    log.info("Admin: Searching users with keyword: {}", keyword);
    Page<User> userPage =
        userRepository.findByUsernameContainingOrEmailContaining(keyword, pageable);
    List<UserResponse> userResponses =
        userPage.getContent().stream().map(UserResponse::from).toList();

    return PageResponse.of(
        userResponses, userPage.getNumber(), userPage.getSize(), userPage.getTotalElements());
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse getUserById(Long id) {
    log.info("Admin: Getting user by ID: {}", id);
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new BusinessException("User not found", 404, "USER_NOT_FOUND"));
    return UserResponse.from(user);
  }

  @Override
  @Transactional
  public UserResponse updateUser(Long id, UserUpdateRequest request) {
    log.info("Admin: Updating user ID: {}", id);

    User user =
        userRepository
            .findById(id)
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
    log.info("Admin: User updated successfully: {}", id);

    return UserResponse.from(updatedUser);
  }

  @Override
  @Transactional
  public void deleteUser(Long id) {
    log.info("Admin: Deleting user ID: {}", id);

    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new BusinessException("User not found", 404, "USER_NOT_FOUND"));

    userRepository.delete(user);
    log.info("Admin: User deleted successfully: {}", id);
  }

  @Override
  @Transactional
  public UserResponse enableUser(Long id) {
    log.info("Admin: Enabling user ID: {}", id);

    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new BusinessException("User not found", 404, "USER_NOT_FOUND"));

    user.setEnabled(true);
    User updatedUser = userRepository.save(user);
    log.info("Admin: User enabled successfully: {}", id);

    return UserResponse.from(updatedUser);
  }

  @Override
  @Transactional
  public UserResponse disableUser(Long id) {
    log.info("Admin: Disabling user ID: {}", id);

    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new BusinessException("User not found", 404, "USER_NOT_FOUND"));

    user.setEnabled(false);
    User updatedUser = userRepository.save(user);
    log.info("Admin: User disabled successfully: {}", id);

    return UserResponse.from(updatedUser);
  }
}
