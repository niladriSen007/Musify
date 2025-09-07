package com.niladri.musify_backend_service.advices;

import com.niladri.musify_backend_service.dtos.response.ApiResponse;
import com.niladri.musify_backend_service.dtos.response.ErrorResponse;
import com.niladri.musify_backend_service.exceptions.AlbumAlreadyExistsWithSameName;
import com.niladri.musify_backend_service.exceptions.FileUploadException;
import com.niladri.musify_backend_service.exceptions.ProviderNotSupportedException;
import com.niladri.musify_backend_service.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private ResponseEntity<ApiResponse<?>> build(HttpStatus status, String message, String code, String path, List<String> subErrors) {
        ErrorResponse error = ErrorResponse.builder()
                .code(code)
                .message(message)
                .path(path)
                .timestamp(Instant.now())
                .subErrors(subErrors)
                .build();
        return ResponseEntity.status(status)
                .body(ApiResponse.error(status.value(), message, error));
    }

    @ExceptionHandler(AlbumAlreadyExistsWithSameName.class)
    public ResponseEntity<ApiResponse<?>> handleAlbumAlreadyExistsWithSameName(AlbumAlreadyExistsWithSameName ex, HttpServletRequest req) {
        log.error("AlbumAlreadyExistsWithSameName: {}", ex.getMessage());
        return build(HttpStatus.CONFLICT, ex.getMessage(), "ALBUM_ALREADY_EXISTS", req.getRequestURI(), null);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest req) {
        log.error("ResourceNotFoundException: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), "RESOURCE_NOT_FOUND", req.getRequestURI(), null);
    }

    @ExceptionHandler(ProviderNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleProviderNotSupported(ProviderNotSupportedException ex, HttpServletRequest req) {
        log.error("ProviderNotSupportedException: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), "PROVIDER_NOT_SUPPORTED", req.getRequestURI(), null);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ApiResponse<?>> handleFileUpload(FileUploadException ex, HttpServletRequest req) {
        log.error("FileUploadException: {}", ex.getMessage());
        return build(HttpStatus.BAD_GATEWAY, ex.getMessage(), "FILE_UPLOAD_FAILED", req.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());
        String message = "Validation failed";
        log.error("Validation error: {} -> {}", message, errors);
        return build(HttpStatus.BAD_REQUEST, message, "VALIDATION_ERROR", req.getRequestURI(), errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        log.error("IllegalArgumentException: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), "BAD_REQUEST", req.getRequestURI(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneric(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception: ", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", "INTERNAL_ERROR", req.getRequestURI(), null);
    }
}
