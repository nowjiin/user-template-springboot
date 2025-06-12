package com.example.usertemplate.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.usertemplate.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  @Query("SELECT u FROM User u WHERE u.enabled = :enabled")
  Page<User> findByEnabled(@Param("enabled") boolean enabled, Pageable pageable);

  @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword%")
  Page<User> findByUsernameContainingOrEmailContaining(
      @Param("keyword") String keyword, Pageable pageable);
}
