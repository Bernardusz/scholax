package io.github.bernardusz.scholax.submission;

import io.github.bernardusz.scholax.exception.dto.ErrorResponse;
import io.github.bernardusz.scholax.submission.dto.SubmissionCreation;
import io.github.bernardusz.scholax.user.UserSecurity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/submissions")
public class SubmissionController {
  private final SubmissionService submissionService;

  public SubmissionController(SubmissionService submissionService) {
    this.submissionService = submissionService;
  }

  @PreAuthorize("hasRole('STUDENT')")
  @GetMapping("/create")
  public String getCreateSubmission(
      Model model,
      @RequestParam(name = "assignmentId", required = false) Long assignmentId
  ) {
    model.addAttribute("submission", new SubmissionCreation(null, assignmentId, LocalDateTime.now(), io.github.bernardusz.scholax.submission.SubmissionStatus.PENDING, null));
    return "submissions/create";
  }

  @PreAuthorize("hasRole('STUDENT')")
  @PostMapping("/create")
  public String createSubmission(@ModelAttribute("submission") SubmissionCreation submissionCreation) {
    submissionService.createSubmission(submissionCreation);
    return "redirect:/submissions";
  }

  @GetMapping
  public String getAllSubmissions(
      @AuthenticationPrincipal UserSecurity user,
      @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
      @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
      Model model) {
    if (user.getRole().equals("STUDENT")){
      model.addAttribute("submissions", submissionService.findAllSubmissionsByUser(user.getId(), limit, offset));
    }
    else{
      model.addAttribute("submissions", submissionService.findAllSubmissionsByUser(user.getId(), limit, offset));
    }
    return "submissions/index";
  }

  @GetMapping("/assignment/{assignmentId}")
  public String getSubmissionsByAssignment(
      @AuthenticationPrincipal UserSecurity user,
      @PathVariable Long assignmentId,
      @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
      @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
      Model model,
      RedirectAttributes redirectAttributes
  ){
    if (user.getRole().equals("ADMIN") || user.getRole().equals("TEACHER")){
      model.addAttribute("submissions", submissionService.findAllSubmissionsByAssignment(assignmentId, limit, offset));
      return "submissions/by-assignment";
    }
    else {
      redirectAttributes.addFlashAttribute("error", new ErrorResponse(
          403,
          "You are not allowed to view submissions for this assignment",
          LocalDateTime.now()
      ));
      return "redirect:/submissions";
    }
  }

  @GetMapping("/{id}")
  public String getSubmissionById(
      @AuthenticationPrincipal UserSecurity user,
      @PathVariable Long id,
      Model model,
      RedirectAttributes redirectAttributes
  ){
    Submission submission = submissionService.findById(id);
    if (user.getRole().equals("ADMIN") || 
        user.getRole().equals("TEACHER") || 
        user.getId().equals(submission.userId())){
      model.addAttribute("submission", submission);
      model.addAttribute("uploadIds", submissionService.getUploadIdsBySubmissionId(id));
      return "submissions/detail";
    }
    else {
      redirectAttributes.addFlashAttribute("error", new ErrorResponse(
          403,
          "You are not allowed to view this submission",
          LocalDateTime.now()
      ));
      return "redirect:/submissions";
    }
  }

  @PutMapping("/{id}/status")
  public String updateSubmissionStatus(
      @AuthenticationPrincipal UserSecurity user,
      @PathVariable Long id,
      @RequestParam SubmissionStatus status,
      RedirectAttributes redirectAttributes
  ){
    Submission submission = submissionService.findById(id);
    if (user.getRole().equals("ADMIN") || user.getRole().equals("TEACHER")){
      submissionService.updateSubmissionStatus(id, status);
      return "redirect:/submissions/" + id;
    }
    redirectAttributes.addFlashAttribute("error", new ErrorResponse(
        403,
        "You don't have the authority to update this submission status",
        LocalDateTime.now()
    ));
    return "redirect:/submissions/" + id;
  }

  @DeleteMapping("/{id}")
  public String deleteSubmission(
      @AuthenticationPrincipal UserSecurity user,
      @PathVariable Long id,
      RedirectAttributes redirectAttributes
  ){
    Submission submission = submissionService.findById(id);
    if (user.getRole().equals("ADMIN") || 
        (user.getRole().equals("STUDENT") && user.getId().equals(submission.userId()))){
      submissionService.deleteSubmissionById(id);
      return "redirect:/submissions";
    }
    redirectAttributes.addFlashAttribute("error", new ErrorResponse(
        403,
        "You are not authorized to delete this submission",
        LocalDateTime.now()
    ));
    return "redirect:/submissions/" + id;
  }
}
