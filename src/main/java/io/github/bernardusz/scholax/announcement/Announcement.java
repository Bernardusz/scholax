package io.github.bernardusz.scholax.announcement;

public record Announcement(
        Long id,
        String title,
        String content,
        Long classroomId,
        Long createdAt
) {}

