package io.github.bernardusz.scholax.exception;

import io.github.bernardusz.scholax.exception.dto.ErrorResponse;
import io.github.bernardusz.scholax.exception.exceptions.FailedUploadingFiles;
import io.github.bernardusz.scholax.exception.exceptions.FileNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class AjaxGlobalExceptionHandler {

  @ExceptionHandler(FailedUploadingFiles.class)
  public ResponseEntity<ErrorResponse> handleFailedUploadingFiles(FailedUploadingFiles ex) {
    return ResponseEntity.internalServerError().body(new ErrorResponse(
      ex.getErrorCode(),
      ex.getMessage(),
      LocalDateTime.now()
    ));
  }

  @ExceptionHandler(FileNotFound.class)
  public ResponseEntity<ErrorResponse> handleFileNotFound(FileNotFound ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(new ErrorResponse(
        ex.getErrorCode(),
        ex.getMessage(),
        LocalDateTime.now()
      )
    );
  }
}
