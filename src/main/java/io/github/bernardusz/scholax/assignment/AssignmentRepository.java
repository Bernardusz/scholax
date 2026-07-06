package io.github.bernardusz.scholax.assignment;

import io.github.bernardusz.scholax.assignment.dto.AssignmentCreation;
import io.github.bernardusz.scholax.assignment.dto.AssignmentSummary;
import io.github.bernardusz.scholax.assignment.dto.AssignmentUpdate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AssignmentRepository {
  private final JdbcClient jdbcClient;
  public AssignmentRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public Optional<Assignment> findById(Long id) {
    return jdbcClient.sql(
        """
        SELECT * FROM assignments WHERE id = :id
        """
    ).param("id", id)
        .query(Assignment.class)
        .optional();
  }

  public List<AssignmentSummary> findAllBySearch(String identifier, int limit, int offset){
    String whereClause = identifier == null ? null : "%" + identifier + "%";
    return jdbcClient.sql(
        """
        SELECT
          id,
          title,
          classroom_id,
          created_at,
          due_date
        FROM assignments
        WHERE (:whereClause IS NULL OR title ILIKE :whereClause)
        LIMIT :limit OFFSET :offset
        """
    ).param("whereClause", whereClause)
        .param("limit", limit)
        .param("offset", offset)
        .query(AssignmentSummary.class)
        .list();
  }

  public List<AssignmentSummary> findAllBySearchStudent(String identifier, int limit, int offset){
    String whereClause = identifier == null ? null : "%" + identifier + "%";
    return jdbcClient.sql(
            """
            SELECT
              id,
              title,
              classroom_id,
              created_at,
              due_date
            FROM assignments
            WHERE (:whereClause IS NULL OR title ILIKE :whereClause)
            LIMIT :limit OFFSET :offset
            """
        ).param("whereClause", whereClause)
        .param("limit", limit)
        .param("offset", offset)
        .query(AssignmentSummary.class)
        .list();
  }

  public List<AssignmentSummary> findAllByClassroomId(Long classroomId, String identifier, int limit, int offset){
    String whereClause = identifier == null ? null : "%" + identifier + "%";
    return jdbcClient.sql(
        """
        SELECT
          id,
          title,
          classroom_id,
          created_at,
          due_date
        FROM assignments
        WHERE 
          classroom_id = :classroomId AND
          (:whereClause IS NULL OR title ILIKE :whereClause)
        LIMIT :limit OFFSET :offset
        """
    ).param("classroomId", classroomId)
        .param("whereClause", whereClause)
        .param("limit", limit)
        .param("offset", offset)
        .query(AssignmentSummary.class)
        .list();
  }

  public Optional<Long> save(AssignmentCreation assignmentCreation){
    return jdbcClient.sql(
        """
        INSERT INTO assignments (title, description, classroom_id, created_at, due_date)
        VALUES (:title, :description, :classroomId, :createdAt, :dueDate)
        RETURNING id
        """
    ).param("title", assignmentCreation.title())
        .param("description", assignmentCreation.description())
        .param("classroomId", assignmentCreation.classroomId())
        .param("createdAt", assignmentCreation.createdAt())
        .param("dueDate", assignmentCreation.dueDate())
        .query(Long.class)
        .optional();
  }

  public void update(Long assignmentId, AssignmentUpdate assignmentUpdate){
    jdbcClient.sql(
        """
        UPDATE assignments SET
          title = :title,
          description = :description,
          due_date = :dueDate
        WHERE id = :assignmentId
        """
    ).param("title", assignmentUpdate.title())
        .param("description", assignmentUpdate.description())
        .param("dueDate", assignmentUpdate.dueDate())
        .param("assignmentId", assignmentId)
        .update();
  }

  public void deleteById(Long assignmentId){
    jdbcClient.sql(
        """
        DELETE FROM assignments WHERE id = :assignmentId
        """
    ).param("assignmentId", assignmentId)
        .update();
  }
}
