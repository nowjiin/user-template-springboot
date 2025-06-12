package com.example.usertemplate.auth.security;

import java.io.IOException;
import java.time.LocalDateTime;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException {

    log.warn(
        "Access denied for request to {} - {}",
        request.getRequestURI(),
        accessDeniedException.getMessage());

    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpStatus.FORBIDDEN.value());

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .result("ERROR")
            .message("Access denied")
            .errorCode("ACCESS_DENIED")
            .timestamp(LocalDateTime.now())
            .build();

    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }

  private static class ErrorResponse {
    public String result;
    public String message;
    public String errorCode;
    public Object details = null;
    public LocalDateTime timestamp;

    public static ErrorResponseBuilder builder() {
      return new ErrorResponseBuilder();
    }

    public static class ErrorResponseBuilder {
      private final ErrorResponse errorResponse = new ErrorResponse();

      public ErrorResponseBuilder result(String result) {
        errorResponse.result = result;
        return this;
      }

      public ErrorResponseBuilder message(String message) {
        errorResponse.message = message;
        return this;
      }

      public ErrorResponseBuilder errorCode(String errorCode) {
        errorResponse.errorCode = errorCode;
        return this;
      }

      public ErrorResponseBuilder timestamp(LocalDateTime timestamp) {
        errorResponse.timestamp = timestamp;
        return this;
      }

      public ErrorResponse build() {
        return errorResponse;
      }
    }
  }
}
