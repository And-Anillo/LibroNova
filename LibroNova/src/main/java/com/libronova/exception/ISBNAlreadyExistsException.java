package com.libronova.exception;

/**
 * Custom runtime exception thrown when attempting to register a book with an ISBN
 * that already exists in the system, violating the uniqueness constraint.
 */
public class ISBNAlreadyExistsException extends RuntimeException {
    public ISBNAlreadyExistsException(String message) {
        super(message);
    }
}