package io.github.bernardusz.scholax.user.dto;


import io.github.bernardusz.scholax.user.UserRole;

/**
 * User registration data transfer object that will be created by admin.
 *
 * @param email The users's email
 * @param password The users's password
 * @param fullName the users's full name
 * @param role The users's role
 */
public record UserRegister(
  String email,
  String password, // Stores the encrypted BCrypt hash
  String fullName,
  UserRole role      // 'STUDENT', 'TEACHER', 'ADMIN
) { }
