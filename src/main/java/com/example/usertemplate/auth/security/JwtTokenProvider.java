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

    return Jwts.builder()
        .subject(userPrincipal.getUsername())
        .issuedAt(new Date())
        .expiration(expiryDate)
        .signWith(key)
        .compact();
  }

  public String generateRefreshToken(String username) {
    Date expiryDate = new Date(System.currentTimeMillis() + refreshExpiration);

    return Jwts.builder()
        .subject(username)
        .issuedAt(new Date())
        .expiration(expiryDate)
        .signWith(key)
        .compact();
  }

  public String getUsernameFromToken(String token) {
    Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

    return claims.getSubject();
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

  public long getExpirationTime() {
    return jwtExpiration;
  }
}
