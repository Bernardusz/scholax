package io.github.bernardusz.scholax.assignment.dto;

import java.time.LocalDateTime;

public record AssignmentSummary(
    Long id,
    String title,
    Long classroomId,
    LocalDateTime createdAt,
    LocalDateTime dueDate
) { }
