package com.notex.exception;

import com.notex.model.dto.response.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler - Handles all exceptions in one place
 * 
 * Without this, exceptions would return ugly error messages
 * With this, we return nice JSON responses
 * 
 * @RestControllerAdvice means this handles exceptions for all controllers
 * 
 * Benefits:
 * - Consistent error format across entire API
 * - Cleaner controller code (no try-catch everywhere)
 * - Better error messages for frontend
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handle ResourceNotFoundException
     * Returns 404 Not Found
     * 
     * Example: User tries to get note with ID 999, but it doesn't exist
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request) {
        
        ApiResponse<Void> response = new ApiResponse<>(
            false,
            ex.getMessage()
        );
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Handle BadRequestException
     * Returns 400 Bad Request
     * 
     * Example: Username already taken, Invalid data
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequestException(
            BadRequestException ex,
            WebRequest request) {
        
        ApiResponse<Void> response = new ApiResponse<>(
            false,
            ex.getMessage()
        );
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handle UnauthorizedException
     * Returns 403 Forbidden
     * 
     * Example: User tries to delete someone else's note
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedException(
            UnauthorizedException ex,
            WebRequest request) {
        
        ApiResponse<Void> response = new ApiResponse<>(
            false,
            ex.getMessage()
        );
        
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
    
    /**
     * Handle UsernameNotFoundException (from Spring Security)
     * Returns 404 Not Found
     * 
     * Example: Login with username that doesn't exist
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsernameNotFoundException(
            UsernameNotFoundException ex,
            WebRequest request) {
        
        ApiResponse<Void> response = new ApiResponse<>(
            false,
            "User not found"
        );
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Handle BadCredentialsException (from Spring Security)
     * Returns 401 Unauthorized
     * 
     * Example: Login with wrong password
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(
            BadCredentialsException ex,
            WebRequest request) {
        
        ApiResponse<Void> response = new ApiResponse<>(
            false,
            "Invalid username or password"
        );
        
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    /**
     * Handle validation errors
     * Returns 400 Bad Request with field-specific errors
     * 
     * This is triggered when @Valid annotation fails
     * Example: Email field is invalid, Password too short
     * 
     * Returns a map of field names to error messages:
     * {
     *   "email": "Email must be valid",
     *   "password": "Password must be at least 6 characters"
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        
        // Loop through all validation errors
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ApiResponse<Map<String, String>> response = new ApiResponse<>(
            false,
            "Validation failed",
            errors
        );
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handle all other exceptions
     * Returns 500 Internal Server Error
     * 
     * This is the "catch-all" handler
     * Handles any exception we didn't specifically handle above
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(
            Exception ex,
            WebRequest request) {
        
        // Log the error (important for debugging!)
        ex.printStackTrace();
        
        ApiResponse<Void> response = new ApiResponse<>(
            false,
            "An error occurred: " + ex.getMessage()
        );
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}