package io.github.bernardusz.scholax.submission;

import io.github.bernardusz.scholax.submission.dto.SubmissionCreation;
import io.github.bernardusz.scholax.submission.dto.SubmissionSummary;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SubmissionRepository {
  private final JdbcClient jdbcClient;

  public SubmissionRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public Optional<Submission> findById(Long id) {
    return jdbcClient.sql(
        """
        SELECT * FROM submissions WHERE id = :id
        """
    ).param("id", id)
        .query(Submission.class)
        .optional();
  }

  public List<SubmissionSummary> findAllByAssignmentId(Long assignmentId, int limit, int offset){
    return jdbcClient.sql(
        """
        SELECT
          id,
          user_id,
          assignment_id,
          submitted_at,
          status
        FROM submissions
        WHERE assignment_id = :assignmentId
        LIMIT :limit OFFSET :offset
        """
    ).param("assignmentId", assignmentId)
        .param("limit", limit)
        .param("offset", offset)
        .query(SubmissionSummary.class)
        .list();
  }

  public List<SubmissionSummary> findAllByUserId(Long userId, int limit, int offset){
    return jdbcClient.sql(
        """
        SELECT
          id,
          user_id,
          assignment_id,
          submitted_at,
          status
        FROM submissions
        WHERE user_id = :userId
        LIMIT :limit OFFSET :offset
        """
    ).param("userId", userId)
        .param("limit", limit)
        .param("offset", offset)
        .query(SubmissionSummary.class)
        .list();
  }

  public Optional<Submission> findByUserIdAndAssignmentId(Long userId, Long assignmentId){
    return jdbcClient.sql(
        """
        SELECT * FROM submissions 
        WHERE user_id = :userId AND assignment_id = :assignmentId
        """
    ).param("userId", userId)
        .param("assignmentId", assignmentId)
        .query(Submission.class)
        .optional();
  }

  public Optional<Long> save(SubmissionCreation submissionCreation){
    return jdbcClient.sql(
        """
        INSERT INTO submissions (user_id, assignment_id, submitted_at, status)
        VALUES (:userId, :assignmentId, :submittedAt, :status)
        RETURNING id
        """
    ).param("userId", submissionCreation.userId())
        .param("assignmentId", submissionCreation.assignmentId())
        .param("submittedAt", submissionCreation.submittedAt())
        .param("status", submissionCreation.status().name())
        .query(Long.class)
        .optional();
  }

  public void updateStatus(Long submissionId, String status){
    jdbcClient.sql(
        """
        UPDATE submissions SET
          status = :status
        WHERE id = :submissionId
        """
    ).param("status", status)
        .param("submissionId", submissionId)
        .update();
  }

  public void deleteById(Long submissionId){
    jdbcClient.sql(
        """
        DELETE FROM submissions WHERE id = :submissionId
        """
    ).param("submissionId", submissionId)
        .update();
  }

  public void addUploadToSubmission(Long submissionId, Long uploadId){
    jdbcClient.sql(
        """
        INSERT INTO submission_uploads (submission_id, upload_id)
        VALUES (:submissionId, :uploadId)
        """
    ).param("submissionId", submissionId)
        .param("uploadId", uploadId)
        .update();
  }

  public List<Long> getUploadIdsBySubmissionId(Long submissionId){
    return jdbcClient.sql(
        """
        SELECT upload_id FROM submission_uploads WHERE submission_id = :submissionId
        """
    ).param("submissionId", submissionId)
        .query(Long.class)
        .list();
  }
}
