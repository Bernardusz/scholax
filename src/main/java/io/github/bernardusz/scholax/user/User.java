package io.github.bernardusz.scholax.user;

import java.time.LocalDateTime;

public record User(
  Long id,
  String email,
  String password, // Stores the encrypted BCrypt hash
  String fullName,
  String role,     // 'STUDENT', 'TEACHER', or 'ADMIN'
  LocalDateTime createdAt
) {}