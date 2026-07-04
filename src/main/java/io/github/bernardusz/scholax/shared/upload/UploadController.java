package io.github.bernardusz.scholax.shared.upload;

import io.github.bernardusz.scholax.shared.upload.dto.UploadCreation;
import io.github.bernardusz.scholax.user.UserSecurity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/uploads")
public class UploadController {
  private final UploadService uploadService;
  public UploadController(UploadService uploadService) {
    this.uploadService = uploadService;
  }

  @PostMapping
  public ResponseEntity<Void> saveGoogleUpload(
    @AuthenticationPrincipal UserSecurity user,
    @RequestBody UploadCreation dto
  ){
    uploadService.saveUploadedFile(dto);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}")
  public String findUrlById(@PathVariable Long id) {
    return "redirect:" + uploadService.findUrlById(id);
  }
}
