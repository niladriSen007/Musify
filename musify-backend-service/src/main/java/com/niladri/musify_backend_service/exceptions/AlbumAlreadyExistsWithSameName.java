package com.niladri.musify_backend_service.exceptions;

public class AlbumAlreadyExistsWithSameName extends RuntimeException {
    public AlbumAlreadyExistsWithSameName(String message) {
        super(message);
    }
}
