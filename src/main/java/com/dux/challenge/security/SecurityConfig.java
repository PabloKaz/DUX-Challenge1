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

    // Tu filtro que extrae y valida el JWT (debe extender OncePerRequestFilter)
    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Deshabilitamos CSRF porque trabajamos con JWT (stateless)
            .csrf(csrf -> csrf.disable())

            // 2. No usamos sesiones HTTP: cada petición lleva su propio token
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 3. Configuramos el manejador de excepciones para rechazos 401
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, authEx) ->
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, authEx.getMessage())
                )
            )

            // 4. Definimos qué rutas son públicas y cuáles requieren autenticación
            .authorizeHttpRequests(auth -> auth
                // `/auth/login` y Swagger/H2 quedan siempre permitidos
                .requestMatchers(
                    "/auth/login",
                    "/h2-console/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**"
                ).permitAll()
                // Cualquier otra petición debe ir con un JWT válido
                .anyRequest().authenticated()
            )

            // 5. Para permitir el console H2 en navegador:
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))

            // 6. Insertamos nuestro filtro justo antes de UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }
}
