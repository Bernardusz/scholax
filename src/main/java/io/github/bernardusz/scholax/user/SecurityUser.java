package io.github.bernardusz.scholax.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.Collections;

public class SecurityUser implements UserDetails {
  private final User user;
  private final Collection<? extends GrantedAuthority> authorities;

  public SecurityUser(User user) {
    this.user = user;
    this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.role()));
  }
  public Long getId() {
    return user.id();
  }

  public String getFullName() {
    return user.fullName();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return user.password();
  }

  @Override
  public String getUsername() {
    return user.email(); // Your email acts as the login username
  }

  @Override public boolean isAccountNonExpired() { return true; }
  @Override public boolean isAccountNonLocked() { return true; }
  @Override public boolean isCredentialsNonExpired() { return true; }
  @Override public boolean isEnabled() { return true; }
}
