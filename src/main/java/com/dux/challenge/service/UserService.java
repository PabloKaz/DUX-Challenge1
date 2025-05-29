package com.dux.challenge.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    /**
     * Inserta un usuario por defecto "test"/"12345" en H2 si no existe.
     * Se invocará desde un CommandLineRunner al iniciar la app.
     */
    void initializeDefaultUser();
}