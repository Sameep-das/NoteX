package com.notex.exception;

/**
 * Exception thrown when request is invalid
 * Example: Username already taken, Invalid credentials
 */
public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String message) {
        super(message);
    }
    
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}