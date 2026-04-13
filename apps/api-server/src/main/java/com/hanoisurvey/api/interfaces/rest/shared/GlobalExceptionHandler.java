package com.hanoisurvey.api.interfaces.rest.shared;

import com.hanoisurvey.api.domain.shared.NotFoundException;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(
                false,
                new ErrorResponse.ErrorBody("NOT_FOUND", exception.getMessage(), List.of()),
                null
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        List<ErrorResponse.FieldErrorItem> details = exception.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = error instanceof FieldError fieldError ? fieldError.getField() : error.getObjectName();
                    return new ErrorResponse.FieldErrorItem(fieldName, error.getDefaultMessage());
                })
                .toList();

        return ResponseEntity.badRequest().body(new ErrorResponse(
                false,
                new ErrorResponse.ErrorBody("VALIDATION_ERROR", "Dữ liệu không hợp lệ", details),
                null
        ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException exception) {
        List<ErrorResponse.FieldErrorItem> details = exception.getConstraintViolations().stream()
                .map(v -> new ErrorResponse.FieldErrorItem(v.getPropertyPath().toString(), v.getMessage()))
                .toList();

        return ResponseEntity.badRequest().body(new ErrorResponse(
                false,
                new ErrorResponse.ErrorBody("VALIDATION_ERROR", "Dữ liệu không hợp lệ", details),
                null
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                false,
                new ErrorResponse.ErrorBody("INTERNAL_ERROR", exception.getMessage(), List.of()),
                null
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(
                false,
                new ErrorResponse.ErrorBody("BAD_REQUEST", exception.getMessage(), List.of()),
                null
        ));
    }
}
