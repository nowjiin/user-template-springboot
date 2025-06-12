package com.example.usertemplate.global.exception;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
  private String result;
  private String message;
  private String errorCode;
  private List<String> details;
  private LocalDateTime timestamp;

  public static ErrorResponse of(String message, String errorCode) {
    return new ErrorResponse("ERROR", message, errorCode, null, LocalDateTime.now());
  }

  public static ErrorResponse of(String message, String errorCode, List<String> details) {
    return new ErrorResponse("ERROR", message, errorCode, details, LocalDateTime.now());
  }
}
