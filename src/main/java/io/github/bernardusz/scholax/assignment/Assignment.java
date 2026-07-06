package io.github.bernardusz.scholax.assignment;

import java.time.LocalDateTime;

public record Assignment(
  Long id,
  String title,
  String description,
  Long classroomId,
  LocalDateTime createdAt,
  LocalDateTime dueDate
){}
