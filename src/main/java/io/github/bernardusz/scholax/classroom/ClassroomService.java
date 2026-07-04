package io.github.bernardusz.scholax.classroom;

import io.github.bernardusz.scholax.classroom.dto.ClassroomCreation;
import io.github.bernardusz.scholax.classroom.dto.ClassroomSummary;
import io.github.bernardusz.scholax.classroom.dto.ClassroomUpdateCover;
import io.github.bernardusz.scholax.classroom.dto.ClassroomUpdateName;
import io.github.bernardusz.scholax.exception.exceptions.ClassroomNotFound;
import io.github.bernardusz.scholax.exception.exceptions.FailedAddingUser;
import io.github.bernardusz.scholax.shared.util.InviteCodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClassroomService  {
  private final ClassroomRepository classroomRepository;
  private final InviteCodeGenerator inviteCodeGenerator;
  public ClassroomService(ClassroomRepository classroomRepository, InviteCodeGenerator inviteCodeGenerator) {
    this.classroomRepository = classroomRepository;
    this.inviteCodeGenerator = inviteCodeGenerator;
  }

  @Transactional
  public Optional<Long> createNewClassroom(ClassroomCreation classroomCreation){
    String inviteCode = inviteCodeGenerator.generateUniqueCode();
    return classroomRepository.save(classroomCreation, inviteCode);
  }

  @Transactional(readOnly = true)
  public List<ClassroomSummary> findAllClassroomRelatedToUser(String name, Long userId, int limit, int offset){
    return classroomRepository.findAllRelatedWithFilter(name, userId, limit, offset);
  }

  @Transactional(readOnly = true)
  public List<ClassroomSummary> findAllClassroomWithFilter(String name, int limit, int offset){
    return classroomRepository.findAllWithFilter(name, limit, offset);
  }

  @Transactional(readOnly = true)
  public boolean isUserAPartOfClassroom(Long userId, Long classroomId){
    return classroomRepository.isUserAPartOfClassroom(userId, classroomId);
  }

  @Transactional(readOnly = true)
  public Classroom findById(Long classroomId){
    return classroomRepository.findById(classroomId).orElseThrow(
        () -> new ClassroomNotFound("Classroom not found")
    );
  }

  @Transactional
  public void updateClassroomName(Long classroomId, ClassroomUpdateName classroomName){
    classroomRepository.updateClassroomName(classroomId, classroomName);
  }

  @Transactional
  public void updateClassroomCover(Long classroomId, ClassroomUpdateCover userProfilePicture){
    classroomRepository.updateClassroomCover(classroomId, userProfilePicture);
  }

  @Transactional
  public void deleteClassroomById(Long classroomId){
    classroomRepository.deleteClassroomById(classroomId);
  }

  @Transactional
  public Long addUserToClassroom(Long userId, Long classroomId){
    return classroomRepository.addUserToClassroom(userId, classroomId).orElseThrow(
        () -> new FailedAddingUser("Failed to add user to classroom")
    );
  }

  @Transactional
  public void removeUserFromClassroom(Long userId, Long classroomId){
    classroomRepository.removeUserFromClassroom(userId, classroomId);
  }

}