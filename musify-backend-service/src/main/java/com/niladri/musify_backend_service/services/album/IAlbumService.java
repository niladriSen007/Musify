package com.niladri.musify_backend_service.services.album;

import com.niladri.musify_backend_service.dtos.payload.AlbumRequest;
import com.niladri.musify_backend_service.dtos.response.ApiResponse;
import com.niladri.musify_backend_service.entity.Albums;
import org.springframework.web.multipart.MultipartFile;

public interface IAlbumService {
    public ApiResponse<?> createAlbum(String request, MultipartFile file);

    public ApiResponse<?> getAllAlbums();

    ApiResponse<?> deleteAlbum(String id);
}
