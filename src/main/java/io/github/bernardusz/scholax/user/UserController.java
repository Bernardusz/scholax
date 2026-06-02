package io.github.bernardusz.scholax.user;

import io.github.bernardusz.scholax.user.dto.UserRegister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping("/login")
  public String loginPage() {
    return "login"; // Looks for src/main/resources/templates/login.html
  }

  @GetMapping("/register")
  public String registerPage(Model model) {
    model.addAttribute("user", new UserRegister("", "", "", "STUDENT"));
    return "register"; // Looks for src/main/resources/templates/register.html
  }

  @PostMapping("/register")
  public String registerUser(@ModelAttribute("user") UserRegister registerDto, Model model) {
    if (userRepository.existsByEmail(registerDto.email())) {
      model.addAttribute("error", "Email is already registered!");
      return "register";
    }

    // Convert DTO to domain object while hashing the password
    User newUser = new User(
      null,
      registerDto.email(),
      passwordEncoder.encode(registerDto.password()),
      registerDto.fullName(),
      registerDto.role().toUpperCase(),
      null
    );

    userRepository.save(newUser);
    return "redirect:/login?registered";
  }
}