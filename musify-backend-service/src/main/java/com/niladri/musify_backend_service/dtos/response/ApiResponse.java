package com.niladri.musify_backend_service.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private int status;
    private String message;
    private T data;
    private ErrorResponse error;
    private Instant timestamp;

    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(status)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> error(int status, String message, ErrorResponse error) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(status)
                .message(message)
                .error(error)
                .timestamp(Instant.now())
                .build();
    }

}
