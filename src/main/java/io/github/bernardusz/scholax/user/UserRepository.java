package io.github.bernardusz.scholax.user;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
  private JdbcClient jdbcClient;

  public UserRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return jdbcClient
        .sql(
            """
                SELECT id, email, password, full_name, role, created_at
                FROM users
                WHERE email = :email
            """)
        .param("email", email)
        .query(
            (rs, rowNum) ->
                new User(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("full_name"),
                    rs.getString("role"),
                    rs.getTimestamp("created_at").toLocalDateTime()))
        .optional()
        .map(SecurityUser::new) // If found, wrap our custom record into SecurityUser
        .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));
  }

  public boolean existsByEmail(String email) {
    return jdbcClient.sql("SELECT COUNT(*) FROM users WHERE email = :email")
      .param("email", email)
      .query(Integer.class)
      .single() > 0;
  }

  public void save(User user) {
    jdbcClient.sql("""
                    INSERT INTO users (email, password, full_name, role)
                    VALUES (:email, :password, :fullName, :role)
                """)
      .param("email", user.email())
      .param("password", user.password()) // Must be pre-hashed!
      .param("fullName", user.fullName())
      .param("role", user.role())
      .update();
  }

}
