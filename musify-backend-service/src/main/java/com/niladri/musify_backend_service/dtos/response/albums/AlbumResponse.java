package com.niladri.musify_backend_service.dtos.response.albums;

import com.niladri.musify_backend_service.entity.Albums;

import java.util.List;

public record AlbumResponse(boolean success, List<Albums> albums) {
}
