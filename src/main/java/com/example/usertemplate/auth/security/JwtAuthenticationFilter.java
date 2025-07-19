package com.example.usertemplate.auth.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.usertemplate.user.entity.User;
import com.example.usertemplate.user.repository.UserRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider tokenProvider;
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    try {
      // 요청에서 JWT 토큰 추출
      String jwt = getJwtFromRequest(request);

      log.debug("🔍 JWT Filter - Token extracted: {}", jwt != null ? "present" : "null");

      // 토큰이 존재하고 유효한 경우 인증 처리
      if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
        log.debug("✅ JWT Filter - Token validation successful");

        // 토큰에서 사용자 ID 추출
        Long userId = tokenProvider.getUserIdAsLongFromToken(jwt);
        log.debug("🔍 JWT Filter - User ID extracted: {}", userId);

        // 사용자 ID로 사용자 상세 정보 로드
        User user = userRepository.findById(userId).orElse(null);
        assert user != null;
        log.debug("✅ JWT Filter - User found: {}", user.getUsername());

        // Spring Security 인증 객체 생성
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        // 요청 상세 정보 설정
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // SecurityContext에 인증 정보 설정 (강화된 설정)
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.debug("🔐 JWT authentication successful for user: {}", user.getUsername());
        log.debug(
            "🔐 SecurityContext set: {}",
            SecurityContextHolder.getContext().getAuthentication().getName());
      }
    } catch (Exception ex) {
      log.error("Could not set user authentication in security context : {}", ex.getMessage());
    }

    filterChain.doFilter(request, response);
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
