package io.github.bernardusz.scholax.announcement.dto;

import java.time.LocalDateTime;

public record AnnouncementCreation(
    String title,
    String content,
    Long classroomId,
    LocalDateTime createdAt
) { }
