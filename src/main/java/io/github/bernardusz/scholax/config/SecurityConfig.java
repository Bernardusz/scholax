package io.github.bernardusz.scholax.config;

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
          .requestMatchers("/teacher/**").hasRole("TEACHER") // Every teacher route will be left to teachers alone
          .requestMatchers("/student/**").hasRole("STUDENT") // Explicit Student Isolation
          .anyRequest().authenticated() // While every other path is shared
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
