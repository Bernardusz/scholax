package io.github.bernardusz.scholax.classroom;

import io.github.bernardusz.scholax.classroom.dto.ClassroomCreation;
import io.github.bernardusz.scholax.classroom.dto.ClassroomUpdateCover;
import io.github.bernardusz.scholax.classroom.dto.ClassroomUpdateName;
import io.github.bernardusz.scholax.exception.dto.ErrorResponse;
import io.github.bernardusz.scholax.user.UserSecurity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/classrooms")
public class ClassroomController  {
  private final ClassroomService classroomService;
  public ClassroomController(ClassroomService classroomService) {
    this.classroomService = classroomService;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/create")
  public String getCreateClassroom(Model model) {
    model.addAttribute("classroom", new ClassroomCreation(""));
    return "classrooms/create";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/create")
  public String createClassroom(@ModelAttribute("classroom") ClassroomCreation classroomCreation) {
    classroomService.createNewClassroom(classroomCreation);
    return "redirect:/classrooms";
  }

  @GetMapping
  public String getAllClassrooms(
      @AuthenticationPrincipal UserSecurity user,
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
      @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
      Model model) {
    if (user.getRole().equals("ADMIN")){
      model.addAttribute("classrooms", classroomService.findAllClassroomWithFilter(name, limit, offset));
    }
    else{
      model.addAttribute("classrooms", classroomService.findAllClassroomRelatedToUser(name, user.getId(), limit, offset));
    }
    return "classrooms/index";
  }

  @GetMapping("/{id}")
  public String getClassroomById(
      @AuthenticationPrincipal UserSecurity user,
      @PathVariable Long id,
      Model model,
      RedirectAttributes redirectAttributes
  ){
    if (user.getRole().equals("ADMIN") || classroomService.isUserAPartOfClassroom(user.getId(), id)){
      model.addAttribute(classroomService.findById(id));
      return "classrooms/detail";
    }
    else {
      redirectAttributes.addFlashAttribute("error", new ErrorResponse(
          403,
          "You are not allowed to open this classroom",
          LocalDateTime.now()
      ));
      return "redirect:/classrooms";
    }
  }

  @PutMapping("/{id}/name")
  public String updateClassroomName(
      @AuthenticationPrincipal UserSecurity user,
      @PathVariable(value = "id") Long id,
      @ModelAttribute ClassroomUpdateName classroomName,
      RedirectAttributes redirectAttributes
    ){
    if (user.getRole().equals("ADMIN") ||
        (user.getRole().equals("TEACHER") && classroomService.isUserAPartOfClassroom(user.getId(), id))
    ){
      classroomService.updateClassroomName(id, classroomName);
      return "redirect:/classrooms/" + id;
    }
    redirectAttributes.addFlashAttribute("error", new ErrorResponse(
        403,
        "You doesn't have the authority to edit this class' name",
        LocalDateTime.now()
    ));
    return "redirect:/classrooms/" + id;
  }

  @PutMapping("/{id}/cover")
  public String editUserProfilePicture(
      @AuthenticationPrincipal UserSecurity user,
      @PathVariable Long id,
      @ModelAttribute ClassroomUpdateCover classroomCover,
      RedirectAttributes redirectAttributes
  ){
    if (user.getRole().equals("ADMIN") ||
        (user.getRole().equals("TEACHER")
            && classroomService.isUserAPartOfClassroom(user.getId(), id)))
    {
      classroomService.updateClassroomCover(id, classroomCover);
      return "redirect:/users/" + id;
    }
    redirectAttributes.addFlashAttribute("error", new ErrorResponse(
        403,
        "You are not authorized to update this users",
        LocalDateTime.now()
    ));
    return "redirect:/classrooms/" + id;
  }

  @DeleteMapping("/{id}")
  public String deleteClassroom(
      @AuthenticationPrincipal UserSecurity user,
      @PathVariable Long id,
      RedirectAttributes redirectAttributes
  ){
    if (user.getRole().equals("ADMIN")){
      classroomService.deleteClassroomById(id);
      return "redirect:/classrooms/";
    }
    redirectAttributes.addFlashAttribute("error", new ErrorResponse(
        403,
        "You are not authorized to delete this class",
        LocalDateTime.now()
    ));

    return "redirect:/classrooms/" + id;
  }

  @PostMapping("/{id}/users")
  public String addUserToClassroom(
      @AuthenticationPrincipal UserSecurity user,
      @PathVariable Long id,
      @RequestParam Long userId,
      RedirectAttributes redirectAttributes
  ){
    if (user.getRole().equals("ADMIN") ||
        (user.getRole().equals("TEACHER") && classroomService.isUserAPartOfClassroom(user.getId(), id))
    ){
      classroomService.addUserToClassroom(userId, id);
      return "redirect:/classrooms/" + id;
    }
    redirectAttributes.addFlashAttribute("error", new ErrorResponse(
        403,
        "You are not authorized to add users to this classroom",
        LocalDateTime.now()
    ));
    return "redirect:/classrooms/" + id;
  }
}