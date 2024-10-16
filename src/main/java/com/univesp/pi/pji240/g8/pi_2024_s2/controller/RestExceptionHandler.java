package com.univesp.pi.pji240.g8.pi_2024_s2.controller;

import com.univesp.pi.pji240.g8.pi_2024_s2.controller.error.BadRequestException;
import com.univesp.pi.pji240.g8.pi_2024_s2.controller.error.ErrorDTO;
import com.univesp.pi.pji240.g8.pi_2024_s2.controller.error.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handle(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorDTO.builder().erro(ex.getMessage()).build());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDTO> handleBadRequest(BadRequestException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorDTO.builder().erro(ex.getMessage()).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Build a list of custom error messages
        List<ValidationErrorResponse> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return ValidationErrorResponse.builder()
                            .campo(fieldName)
                            .erro(errorMessage)
                            .build();
                })
                .collect(Collectors.toList());

        // Return a prettified response with a list of validation errors
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
