package io.github.bernardusz.scholax.user;

import java.time.LocalDateTime;

public record User(
  Long id,
  String email,
  String password, // Stores the encrypted BCrypt hash
  String fullName,
  UserRole role,     // 'STUDENT', 'TEACHER', ADMIN
  LocalDateTime createdAt,
  Long profilePictureId
) {}