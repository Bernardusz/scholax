package io.github.bernardusz.scholax.classroom;

import io.github.bernardusz.scholax.classroom.dto.ClassroomCreation;
import io.github.bernardusz.scholax.classroom.dto.ClassroomSummary;
import io.github.bernardusz.scholax.classroom.dto.ClassroomUpdateCover;
import io.github.bernardusz.scholax.classroom.dto.ClassroomUpdateName;
import io.github.bernardusz.scholax.user.dto.UserUpdatePicture;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClassroomRepository  {
  private final JdbcClient jdbcClient;
  public ClassroomRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public boolean inviteCodeExist(String inviteCode){
    return jdbcClient.sql(
      "SELECT EXISTS (SELECT 1 FROM classrooms WHERE invite_code = :inviteCode)"
      ).param("inviteCode", inviteCode).query(Boolean.class)
      .single();
  }

  public Optional<Long> save(ClassroomCreation classroomName, String inviteCode){
    return jdbcClient.sql(
      """
      INSERT INTO classrooms (name, invite_code) VALUES (:name, :inviteCode)
      RETURNING id
      """
    ).param("name", classroomName.name())
      .param("inviteCode", inviteCode)
      .query(Long.class)
      .optional();
  }

  public List<ClassroomSummary> findAllRelatedWithFilter(String name,Long userId ,int limit, int offset){
    String searchParam = (name == null || name.isBlank()) ? null : "%" + name.trim() + "%";

    return jdbcClient.sql(
        """
        SELECT c.id, c.name, c.classroom_cover_id
        FROM classrooms c
        INNER JOIN classrooms_users cu ON c.id = cu.classroom_id
        WHERE 
          cu.user_id = :userId AND
          (:search::text IS NULL OR name ILIKE :search::text)
        ORDER BY c.created_at DESC
        LIMIT :limit OFFSET :offset
        """
      ).param("userId", userId)
      .param("limit", limit)
      .param("offset", offset)
        .param("search", searchParam)
      .query(ClassroomSummary.class)
      .list();
  }

  public List<ClassroomSummary> findAllWithFilter(String name, int limit, int offset){
    String searchParam = (name == null || name.isBlank()) ? null : "%" + name.trim() + "%";

    return jdbcClient.sql(
                    """
                    SELECT id, name, classroom_cover_id
                    FROM classrooms
                    WHERE 
                      :search::text IS NULL OR name ILIKE :search::text
                    ORDER BY created_at DESC
                    LIMIT :limit OFFSET :offset
                    """
            ).param("search", searchParam)
            .param("limit", limit)
            .param("offset", offset)
            .query(ClassroomSummary.class)
            .list();
  }

  public Optional<Classroom> findById(Long classroomId){
    return jdbcClient.sql(
        """
        SELECT * FROM classrooms
        WHERE id = :classroomId
        """
    ).param("classroomId", classroomId)
        .query(Classroom.class)
        .optional();
  }

  public void updateClassroomName(Long classroomId, ClassroomUpdateName classroomName){
    jdbcClient.sql(
        """
        UPDATE classrooms SET
          name = :classroomName
        WHERE id = :classroomId
        """
    ).param("classroomName", classroomName.name()).param("classroomId", classroomId).update();
  }

  public void updateClassroomCover(Long classroomId, ClassroomUpdateCover classroomCover){
    jdbcClient.sql(
        """
        UPDATE classrooms SET
          classroom_cover_id = :classroomCoverId
        WHERE id = :userId
        """
    ).param("classroomCoverId", classroomCover.classroomCoverId())
    .param("classroomId", classroomId)
    .update();
  }

  public void deleteClassroomById(Long classroomId){
    jdbcClient.sql(
      """
      DELETE FROM classrooms WHERE id = :classroomId
      """
    ).param("classroomId", classroomId)
        .update();
  }

  public Optional<Long> addUserToClassroom(Long userId, Long classroomId){
    return jdbcClient.sql(
        """
        INSERT INTO classrooms_users (user_id, classroom_id)
        VALUES (:userId, :classroomId)
        RETURNING classroomId
        """
    ).param("userId", userId)
        .param("classroomId", classroomId)
        .query(Long.class)
        .optional();
  }

  public void removeUserFromClassroom(Long userId, Long classroomId){
    jdbcClient.sql(
        """
        DELETE FROM classrooms_users
        WHERE 
          user_id = :userId AND
          classroom_id = :classroomId
        """
    ).param("userId", userId)
        .param("classroomId", classroomId)
        .update();
  }

  public boolean isUserAPartOfClassroom(Long userId, Long classroomId){
    return jdbcClient.sql(
        """
        SELECT EXISTS (SELECT 1 FROM classrooms_users WHERE user_id = :userId AND classroom_id = :classroomId)
        """
    ).param("userId", userId)
        .param("classroomId", classroomId)
        .query(Boolean.class)
        .single();
  }
}