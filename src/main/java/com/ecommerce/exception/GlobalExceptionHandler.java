package com.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

   /**
    * Handle resource not found exceptions
    */
   @ExceptionHandler(ResourceNotFoundException.class)
   public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
      ErrorResponse error = ErrorResponse.builder()
              .timestamp(LocalDateTime.now())
              .status(HttpStatus.NOT_FOUND.value())
              .error("Not Found")
              .message(ex.getMessage())
              .build();
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
   }

   /**
    * Handle insufficient stock exceptions
    */
   @ExceptionHandler(InsufficientStockException.class)
   public ResponseEntity<ErrorResponse> handleInsufficientStockException(InsufficientStockException ex) {
      ErrorResponse error = ErrorResponse.builder()
              .timestamp(LocalDateTime.now())
              .status(HttpStatus.BAD_REQUEST.value())
              .error("Insufficient Stock")
              .message(ex.getMessage())
              .build();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
   }

   /**
    * Handle invalid operation exceptions
    */
   @ExceptionHandler(InvalidOperationException.class)
   public ResponseEntity<ErrorResponse> handleInvalidOperationException(InvalidOperationException ex) {
      ErrorResponse error = ErrorResponse.builder()
              .timestamp(LocalDateTime.now())
              .status(HttpStatus.BAD_REQUEST.value())
              .error("Invalid Operation")
              .message(ex.getMessage())
              .build();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
   }

   /**
    * Handle validation exceptions
    */
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
      Map<String, String> errors = new HashMap<>();
      ex.getBindingResult().getAllErrors().forEach((error) -> {
         String fieldName = ((FieldError) error).getField();
         String errorMessage = error.getDefaultMessage();
         errors.put(fieldName, errorMessage);
      });

      ValidationErrorResponse errorResponse = ValidationErrorResponse.builder()
              .timestamp(LocalDateTime.now())
              .status(HttpStatus.BAD_REQUEST.value())
              .error("Validation Failed")
              .message("Invalid request parameters")
              .validationErrors(errors)
              .build();

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
   }

   /**
    * Handle all other exceptions
    */
   @ExceptionHandler(Exception.class)
   public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
      ErrorResponse error = ErrorResponse.builder()
              .timestamp(LocalDateTime.now())
              .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
              .error("Internal Server Error")
              .message("An unexpected error occurred: " + ex.getMessage())
              .build();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
   }

   /**
    * Error response structure
    */
   @lombok.Data
   @lombok.Builder
   public static class ErrorResponse {
      private LocalDateTime timestamp;
      private int status;
      private String error;
      private String message;
   }

   /**
    * Validation error response structure
    */
   @lombok.Data
   @lombok.Builder
   public static class ValidationErrorResponse {
      private LocalDateTime timestamp;
      private int status;
      private String error;
      private String message;
      private Map<String, String> validationErrors;
   }
}