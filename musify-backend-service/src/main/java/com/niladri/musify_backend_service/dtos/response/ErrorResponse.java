package com.niladri.musify_backend_service.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String code;                // semantic error code e.g., "RESOURCE_NOT_FOUND"
    private String message;             // human readable message
    private String path;                // request path
    private Instant timestamp;
    private List<String> subErrors;
}
