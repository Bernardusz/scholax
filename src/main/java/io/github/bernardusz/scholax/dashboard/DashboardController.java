package io.github.bernardusz.scholax.dashboard;

import io.github.bernardusz.scholax.user.UserSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DashboardController {
  private final DashboardService dashboardService;
  public DashboardController(DashboardService dashboardService){
    this.dashboardService = dashboardService;
  }

  @GetMapping
  public String dashboard(@AuthenticationPrincipal UserSecurity user) {
    if (user == null) {
      return "redirect:/login";
    }

    return switch (user.getRole()) {
      case "STUDENT" -> "redirect:/students/dashboard";
      case "TEACHER" -> "redirect:/teachers/dashboard";
      case "ADMIN"   -> "redirect:/admins/dashboard";
      default        -> "redirect:/login";
    };
  }

  @GetMapping("/students/dashboard")
  public String studentDashboard() {
    return "students/dashboard";
  }

  @GetMapping("/teachers/dashboard")
  public String teacherDashboard() {
    return "teachers/dashboard";
  }

  @GetMapping("/admins/dashboard")
  public String adminDashboard() {
    return "admins/dashboard";
  }
}
