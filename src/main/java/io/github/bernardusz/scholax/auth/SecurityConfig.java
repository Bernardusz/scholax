package io.github.bernardusz.scholax.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(
        authorize ->
          authorize
            .requestMatchers("/login").permitAll()
            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Let assets pass through
            .requestMatchers("/teachers/**").hasRole("TEACHER") // Every teachers route will be left to teachers alone
            .requestMatchers("/students/**").hasRole("STUDENT") // Explicit Student Isolation
            .requestMatchers("/admins/**").hasRole("ADMIN") // Explicit Admin Isolation
            .requestMatchers("/register").hasRole("ADMIN") // Only admin can register users
            .anyRequest().authenticated() // While every other path is protected
      )
      .formLogin(formLogin -> formLogin
        .loginPage("/login")
        .defaultSuccessUrl("/", true)
        .permitAll()
      )
      .sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        .sessionFixation(fixation -> fixation.newSession())
        .maximumSessions(1)
      )
      .logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .permitAll()
      )
      .httpBasic(Customizer.withDefaults());
    return  http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
