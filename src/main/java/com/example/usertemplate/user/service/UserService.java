package com.example.usertemplate.user.service;

import com.example.usertemplate.user.dto.UserResponse;
import com.example.usertemplate.user.dto.UserUpdateRequest;

public interface UserService {

  UserResponse getUserById(Long id);

  UserResponse updateCurrentUser(Long userId, UserUpdateRequest request);

  void deleteCurrentUser(Long userId);
}
