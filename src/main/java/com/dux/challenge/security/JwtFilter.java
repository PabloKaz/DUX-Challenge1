package com.dux.challenge.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dux.challenge.exception.InvalidTokenException;
import com.dux.challenge.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Filtro que:
 * 1) Lee el header Authorization Bearer <token>
 * 2) Extrae username del token
 * 3) Valida el token
 * 4) Carga el UserDetails y lo pone en SecurityContext
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
    private JwtUtil jwtUtil;
	
	@Autowired
    private UserService userService;  
	
	
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // 1) Extrae el token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                // 2) Saca el usuario del token
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
            	/* Se arroja la exception en caso de Token inválido */
            	throw new InvalidTokenException("Token inválido o expirado");
            }
        }

        // 3) Si tenemos usuario y no hay autenticación en contexto, validamos
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(username);
            // 4) Comprueba que el token coincida con el usuario y no esté expirado
            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continua con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
