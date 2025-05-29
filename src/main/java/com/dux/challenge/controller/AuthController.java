package com.dux.challenge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dux.challenge.dto.request.AuthRequest;
import com.dux.challenge.dto.response.AuthResponse;
import com.dux.challenge.security.JwtUtil;
import com.dux.challenge.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserService userService;     // <-- inyectamos tu UserService

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        // 1) Autentica
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
            )
        );
        // 2) Carga el UserDetails usando tu servicio
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        // 3) Genera el JWT
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
