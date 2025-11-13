package com.notex.exception;

/**
 * Exception thrown when user is not authorized
 * Example: Trying to delete someone else's note
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}