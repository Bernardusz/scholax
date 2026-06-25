package io.github.bernardusz.scholax.shared.upload;

import io.github.bernardusz.scholax.exception.exceptions.FailedUploadingFiles;
import io.github.bernardusz.scholax.exception.exceptions.FileNotFound;
import io.github.bernardusz.scholax.shared.upload.dto.UploadCreation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UploadService {
  private final UploadRepository uploadRepository;
  public UploadService(UploadRepository uploadRepository) {
    this.uploadRepository = uploadRepository;
  }

  @Transactional
  public Long saveUploadedFile(UploadCreation uploadCreation) {
    return uploadRepository.save(uploadCreation)
      .orElseThrow(() -> new FailedUploadingFiles("Failed to upload file"));
  }

  @Transactional(readOnly = true)
  public String findByUrlId(Long id) {
    return uploadRepository.findByUrlId(id).orElseThrow(() -> new FileNotFound("File doesn't exist or isn't found"));
  }

  @Transactional
  public boolean deleteById(Long id) {
    return uploadRepository.deleteById(id);
  }
}
