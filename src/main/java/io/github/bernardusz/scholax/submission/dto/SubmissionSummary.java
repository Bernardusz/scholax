package io.github.bernardusz.scholax.submission.dto;

import io.github.bernardusz.scholax.submission.SubmissionStatus;

import java.time.LocalDateTime;

public record SubmissionSummary(
    Long id,
    Long userId,
    Long assignmentId,
    LocalDateTime submittedAt,
    SubmissionStatus status
) {}
