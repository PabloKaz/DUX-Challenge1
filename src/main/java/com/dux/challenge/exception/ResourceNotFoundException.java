package com.dux.challenge.exception;

/**
 * Excepción lanzada cuando no se encuentra un recurso (por ejemplo, un Equipo por ID).
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
