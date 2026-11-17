package com.app.quantitymeasurement.exception;


import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

// UC17: Handles errors across all REST controllers.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Consistent JSON error response.
    public record ErrorResponse(
            LocalDateTime timestamp,
            int status,
            String error,
            String message,
            String path
    ) {
    }


    // Handle custom business exceptions.
    @ExceptionHandler(QuantityApiException.class)
    public ResponseEntity<ErrorResponse> handleQuantityError(
            QuantityApiException exception,
            HttpServletRequest request) {

        return createResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request.getRequestURI()
        );
    }


    // Handle invalid arguments.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidArgument(
            IllegalArgumentException exception,
            HttpServletRequest request) {

        return createResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request.getRequestURI()
        );
    }


    // Handle @Valid request-body failures.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        String message = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Invalid request body");

        return createResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request.getRequestURI()
        );
    }


    // Unexpected errors return HTTP 500.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception exception,
            HttpServletRequest request) {

        // Do not expose internal stack traces
        // or sensitive database details to the client.
        return createResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected server error occurred",
                request.getRequestURI()
        );
    }


    // Shared method for consistent responses.
    private ResponseEntity<ErrorResponse> createResponse(
            HttpStatus status,
            String message,
            String path) {

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }
}

