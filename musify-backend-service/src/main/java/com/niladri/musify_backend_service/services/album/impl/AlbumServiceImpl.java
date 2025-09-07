package com.niladri.musify_backend_service.services.album.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niladri.musify_backend_service.dtos.payload.albums.AlbumRequest;
import com.niladri.musify_backend_service.dtos.response.ApiResponse;
import com.niladri.musify_backend_service.dtos.response.ErrorResponse;
import com.niladri.musify_backend_service.entity.Albums;
import com.niladri.musify_backend_service.exceptions.ResourceNotFoundException;
import com.niladri.musify_backend_service.repositories.AlbumRepository;
import com.niladri.musify_backend_service.services.album.IAlbumService;
import com.niladri.musify_backend_service.services.storage.impl.CloudinaryStorageStrategy;
import com.niladri.musify_backend_service.services.storage.impl.FileStorageContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlbumServiceImpl implements IAlbumService {

    private final FileStorageContext fileStorageContext;
    private final CloudinaryStorageStrategy cloudinaryStorageStrategy;
    private final AlbumRepository albumRepository;

    @Override
    public ApiResponse<?> createAlbum(String request, MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AlbumRequest album = objectMapper.readValue(request, AlbumRequest.class);
            album.setImageFile(file);

            // Use strategy pattern for file upload
            fileStorageContext.setStrategy(cloudinaryStorageStrategy);
            String imageUrl = fileStorageContext.upload(
                    album.getImageFile(),
                    null
            );
            String[] parts = imageUrl.split("/");
            String publicId = parts[parts.length - 1].split("\\.")[0];

            Albums newAlbum = Albums.builder()
                    .name(album.getName())
                    .description(album.getDescription())
                    .background(album.getBackground())
                    .coverImage(imageUrl)
                    .imagePublicId(publicId)
                    .build();

            Albums newCreatedAlbum = albumRepository.save(newAlbum);

            // Return standardized ApiResponse from service layer
            return ApiResponse.success(HttpStatus.CREATED.value(), "Album created successfully", newCreatedAlbum);
        } catch (IOException e) {
            ErrorResponse error = ErrorResponse.builder()
                    .code("IMAGE_UPLOAD_FAILED")
                    .message("Failed to upload album cover image: " + e.getMessage())
                    .timestamp(Instant.now())
                    .build();
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unable to create album", error);
        }
    }

    @Override
    public ApiResponse<?> getAllAlbums() {
        try {
            List<Albums> albums = albumRepository.findAll();
            return ApiResponse.success(HttpStatus.OK.value(), "Albums fetched successfully", albums);
        } catch (Exception e) {
            ErrorResponse error = ErrorResponse.builder()
                    .code("ALBUM_FETCH_FAILED")
                    .message("Failed to fetch albums" + e.getMessage())
                    .timestamp(Instant.now())
                    .build();
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unable to fetch albums", error);

        }
    }

    @Override
    public ApiResponse<?> deleteAlbum(String id) {
        try {
            Albums album = albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Album not found with id - " + id));
            fileStorageContext.setStrategy(cloudinaryStorageStrategy);
            fileStorageContext.delete(album.getImagePublicId());
            albumRepository.delete(album);
            return ApiResponse.success(HttpStatus.OK.value(), "Album deleted successfully", album);
        } catch (ResourceNotFoundException | IOException e) {
            ErrorResponse error = ErrorResponse.builder()
                    .code("ALBUM_DELETE_FAILED")
                    .message("Failed to delete album" + e.getMessage())
                    .timestamp(Instant.now())
                    .build();
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Unable to delete album",
                    error);
        }
    }


}
