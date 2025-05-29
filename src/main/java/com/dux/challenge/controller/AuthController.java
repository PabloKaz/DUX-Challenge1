package com.dux.challenge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints para obtener y validar JWT")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Login", description = "Autentica un usuario y devuelve un token JWT")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Autenticado correctamente",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = AuthResponse.class))),
      @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content)
    })
    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        // 1) Autenticamos (lanza BadCredentialsException si falla)
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
            )
        );
        // 2) Recargamos UserDetails
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        // 3) Generamos el JWT
        String token = jwtUtil.generateToken(userDetails);
        // 4) Devolvemos el token con 200 OK
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
