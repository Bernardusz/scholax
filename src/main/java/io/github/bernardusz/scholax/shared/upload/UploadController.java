package io.github.bernardusz.scholax.shared.upload;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/uploads")
public class UploadController {
  private final UploadService uploadService;
  public UploadController(UploadService uploadService) {
    this.uploadService = uploadService;
  }

  @GetMapping("/{id}")
  public String findUrlById(@PathVariable Long id) {
    return "redirect:" + uploadService.findUrlById(id);
  }
}
