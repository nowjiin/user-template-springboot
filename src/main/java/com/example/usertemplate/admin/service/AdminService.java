package com.example.usertemplate.admin.service;

import org.springframework.data.domain.Pageable;

import com.example.usertemplate.global.common.PageResponse;
import com.example.usertemplate.user.dto.UserResponse;
import com.example.usertemplate.user.dto.UserUpdateRequest;

public interface AdminService {

  PageResponse<UserResponse> getAllUsers(Pageable pageable);

  PageResponse<UserResponse> searchUsers(String keyword, Pageable pageable);

  UserResponse getUserById(Long id);

  UserResponse updateUser(Long id, UserUpdateRequest request);

  void deleteUser(Long id);

  UserResponse enableUser(Long id);

  UserResponse disableUser(Long id);
}
