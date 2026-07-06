package io.github.bernardusz.scholax.assignment;

import io.github.bernardusz.scholax.assignment.dto.AssignmentCreation;
import io.github.bernardusz.scholax.assignment.dto.AssignmentSummary;
import io.github.bernardusz.scholax.assignment.dto.AssignmentUpdate;
import io.github.bernardusz.scholax.exception.exceptions.AssignmentNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {
  private final AssignmentRepository assignmentRepository;
  private final io.github.bernardusz.scholax.classroom.ClassroomRepository classroomRepository;

  public AssignmentService(AssignmentRepository assignmentRepository, io.github.bernardusz.scholax.classroom.ClassroomRepository classroomRepository) {
    this.assignmentRepository = assignmentRepository;
    this.classroomRepository = classroomRepository;
  }

  @Transactional
  public Optional<Long> createAssignment(AssignmentCreation assignmentCreation){
    return assignmentRepository.save(assignmentCreation);
  }

  @Transactional(readOnly = true)
  public List<AssignmentSummary> findAllAssignments(String identifier, int limit, int offset){
    return assignmentRepository.findAllBySearch(identifier, limit, offset);
  }

  @Transactional(readOnly = true)
  public List<AssignmentSummary> findAllAssignmentsByClassroom(Long classroomId, String identifier, int limit, int offset){
    return assignmentRepository.findAllByClassroomId(classroomId, identifier, limit, offset);
  }

  @Transactional(readOnly = true)
  public Assignment findById(Long assignmentId){
    return assignmentRepository.findById(assignmentId).orElseThrow(
        () -> new AssignmentNotFound("Assignment not found")
    );
  }

  @Transactional(readOnly = true)
  public boolean isUserAPartOfClassroom(Long userId, Long classroomId){
    return classroomRepository.isUserAPartOfClassroom(userId, classroomId);
  }

  @Transactional
  public void updateAssignment(Long assignmentId, AssignmentUpdate assignmentUpdate){
    assignmentRepository.update(assignmentId, assignmentUpdate);
  }

  @Transactional
  public void deleteAssignmentById(Long assignmentId){
    assignmentRepository.deleteById(assignmentId);
  }
}
