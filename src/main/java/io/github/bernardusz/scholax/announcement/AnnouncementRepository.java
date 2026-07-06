package io.github.bernardusz.scholax.announcement;

import io.github.bernardusz.scholax.announcement.dto.AnnouncementCreation;
import io.github.bernardusz.scholax.announcement.dto.AnnouncementUpdate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AnnouncementRepository {
  private final JdbcClient jdbcClient;

  public AnnouncementRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public Optional<Announcement> findById(Long id) {
    return jdbcClient.sql(
        """
        SELECT * FROM announcements WHERE id = :id
        """
    ).param("id", id)
        .query(Announcement.class)
        .optional();
  }

  public Optional<Long> save(AnnouncementCreation announcementCreation) {
    return jdbcClient.sql(
        """
        INSERT INTO announcements (title, content, classroom_id, created_at)
        VALUES (:title, :content, :classroomId, :createdAt)
        RETURNING id
        """
    ).param("title", announcementCreation.title())
        .param("content", announcementCreation.content())
        .param("classroomId", announcementCreation.classroomId())
        .param("createdAt", announcementCreation.createdAt())
        .query(Long.class)
        .optional();
  }

  public void update(Long announcementId, AnnouncementUpdate announcementUpdate) {
    jdbcClient.sql(
        """
        UPDATE announcements SET
          title = :title,
          content = :content
        WHERE id = :announcementId
        """
    ).param("title", announcementUpdate.title())
        .param("content", announcementUpdate.content())
        .param("announcementId", announcementId)
        .update();
  }
}
