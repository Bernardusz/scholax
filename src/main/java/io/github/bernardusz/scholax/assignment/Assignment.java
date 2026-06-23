package io.github.bernardusz.scholax.assignment;

public record Assignment(
  Long id,
  String title,
  String description,
  Long classroomId,
  Long createdAt
){}
