package io.github.bernardusz.scholax.announcement;

import io.github.bernardusz.scholax.announcement.dto.AnnouncementCreation;
import io.github.bernardusz.scholax.announcement.dto.AnnouncementUpdate;
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
@RequestMapping("/announcements")
public class AnnouncementController {
  private final AnnouncementService announcementService;

  public AnnouncementController(AnnouncementService announcementService) {
    this.announcementService = announcementService;
  }

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  @GetMapping("/create")
  public String getCreateAnnouncement(
      Model model,
      @RequestParam(name = "classroomId", required = false) Long classroomId
  ) {
    model.addAttribute("announcement", new AnnouncementCreation("", "", classroomId, LocalDateTime.now()));
    return "announcements/create";
  }

  @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
  @PostMapping("/create")
  public String createAnnouncement(
      @AuthenticationPrincipal UserSecurity user,
      @ModelAttribute("announcement") AnnouncementCreation announcementCreation
  ) {
    AnnouncementCreation creationWithTimestamp = new AnnouncementCreation(
        announcementCreation.title(),
        announcementCreation.content(),
        announcementCreation.classroomId(),
        LocalDateTime.now()
    );
    announcementService.createAnnouncement(creationWithTimestamp);
    return "redirect:/classrooms/" + announcementCreation.classroomId();
  }

  @PutMapping("/{id}")
  public String updateAnnouncement(
      @AuthenticationPrincipal UserSecurity user,
      @PathVariable Long id,
      @ModelAttribute AnnouncementUpdate announcementUpdate,
      RedirectAttributes redirectAttributes
  ) {
    Announcement announcement = announcementService.findById(id);
    if (user.getRole().equals("ADMIN") ||
        (user.getRole().equals("TEACHER") && announcementService.isUserAPartOfClassroom(user.getId(), announcement.classroomId()))
    ) {
      announcementService.updateAnnouncement(id, announcementUpdate);
      return "redirect:/classrooms/" + announcement.classroomId();
    }
    redirectAttributes.addFlashAttribute("error", new ErrorResponse(
        403,
        "You don't have the authority to edit this announcement",
        LocalDateTime.now()
    ));
    return "redirect:/classrooms/" + announcement.classroomId();
  }
}
