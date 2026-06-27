package io.github.bernardusz.scholax.dashboard;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardRepository {
  private final JdbcClient jdbcClient;
  public DashboardRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }
}
