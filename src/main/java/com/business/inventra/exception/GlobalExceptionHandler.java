package com.business.inventra.exception;

import com.business.inventra.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import io.jsonwebtoken.security.SignatureException;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(String.valueOf(HttpStatus.BAD_REQUEST.value()));

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            String errorCode = getErrorCode(error.getCode());
            errorResponse.addFieldError(
                error.getField(),
                errorCode,
                error.getDefaultMessage()
            );
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private String getErrorCode(String validationCode) {
        return switch (validationCode) {
            case "NotNull", "NotBlank", "NotEmpty" -> "FIELD_REQUIRED";
            case "Email" -> "INVALID_EMAIL";
            case "Size", "Length" -> "INVALID_LENGTH";
            case "Pattern" -> "INVALID_FORMAT";
            case "Min", "Max" -> "INVALID_RANGE";
            default -> "VALIDATION_FAILURE";
        };
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        errorResponse.addError("AUTHENTICATION_FAILURE", "Invalid credentials");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(String.valueOf(HttpStatus.NOT_FOUND.value()));
        errorResponse.addError("RESOURCE_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(String.valueOf(HttpStatus.CONFLICT.value()));
        errorResponse.addError("DUPLICATE_RESOURCE", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> handleJwtSignatureException(SignatureException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        errorResponse.addError("INVALID_TOKEN", "JWT signature is invalid or token is malformed");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationFailedException(AuthenticationFailedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        errorResponse.addError("INTERNAL_SERVER_ERROR", "An unexpected error occurred");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 