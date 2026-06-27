package io.github.bernardusz.scholax.user.dto;

import io.github.bernardusz.scholax.user.UserRole;

public record UserSummary(
  Long id,
  String fullName,
  UserRole role,     // 'STUDENT', 'TEACHER'
  Long profilePictureId
) {}
