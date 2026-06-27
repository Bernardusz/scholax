package io.github.bernardusz.scholax.user.dto;

import io.github.bernardusz.scholax.user.UserRole;

import java.time.LocalDateTime;

public record UserInformation(
  Long id,
  String email,
  String fullName,
  UserRole role,     // 'STUDENT', 'TEACHER'
  LocalDateTime createdAt,
  Long profilePictureId
){}
