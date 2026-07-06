package io.github.bernardusz.scholax.submission.dto;

import io.github.bernardusz.scholax.submission.SubmissionStatus;

import java.time.LocalDateTime;
import java.util.List;

public record SubmissionCreation(
    Long userId,
    Long assignmentId,
    LocalDateTime submittedAt,
    SubmissionStatus status,
    List<Long> uploadIds // The uploaded files
) {}
