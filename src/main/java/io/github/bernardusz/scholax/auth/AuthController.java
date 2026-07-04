package io.github.bernardusz.scholax.auth;

import io.github.bernardusz.scholax.user.User;
import io.github.bernardusz.scholax.user.UserRole;
import io.github.bernardusz.scholax.user.UserSecurity;
import io.github.bernardusz.scholax.user.UserService;
import io.github.bernardusz.scholax.user.dto.UserRegister;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  public  AuthController(UserService userService, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping("/admins/users/create")
  public String registerPage(@AuthenticationPrincipal UserSecurity user, Model model) {
    model.addAttribute("users", new UserRegister("", "", "", UserRole.STUDENT));
    return "auth/register"; // Looks for src/main/resources/templates/register.html
  }

  @PostMapping("/admins/users/create")
  public String registerUser(@ModelAttribute("users") UserRegister registerDto, Model model) {
    if (userService.existsByEmail(registerDto.email())) {
      model.addAttribute("error", "Email is already registered!");
      return "auth/register";
    }

    // Convert DTO to domain object while hashing the password
    UserRegister newUser = new UserRegister(
      registerDto.email(),
      passwordEncoder.encode(registerDto.password()),
      registerDto.fullName(),
      registerDto.role()
    );

    userService.save(newUser);
    return "redirect:/";
  }

  @GetMapping("/login")
  public String loginPage(@AuthenticationPrincipal UserSecurity user) {
    if (user != null){
      return "redirect:/";
    }
    return "auth/login";
  }
}
