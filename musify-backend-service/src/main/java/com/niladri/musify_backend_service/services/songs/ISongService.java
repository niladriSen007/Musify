package com.niladri.musify_backend_service.services.songs;

import com.niladri.musify_backend_service.dtos.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ISongService {
    ApiResponse<?> createSong(String request, MultipartFile audio, MultipartFile image);

    ApiResponse<?> getAllSongs();

    ApiResponse<?> deleteSong(String id);
}
