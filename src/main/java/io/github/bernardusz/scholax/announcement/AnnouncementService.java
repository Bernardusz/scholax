package io.github.bernardusz.scholax.announcement;

import io.github.bernardusz.scholax.announcement.dto.AnnouncementCreation;
import io.github.bernardusz.scholax.announcement.dto.AnnouncementUpdate;
import io.github.bernardusz.scholax.exception.exceptions.AnnouncementNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AnnouncementService {
  private final AnnouncementRepository announcementRepository;
  private final io.github.bernardusz.scholax.classroom.ClassroomRepository classroomRepository;

  public AnnouncementService(AnnouncementRepository announcementRepository, io.github.bernardusz.scholax.classroom.ClassroomRepository classroomRepository) {
    this.announcementRepository = announcementRepository;
    this.classroomRepository = classroomRepository;
  }

  @Transactional
  public Optional<Long> createAnnouncement(AnnouncementCreation announcementCreation) {
    return announcementRepository.save(announcementCreation);
  }

  @Transactional(readOnly = true)
  public Announcement findById(Long announcementId) {
    return announcementRepository.findById(announcementId).orElseThrow(
        () -> new AnnouncementNotFound("Announcement not found")
    );
  }

  @Transactional(readOnly = true)
  public boolean isUserAPartOfClassroom(Long userId, Long classroomId) {
    return classroomRepository.isUserAPartOfClassroom(userId, classroomId);
  }

  @Transactional
  public void updateAnnouncement(Long announcementId, AnnouncementUpdate announcementUpdate) {
    announcementRepository.update(announcementId, announcementUpdate);
  }
}
