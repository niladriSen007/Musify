package com.niladri.musify_backend_service.services.album;

import com.niladri.musify_backend_service.dtos.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IAlbumService {
    public ApiResponse<?> createAlbum(String request, MultipartFile file);

    public ApiResponse<?> getAllAlbums();

    ApiResponse<?> deleteAlbum(String id);
}
