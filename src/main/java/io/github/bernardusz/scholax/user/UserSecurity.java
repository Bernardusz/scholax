package io.github.bernardusz.scholax.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;

public class UserSecurity implements UserDetails {
  private final User user;
  private final Collection<? extends GrantedAuthority> authorities;

  public UserSecurity(User user) {
    this.user = user;

    String standardizedRole = user.role().name().toUpperCase().startsWith("ROLE_")
      ? user.role().name().toUpperCase()
      : "ROLE_" + user.role().name().toUpperCase();

    this.authorities = List.of(new SimpleGrantedAuthority(standardizedRole));
  }
  public Long getId() {
    return user.id();
  }

  public String getEmail() {
    return user.email();
  }

  @Override
  public String getUsername() {
    return getEmail(); // Your email acts as the login username
  }

  @Override
  public String getPassword() {
    return user.password();
  }

  public String getFullName() {
    return user.fullName();
  }

  public String getRole(){
    return user.role().name();
  }

  public Long getProfilePictureId() {
    return user.profilePictureId();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override public boolean isAccountNonExpired() { return true; }
  @Override public boolean isAccountNonLocked() { return true; }
  @Override public boolean isCredentialsNonExpired() { return true; }
  @Override public boolean isEnabled() { return true; }
}
