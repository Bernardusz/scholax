package io.github.bernardusz.scholax.auth;

import io.github.bernardusz.scholax.user.UserSecurity;
import io.github.bernardusz.scholax.user.User;
import io.github.bernardusz.scholax.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UserService userService;

  public CustomUserDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userService.loadUserByEmailSecurity(email);
    return new UserSecurity(user);
  }
}
