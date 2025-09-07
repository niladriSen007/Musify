package com.niladri.musify_backend_service.exceptions;

public class ProviderNotSupportedException extends RuntimeException {
    public ProviderNotSupportedException(String message) {
        super(message);
    }
}

