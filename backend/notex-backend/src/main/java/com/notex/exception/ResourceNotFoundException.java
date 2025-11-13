package com.notex.exception;

/**
 * Exception thrown when a resource is not found
 * Example: User not found, Note not found
 * 
 * RuntimeException means it's an "unchecked exception"
 * We don't need to declare it in method signatures
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Constructor with message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructor with message and cause
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}