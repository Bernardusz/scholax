package io.github.bernardusz.scholax.user;

import io.github.bernardusz.scholax.exception.dto.ErrorResponse;
import io.github.bernardusz.scholax.user.dto.UserUpdateInformation;
import io.github.bernardusz.scholax.user.dto.UserUpdatePassword;
import io.github.bernardusz.scholax.user.dto.UserUpdatePicture;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public String getAllUsers(
    @RequestParam(required = false) String search,
    @RequestParam(required = false) UserRole role,
    @RequestParam(defaultValue = "10") int limit,
    @RequestParam(defaultValue = "0") int offset,
    Model model
  ) {
    model.addAttribute("users", userService.findAllWithFilter(search, role, limit, offset));
    return "users/users";
  }

  @GetMapping("/{id}")
  public String getUserById(
    @AuthenticationPrincipal UserSecurity user,
    @PathVariable Long id,
    RedirectAttributes redirectAttributes,
    Model model
  ) {
    if (user.getRole().equals("ADMIN") || user.getId().equals(id)){
      model.addAttribute("userData", userService.findById(id));
      return "users/user_data";
    }
    redirectAttributes.addFlashAttribute("error", new ErrorResponse(
      403,
      "You are not authorized to view this users",
      LocalDateTime.now()
    ));
    return "redirect:/users/" + user.getId();
  }

  @GetMapping("/{id}/information")
  public String getEditUserInformation(
    @AuthenticationPrincipal UserSecurity user,
    @PathVariable Long id,
    RedirectAttributes redirectAttributes,
    Model model
  ) {
    if (user.getId().equals(id)){
      model.addAttribute("userData", userService.findById(id));
      return "users/user_update";
    }
    redirectAttributes.addFlashAttribute("error", new ErrorResponse(
      403,
      "You are not authorized to view this users",
      LocalDateTime.now()
    ));
    return "redirect:/users/" + user.getId();
  }

  @PutMapping("/{id}/information")
  public String editUserInformation(
    @AuthenticationPrincipal UserSecurity user,
    @PathVariable Long id,
    @ModelAttribute UserUpdateInformation userInformation,
    RedirectAttributes redirectAttributes
  ){
    if (user.getId().equals(id)){
      userService.updateInformation(id, userInformation);
      return "redirect:/users/" + id;
    }
    redirectAttributes.addFlashAttribute("error", new ErrorResponse(
      403,
      "You are not authorized to update this users",
      LocalDateTime.now()
    ));
    return "redirect:/users/" + user.getId();
  }

  @PutMapping("/{id}/profile-picture")
  public String editUserProfilePicture(
    @AuthenticationPrincipal UserSecurity user,
    @PathVariable Long id,
    @ModelAttribute UserUpdatePicture userProfilePicture,
    RedirectAttributes redirectAttributes
  ){
    if (user.getId().equals(id)){
      userService.updateProfilePicture(id, userProfilePicture);
      return "redirect:/users/" + id;
    }
    redirectAttributes.addFlashAttribute("error", new ErrorResponse(
      403,
      "You are not authorized to update this users",
      LocalDateTime.now()
    ));
    return "redirect:/users/" + user.getId();
  }

  @GetMapping("/{id}/password")
  public String getEditUserPassword(
    @AuthenticationPrincipal UserSecurity user,
    @PathVariable Long id,
    RedirectAttributes redirectAttributes
  ){
    if (user.getRole().equals("ADMIN") || user.getId().equals(id)){
      return "users/user_password";
    }
    redirectAttributes.addFlashAttribute("error", new ErrorResponse(
      403,
      "You are not authorized to update this users",
      LocalDateTime.now()
    ));
    return "redirect:/users/" + id;
  }

  @PutMapping("/{id}/password")
  public String editUserPassword(
    @AuthenticationPrincipal UserSecurity user,
    @PathVariable Long id,
    @ModelAttribute UserUpdatePassword newPassword,
    RedirectAttributes redirectAttributes
  ){
    if (user.getRole().equals("ADMIN") || user.getId().equals(id)){
      userService.updatePassword(id, newPassword);
      return "redirect:/users/" + id;
    }
    redirectAttributes.addFlashAttribute("error", new ErrorResponse(
      403,
      "You are not authorized to update this users",
      LocalDateTime.now()
    ));
    return "redirect:/users/" + id;
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public String deleteUser(@PathVariable Long id){
    userService.deleteById(id);
    return "redirect:/users";
  }
}