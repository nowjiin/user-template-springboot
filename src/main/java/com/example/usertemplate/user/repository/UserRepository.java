package com.example.usertemplate.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.usertemplate.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}
