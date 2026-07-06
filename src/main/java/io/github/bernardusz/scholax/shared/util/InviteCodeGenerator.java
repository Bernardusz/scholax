package io.github.bernardusz.scholax.shared.util;

import io.github.bernardusz.scholax.classroom.ClassroomRepository;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class InviteCodeGenerator {
  private final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private final SecureRandom random = new SecureRandom();
  private final int length = 10;
  private final ClassroomRepository classroomRepository;

  public InviteCodeGenerator(ClassroomRepository classroomService) {
    this.classroomRepository = classroomService;
  }

  /**
   * Generates a 10-character code and guarantees its uniqueness
   * by checking the database before returning.
   */
  public String generateUniqueCode() {
    String code;
    boolean isDuplicate;

    do {
      code = generateRawCode(10);
      isDuplicate = classroomRepository.inviteCodeExist(code);
    } while (isDuplicate); // If it exists, loop again and try a new one!

    return code;
  }

  private String generateRawCode(int length) {
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int index = random.nextInt(ALPHA_NUMERIC.length());
      sb.append(ALPHA_NUMERIC.charAt(index));
    }
    return sb.toString();
  }
}
