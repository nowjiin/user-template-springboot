package com.example.usertemplate.user.service;

import org.springframework.data.domain.Pageable;

import com.example.usertemplate.global.common.PageResponse;
import com.example.usertemplate.user.dto.UserCreateRequest;
import com.example.usertemplate.user.dto.UserResponse;
import com.example.usertemplate.user.dto.UserUpdateRequest;

public interface UserService {

  UserResponse createUser(UserCreateRequest request);

  UserResponse getUserById(Long id);

  UserResponse getUserByUsername(String username);

  UserResponse updateCurrentUser(String username, UserUpdateRequest request);

  void deleteCurrentUser(String username);

  PageResponse<UserResponse> getAllUsers(Pageable pageable);

  PageResponse<UserResponse> searchUsers(String keyword, Pageable pageable);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}
