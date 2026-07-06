package io.github.bernardusz.scholax.submission;

import io.github.bernardusz.scholax.assignment.AssignmentService;
import io.github.bernardusz.scholax.submission.dto.SubmissionCreation;
import io.github.bernardusz.scholax.submission.dto.SubmissionSummary;
import io.github.bernardusz.scholax.exception.exceptions.SubmissionNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SubmissionService {
  private final SubmissionRepository submissionRepository;
  private final AssignmentService assignmentService;

  public SubmissionService(SubmissionRepository submissionRepository, AssignmentService assignmentService) {
    this.submissionRepository = submissionRepository;
    this.assignmentService = assignmentService;
  }

  @Transactional
  public Optional<Long> createSubmission(SubmissionCreation submissionCreation){
    Optional<Long> submissionId = submissionRepository.save(submissionCreation);
    if (submissionId.isPresent() && submissionCreation.uploadIds() != null){
      for (Long uploadId : submissionCreation.uploadIds()){
        submissionRepository.addUploadToSubmission(submissionId.get(), uploadId);
      }
    }
    return submissionId;
  }

  @Transactional(readOnly = true)
  public List<SubmissionSummary> findAllSubmissionsByAssignment(Long assignmentId, int limit, int offset){
    return submissionRepository.findAllByAssignmentId(assignmentId, limit, offset);
  }

  @Transactional(readOnly = true)
  public List<SubmissionSummary> findAllSubmissionsByUser(Long userId, int limit, int offset){
    return submissionRepository.findAllByUserId(userId, limit, offset);
  }

  @Transactional(readOnly = true)
  public Submission findById(Long submissionId){
    return submissionRepository.findById(submissionId).orElseThrow(
        () -> new SubmissionNotFound("Submission not found")
    );
  }

  @Transactional(readOnly = true)
  public Optional<Submission> findByUserIdAndAssignmentId(Long userId, Long assignmentId){
    return submissionRepository.findByUserIdAndAssignmentId(userId, assignmentId);
  }

  @Transactional
  public void updateSubmissionStatus(Long submissionId, SubmissionStatus status){
    submissionRepository.updateStatus(submissionId, status.name());
  }

  @Transactional
  public void deleteSubmissionById(Long submissionId){
    submissionRepository.deleteById(submissionId);
  }

  @Transactional
  public void addUploadToSubmission(Long submissionId, Long uploadId){
    submissionRepository.addUploadToSubmission(submissionId, uploadId);
  }

  @Transactional(readOnly = true)
  public List<Long> getUploadIdsBySubmissionId(Long submissionId){
    return submissionRepository.getUploadIdsBySubmissionId(submissionId);
  }
}
