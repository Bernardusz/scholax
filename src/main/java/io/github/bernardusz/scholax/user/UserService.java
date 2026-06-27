package io.github.bernardusz.scholax.user;

import io.github.bernardusz.scholax.exception.exceptions.UserNotFound;
import io.github.bernardusz.scholax.user.dto.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
  private final UserRepository userRepository;
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public User loadUserByEmailSecurity(String email) {
    return userRepository
        .loadUserByEmailSecurity(email)
        .orElseThrow(() -> new UsernameNotFoundException("No users found with email: " + email));
  }

  @Transactional(readOnly = true)
  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public List<UserSummary> findAllWithFilter(String search, UserRole role, int limit, int offset) {
    return userRepository.findAllWithFilter(search, role, limit, offset);
  }

  @Transactional(readOnly = true)
  public List<UserSummary> findAllByClassroom(Long classroomId){
    return userRepository.findAllByClassroom(classroomId);
  }

  @Transactional(readOnly = true)
  public UserInformation findById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFound("User not found"));
  }

  @Transactional
  public Optional<Long> save(UserRegister user) {
    return userRepository.save(user);
  }

  @Transactional
  public void updateInformation(Long userId, UserUpdateInformation userInformation) {
    userRepository.updateInformation(userId, userInformation);
  }

  @Transactional
  public void updateProfilePicture(Long userId, UserUpdatePicture userProfilePicture){
    userRepository.updateProfilePicture(userId, userProfilePicture);
  }

  @Transactional
  public void updatePassword(Long userId, UserUpdatePassword newPassword) {
    userRepository.updatePassword(userId, newPassword);
  }

  @Transactional
  public void deleteById(Long userId){
    userRepository.deleteById(userId);
  }
}
