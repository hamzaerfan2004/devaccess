package com.hamzaerfan.devaccess.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException exception) {

        Map<String, Object> response = new HashMap<>();

        String message = exception.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        response.put("status", 400);
        response.put("error", "Validation failed");
        response.put("message", message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ScanFailedException.class)
    public ResponseEntity<Map<String, Object>> handleScanFailed(
            ScanFailedException exception) {

        Map<String, Object> response = new HashMap<>();

        response.put("status", 422);
        response.put("error", "Scan failed");
        response.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(response);
    }

    @ExceptionHandler(AnalysisServiceException.class)
    public ResponseEntity<Map<String, Object>> handleAnalysisServiceError(
            AnalysisServiceException exception) {

        Map<String, Object> response = new HashMap<>();

        response.put("status", 503);
        response.put("error", "Analysis service unavailable");
        response.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException exception) {

        Map<String, Object> response = new HashMap<>();

        response.put("status", 404);
        response.put("error", "Not Found");
        response.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }
}