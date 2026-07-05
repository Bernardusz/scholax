package io.github.bernardusz.scholax.assignment.dto;

import java.time.LocalDateTime;

public record AssignmentUpdate(
    String title,
    String description,
    LocalDateTime dueDate
) {}
