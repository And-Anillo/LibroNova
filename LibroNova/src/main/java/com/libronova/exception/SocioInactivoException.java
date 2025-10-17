package com.libronova.exception;

/**
 * Custom runtime exception thrown when an operation (e.g., borrowing a book)
 * is attempted by a library member (socio) whose status is not 'ACTIVO'.
 */
public class SocioInactivoException extends RuntimeException {
    public SocioInactivoException(String message) {
        super(message);
    }
}