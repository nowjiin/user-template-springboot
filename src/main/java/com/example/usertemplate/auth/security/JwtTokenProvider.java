package com.example.usertemplate.auth.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

  private final SecretKey key;
  private final long jwtExpiration;
  private final long refreshExpiration;

  public JwtTokenProvider(
      @Value("${jwt.secret:mySecretKeyForJwtTokenGenerationAndValidation}") String secretKey,
      @Value("${jwt.expiration:86400000}") long jwtExpiration,
      @Value("${jwt.refresh-expiration:604800000}") long refreshExpiration) {
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    this.jwtExpiration = jwtExpiration;
    this.refreshExpiration = refreshExpiration;
  }

  public String generateAccessToken(Authentication authentication) {
    UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
    Date expiryDate = new Date(System.currentTimeMillis() + jwtExpiration);

    // Extract user ID from UserDetails (assuming it's stored in username field)
    String userId = userPrincipal.getUsername();

    return Jwts.builder()
        .subject(userId)
        .issuedAt(new Date())
        .expiration(expiryDate)
        .signWith(key)
        .compact();
  }

  public String generateRefreshToken(String userId) {
    Date expiryDate = new Date(System.currentTimeMillis() + refreshExpiration);

    return Jwts.builder()
        .subject(userId)
        .issuedAt(new Date())
        .expiration(expiryDate)
        .signWith(key)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
      return true;
    } catch (JwtException ex) {
      log.error("Invalid JWT token: {}", ex.getMessage());
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty");
    }
    return false;
  }

  public String getUserIdFromToken(String token) {
    Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    return claims.getSubject(); // User ID
  }

  public Long getUserIdAsLongFromToken(String token) {
    String userIdStr = getUserIdFromToken(token);
    try {
      return Long.parseLong(userIdStr);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid user ID in token: " + userIdStr, e);
    }
  }
}
