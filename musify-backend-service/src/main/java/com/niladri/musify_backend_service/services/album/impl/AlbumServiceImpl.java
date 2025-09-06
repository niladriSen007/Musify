package com.niladri.musify_backend_service.services.album.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niladri.musify_backend_service.dtos.payload.AlbumRequest;
import com.niladri.musify_backend_service.dtos.response.ApiResponse;
import com.niladri.musify_backend_service.dtos.response.ErrorResponse;
import com.niladri.musify_backend_service.entity.Albums;
import com.niladri.musify_backend_service.exceptions.ResourceNotFoundException;
import com.niladri.musify_backend_service.repositories.MusifyRepository;
import com.niladri.musify_backend_service.services.album.IAlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class AlbumServiceImpl implements IAlbumService {

    private final Cloudinary cloudinary;
    private final MusifyRepository musifyRepository;

    public AlbumServiceImpl(Cloudinary cloudinary, MusifyRepository musifyRepository) {
        this.cloudinary = cloudinary;
        this.musifyRepository = musifyRepository;
    }

    @Override
    public ApiResponse<?> createAlbum(String request, MultipartFile file) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AlbumRequest album = objectMapper.readValue(request, AlbumRequest.class);
            album.setImageFile(file);
            Map result =
                    cloudinary.uploader().upload(
                            album.getImageFile().getBytes(),
                            ObjectUtils.asMap("resource_type", "image"));

            Albums newAlbum = Albums.builder()
                    .name(album.getName())
                    .description(album.getDescription())
                    .background(album.getBackground())
                    .coverImage((String) result.get("secure_url"))
                    .build();

            Albums newCreatedAlbum = musifyRepository.save(newAlbum);

            // Return standardized ApiResponse from service layer
            return ApiResponse.success(HttpStatus.CREATED.value(), "Album created successfully", newCreatedAlbum);
        } catch (IOException e) {
            ErrorResponse error = ErrorResponse.builder()
                    .code("IMAGE_UPLOAD_FAILED")
                    .message("Failed to upload album background: " + e.getMessage())
                    .timestamp(Instant.now())
                    .build();
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unable to create album", error);
        }
    }

    @Override
    public ApiResponse<?> getAllAlbums() {
        try {
            List<Albums> albums = musifyRepository.findAll();
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
            Albums album = musifyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Album not found with id - " + id));
            musifyRepository.delete(album);
            return ApiResponse.success(HttpStatus.OK.value(), "Album deleted successfully", album);
        } catch (ResourceNotFoundException e) {
            ErrorResponse error = ErrorResponse.builder()
                    .code("ALBUM_DELETE_FAILED")
                    .message("Failed to delete album" + e.getMessage())
                    .timestamp(Instant.now())
                    .build();

            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unable to delete album", error);
        }
    }


}
