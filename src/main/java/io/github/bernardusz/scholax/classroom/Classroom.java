package io.github.bernardusz.scholax.classroom;

public record Classroom(
  Long id,
  String name,
  String inviteCode,
  Long classroomCoverId,
  Long createdAt
){ }