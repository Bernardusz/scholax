package io.github.bernardusz.scholax.assignment;

import io.github.bernardusz.scholax.assignment.dto.AssignmentCreation;
import io.github.bernardusz.scholax.assignment.dto.AssignmentUpdate;
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
@RequestMapping("/assignments")
public class AssignmentController {
  private final AssignmentService assignmentService;

  public AssignmentController(AssignmentService assignmentService) {
    this.assignmentService = assignmentService;
  }

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  @GetMapping("/create")
  public String getCreateAssignment(
      Model model,
      @RequestParam(name = "classroomId", required = false) Long classroomId
  ) {
    model.addAttribute("assignment", new AssignmentCreation("", "", classroomId, LocalDateTime.now(), LocalDateTime.now()));
    return "assignments/create";
  }

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  @PostMapping("/create")
  public String createAssignment(@ModelAttribute("assignment") AssignmentCreation assignmentCreation) {
    assignmentService.createAssignment(assignmentCreation);
    return "redirect:/assignments";
  }

  @GetMapping
  public String getAllAssignments(
      @AuthenticationPrincipal UserSecurity user,
      @RequestParam(name = "title", required = false) String title,
      @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
      @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
      Model model) {
    if (user.getRole().equals("ADMIN")){
      model.addAttribute("assignments", assignmentService.findAllAssignments(title, limit, offset));
    }
    else{
      model.addAttribute("assignments", assignmentService.findAllAssignments(title, limit, offset));
    }
    return "assignments/index";
  }

  @GetMapping("/{id}")
  public String getAssignmentById(
      @AuthenticationPrincipal UserSecurity user,
      @PathVariable Long id,
      Model model,
      RedirectAttributes redirectAttributes
  ){
    Assignment assignment = assignmentService.findById(id);
    if (user.getRole().equals("ADMIN") || assignmentService.isUserAPartOfClassroom(user.getId(), assignment.classroomId())){
      model.addAttribute("assignment", assignment);
      return "assignments/detail";
    }
    else {
      redirectAttributes.addFlashAttribute("error", new ErrorResponse(
          403,
          "You are not allowed to view this assignment",
          LocalDateTime.now()
      ));
      return "redirect:/assignments";
    }
  }

  @PutMapping("/{id}")
  public String updateAssignment(
      @AuthenticationPrincipal UserSecurity user,
      @PathVariable Long id,
      @ModelAttribute AssignmentUpdate assignmentUpdate,
      RedirectAttributes redirectAttributes
  ){
    Assignment assignment = assignmentService.findById(id);
    if (user.getRole().equals("ADMIN") ||
        (user.getRole().equals("TEACHER") && assignmentService.isUserAPartOfClassroom(user.getId(), assignment.classroomId()))
    ){
      assignmentService.updateAssignment(id, assignmentUpdate);
      return "redirect:/assignments/" + id;
    }
    redirectAttributes.addFlashAttribute("error", new ErrorResponse(
        403,
        "You don't have the authority to edit this assignment",
        LocalDateTime.now()
    ));
    return "redirect:/assignments/" + id;
  }

  @DeleteMapping("/{id}")
  public String deleteAssignment(
      @AuthenticationPrincipal UserSecurity user,
      @PathVariable Long id,
      RedirectAttributes redirectAttributes
  ){
    Assignment assignment = assignmentService.findById(id);
    if (user.getRole().equals("ADMIN") ||
        (user.getRole().equals("TEACHER") && assignmentService.isUserAPartOfClassroom(user.getId(), assignment.classroomId()))
    ){
      assignmentService.deleteAssignmentById(id);
      return "redirect:/assignments";
    }
    redirectAttributes.addFlashAttribute("error", new ErrorResponse(
        403,
        "You are not authorized to delete this assignment",
        LocalDateTime.now()
    ));
    return "redirect:/assignments/" + id;
  }
}
