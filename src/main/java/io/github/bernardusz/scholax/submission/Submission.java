package io.github.bernardusz.scholax.submission;

import java.time.LocalDateTime;

public record Submission(
  Long id,
  Long userId,
  Long assignmentId,
  LocalDateTime submittedAt,
  SubmissionStatus status
){}
