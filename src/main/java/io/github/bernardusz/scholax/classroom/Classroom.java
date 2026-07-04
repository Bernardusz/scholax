package io.github.bernardusz.scholax.classroom;

import java.time.LocalDateTime;

public record Classroom(
  Long id,
  String name,
  String inviteCode,
  Long classroomCoverId,
  LocalDateTime createdAt
){ }