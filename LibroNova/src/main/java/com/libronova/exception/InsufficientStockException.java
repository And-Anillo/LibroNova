package com.libronova.exception;

/**
 * Custom runtime exception thrown when a loan request is made for a book
 * that has no available copies (i.e., stock is zero or negative).
 */
public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}