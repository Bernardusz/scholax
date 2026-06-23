package io.github.bernardusz.scholax.submission;

public record Submission(
  Long id,
  Long userId,
  Long assignmentId,
  Long submittedAt
){}
