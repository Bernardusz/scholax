package io.github.bernardusz.scholax.assignment.dto;

import java.time.LocalDateTime;

public record AssignmentCreation(
    String title,
    String description,
    Long classroomId,
    LocalDateTime createdAt,
    LocalDateTime dueDate
) {}
