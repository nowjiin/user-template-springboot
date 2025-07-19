package com.example.usertemplate.global.common;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
  private String result;
  private String message;
  private T data;
  private LocalDateTime timestamp;

  public static <T> ApiResponse<T> success(String message, T data) {
    return new ApiResponse<>("SUCCESS", message, data, LocalDateTime.now());
  }

  public static <T> ApiResponse<T> error(String message) {
    return new ApiResponse<>("ERROR", message, null, LocalDateTime.now());
  }

  public static <T> ApiResponse<T> error(String message, T data) {
    return new ApiResponse<>("ERROR", message, data, LocalDateTime.now());
  }
}
