package io.github.bernardusz.scholax.user.dto;


/**
 * User registration data transfer object that will be created by admin.
 *
 * @param email The user's email
 * @param password The user's password
 * @param fullName the user's full name
 * @param role The user's role
 */
public record UserRegister(
  String email,
  String password, // Stores the encrypted BCrypt hash
  String fullName,
  String role
) { }
