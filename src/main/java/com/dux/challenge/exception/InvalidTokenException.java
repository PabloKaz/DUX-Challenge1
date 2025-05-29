package com.dux.challenge.exception;

/**
 * Se lanza cuando el JWT es inválido o está expirado.
 */

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
