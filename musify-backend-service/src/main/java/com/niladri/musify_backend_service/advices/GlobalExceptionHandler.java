package com.niladri.musify_backend_service.advices;

import com.niladri.musify_backend_service.dtos.response.ApiResponse;
import com.niladri.musify_backend_service.dtos.response.ErrorResponse;
import com.niladri.musify_backend_service.exceptions.AlbumAlreadyExistsWithSameName;
import com.niladri.musify_backend_service.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AlbumAlreadyExistsWithSameName.class)
    public ResponseEntity<ApiResponse<?>> handleAlbumAlreadyExistsWithSameName(AlbumAlreadyExistsWithSameName ex) {
        log.error("AlbumAlreadyExistsWithSameName: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .error(ErrorResponse.builder()
                        .message(ex.getMessage())
                        .build())
                .build());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("ResourceNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .error(ErrorResponse.builder()
                        .message(ex.getMessage())
                        .build())
                .build());
    }

}
