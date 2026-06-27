package io.github.bernardusz.scholax.dashboard;

import org.springframework.stereotype.Service;

@Service
public class DashboardService {
  private final DashboardRepository dashboardRepository;
  public DashboardService(DashboardRepository dashboardRepository) {
    this.dashboardRepository = dashboardRepository;
  }
}
