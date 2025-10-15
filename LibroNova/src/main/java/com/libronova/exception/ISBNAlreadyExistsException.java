package com.libronova.exception;

public class ISBNAlreadyExistsException extends RuntimeException {
    public ISBNAlreadyExistsException(String message) {
        super(message);
    }
}