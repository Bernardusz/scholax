package io.github.bernardusz.scholax.user;

import io.github.bernardusz.scholax.user.dto.*;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
  private JdbcClient jdbcClient;

  public UserRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public Optional<User> loadUserByEmailSecurity(String email) throws UsernameNotFoundException {
    return jdbcClient
        .sql(
            """
                SELECT *
                FROM users
                WHERE email = :email
            """)
        .param("email", email)
        .query(User.class)
        .optional();
  }

  public boolean existsByEmail(String email) {
    return jdbcClient.sql("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
      .param("email", email)
      .query(Boolean.class)
      .single();
  }

  public Optional<Long> save(UserRegister user) {
    return jdbcClient.sql("""
                    INSERT INTO users (email, password, full_name, role)
                    VALUES (:email, :password, :fullName, :role)
                    RETURNING id
                """)
      .param("email", user.email())
      .param("password", user.password()) // Must be pre-hashed!
      .param("fullName", user.fullName())
      .param("role", user.role().name())
      .query(Long.class)
      .optional();
  }

  public List<UserSummary> findAll(int limit, int offset){
    return jdbcClient.sql(
      """
      SELECT id, full_name, role, profile_picture_id
      FROM users
      ORDER BY created_at DESC
      LIMIT :limit OFFSET :offset
      """
    )
      .param("limit", limit)
      .param("offset", offset)
      .query(UserSummary.class).list();
  }

  public List<UserSummary> findAllWithFilter(String search, UserRole role, int limit, int offset){
    String searchParam = (search == null || search.isBlank()) ? null : "%" + search.trim() + "%";
    String roleParam = (role == null) ? null : role.name();

    return jdbcClient.sql(
      """
      SELECT id, full_name, role, profile_picture_id
      FROM users
      WHERE 
        (:search::text IS NULL OR full_name::text ILIKE :search OR email::text ILIKE :search)
        AND (:role::text IS NULL OR role = :role::text)
      ORDER BY created_at DESC
      LIMIT :limit OFFSET :offset
      """
    ).param("search", searchParam)
      .param("role", roleParam)
      .param("limit", limit)
      .param("offset", offset)
      .query(UserSummary.class)
      .list();
  }

  public List<UserSummary> findAllByClassroom(Long classroomId){
    return jdbcClient.sql(
      """
      SELECT
        u.id,
        u.full_name,
        u.role,
        u.profile_picture_id,
      FROM users u
      INNER JOIN classroom_users cu ON u.id = cu.user_id
      WHERE cu.classroom_id = :classroomId
      """
    ).param("classroomId", classroomId)
      .query(UserSummary.class)
      .list();
  }

  public Optional<UserInformation> findById(Long userId){
    return jdbcClient.sql(
      """
      SELECT id, email, full_name, role, created_at, profile_picture_id
      FROM users
      WHERE id = :userId
      """
    ).param("userId", userId)
      .query(UserInformation.class)
      .optional();
  }

  public void updateInformation(Long userId, UserUpdateInformation userInformation){
    jdbcClient.sql(
      """
      UPDATE users SET
        email = :email,
        full_name = :fullName,
        role = :role
      WHERE id = :userId
      """
    ).param("userId", userId)
      .param("email", userInformation.email())
      .param("fullName", userInformation.fullName())
      .param("role", userInformation.role())
      .update();
  }

  public void updateProfilePicture(Long userId, UserUpdatePicture userProfilePicture){
    jdbcClient.sql(
      """
      UPDATE users SET
        profile_picture_id = :profilePictureId
      WHERE id = :userId
      """
    ).param("profilePictureId", userProfilePicture.profilePictureId())
      .param("userId", userId)
      .update();
  }

  public void updatePassword(Long userId, UserUpdatePassword userPassword){
    jdbcClient.sql(
      """
      UPDATE users SET
        password = :password
      WHERE id = :userId
      """
    ).param("password", userPassword.password())
      .param("userId", userId)
      .update();
  }

  public void deleteById(Long userId){
    jdbcClient.sql(
      """
      DELETE FROM users WHERE id = :userId
      """
    ).param("userId", userId)
      .update();
  }

}
