package com.dux.challenge.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private JwtFilter jwtFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .exceptionHandling(ex -> ex
        .authenticationEntryPoint((req, res, authEx) ->
          res.sendError(HttpServletResponse.SC_UNAUTHORIZED, authEx.getMessage())
        )
      )
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/auth/login", "/h2-console/**").permitAll()
        .anyRequest().authenticated()
      )
      .headers(headers -> headers.frameOptions(frame -> frame.disable()))
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
