package io.github.bernardusz.scholax.submission.dto;

import io.github.bernardusz.scholax.submission.SubmissionStatus;

import java.time.LocalDateTime;
import java.util.List;

public record SubmissionDetail(
    Long id,
    Long userId,
    Long assignmentId,
    LocalDateTime submittedAt,
    SubmissionStatus status,
    List<Long> uploadIds // For Teacher/Admin it must be every submission to this assignment
    // For students their assignment only
) {}
