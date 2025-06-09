package com.FlightSearch.breakabletoy2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AmadeusApiException.class)
  public ResponseEntity<Map<String, Object>> handleAmadeusApiException(AmadeusApiException ex) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", HttpStatus.BAD_GATEWAY.value());
    errorResponse.put("error", "External API Error");
    errorResponse.put("message", ex.getMessage());
    errorResponse.put("code", "AMADEUS_API_ERROR");
    errorResponse.put("path", "/api/v1/flights");

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_GATEWAY);
  }

  @ExceptionHandler(AmadeusAuthenticationException.class)
  public ResponseEntity<Map<String, Object>> handleAmadeusAuthenticationException(AmadeusAuthenticationException ex) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
    errorResponse.put("error", "Authentication Error");
    errorResponse.put("message", ex.getMessage());
    errorResponse.put("code", "AUTHENTICATION_ERROR");
    errorResponse.put("path", "/api/v1/flights");

    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AirportNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleAirportNotFoundException(AirportNotFoundException ex) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", HttpStatus.NOT_FOUND.value());
    errorResponse.put("error", "Airport Not Found");
    errorResponse.put("message", ex.getMessage());
    errorResponse.put("code", "AIRPORT_NOT_FOUND");
    errorResponse.put("path", "/api/v1/airports");

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(FlightNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleFlightNotFoundException(FlightNotFoundException ex) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", HttpStatus.NOT_FOUND.value());
    errorResponse.put("error", "Flight Not Found");
    errorResponse.put("message", ex.getMessage());
    errorResponse.put("code", "FLIGHT_NOT_FOUND");
    errorResponse.put("path", "/api/v1/flights");

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(FilterValidationException.class)
  public ResponseEntity<Map<String, Object>> handleFilterValidationException(FilterValidationException ex) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
    errorResponse.put("error", "Filter Validation Error");
    errorResponse.put("message", ex.getMessage());
    errorResponse.put("code", "FILTER_VALIDATION_ERROR");
    errorResponse.put("path", "/api/v1/flights");

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, Object> errorResponse = new HashMap<>();
    Map<String, String> validationErrors = new HashMap<>();

    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      validationErrors.put(fieldName, errorMessage);
    });

    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
    errorResponse.put("error", "Validation Failed");
    errorResponse.put("message", "Invalid request parameters");
    errorResponse.put("code", "VALIDATION_ERROR");
    errorResponse.put("validationErrors", validationErrors);

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Map<String, Object>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
    errorResponse.put("error", "Type Mismatch");
    errorResponse.put("message", String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
            ex.getValue(), ex.getName(),
            ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"));
    errorResponse.put("code", "TYPE_MISMATCH_ERROR");

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
    errorResponse.put("error", "Invalid Argument");
    errorResponse.put("message", ex.getMessage());
    errorResponse.put("code", "INVALID_ARGUMENT");

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    errorResponse.put("error", "Internal Server Error");
    errorResponse.put("message", "An unexpected error occurred");
    errorResponse.put("code", "INTERNAL_ERROR");

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}