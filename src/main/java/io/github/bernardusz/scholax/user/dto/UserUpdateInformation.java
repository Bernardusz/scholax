package io.github.bernardusz.scholax.user.dto;

import io.github.bernardusz.scholax.user.UserRole;

public record UserUpdateInformation (
  String email,
  String fullName,
  UserRole role     // 'STUDENT', 'TEACHER'
) { }
