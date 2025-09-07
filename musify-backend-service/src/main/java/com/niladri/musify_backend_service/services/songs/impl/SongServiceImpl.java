package com.niladri.musify_backend_service.services.songs.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niladri.musify_backend_service.dtos.payload.songs.SongRequest;
import com.niladri.musify_backend_service.dtos.response.ApiResponse;
import com.niladri.musify_backend_service.dtos.response.ErrorResponse;
import com.niladri.musify_backend_service.entity.Songs;
import com.niladri.musify_backend_service.exceptions.ResourceNotFoundException;
import com.niladri.musify_backend_service.repositories.SongRepository;
import com.niladri.musify_backend_service.services.songs.ISongService;
import com.niladri.musify_backend_service.services.storage.impl.CloudinaryStorageStrategy;
import com.niladri.musify_backend_service.services.storage.impl.FileStorageContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements ISongService {

    private final SongRepository songRepository;
    private final FileStorageContext fileStorageContext;
    private final CloudinaryStorageStrategy cloudinaryStorageStrategy;

    @Override
    public ApiResponse<?> createSong(String request, MultipartFile audio, MultipartFile image) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SongRequest song = objectMapper.readValue(request, SongRequest.class);
            song.setImageFile(image);
            song.setAudioFile(audio);

            fileStorageContext.setStrategy(cloudinaryStorageStrategy);

            Map<String, String> imageUploadResult = fileStorageContext.upload(song.getImageFile(), null, "image");
            Map<String, String> audioUploadResult = fileStorageContext.upload(song.getAudioFile(), null, "video");

            String imageUrl = imageUploadResult.get("url");
            String audioUrl = audioUploadResult.get("url");
            String audioDuration = audioUploadResult.get("duration");

            String[] imageParts = imageUrl.split("/");
            String imagePublicId = imageParts[imageParts.length - 1].split("\\.")[0];

            String[] audioParts = audioUrl.split("/");
            String audioPublicId = audioParts[audioParts.length - 1].split("\\.")[0];


            Songs newSong = Songs.builder().
                    name(song.getName()).
                    description(song.getDescription())
                    .image(imageUrl)
                    .audio(audioUrl)
                    .publicImageId(imagePublicId)
                    .publicAudioId(audioPublicId)
                    .album(song.getAlbum())
                    .duration(audioDuration)
                    .build();

            Songs newCreatedSong = songRepository.save(newSong);

            return ApiResponse.success(HttpStatus.CREATED.value(), "Song created successfully", newCreatedSong);

        } catch (IOException e) {
            ErrorResponse error = ErrorResponse.builder()
                    .code("SONG_CREATION_FAILED")
                    .message("Failed to create song : " + e.getMessage())
                    .timestamp(Instant.now())
                    .build();
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unable to create song", error);
        }
    }

    @Override
    public ApiResponse<?> getAllSongs() {
        try {
            List<Songs> allSongs = songRepository.findAll();
            return ApiResponse.success(HttpStatus.OK.value(), "Songs fetched successfully", allSongs);
        } catch (Exception e) {
            ErrorResponse error = ErrorResponse.builder()
                    .code("SONG_FETCH_FAILED")
                    .message("Failed to fetch songs" + e.getMessage())
                    .timestamp(Instant.now())
                    .build();
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unable to fetch songs", error);

        }
    }

    @Override
    public ApiResponse<?> deleteSong(String id) {
        try {
            Songs song = songRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + id));
            fileStorageContext.setStrategy(cloudinaryStorageStrategy);
            if (song.getPublicAudioId() != null && !song.getPublicAudioId().isEmpty()) {
                fileStorageContext.delete(song.getPublicAudioId());
            }
            fileStorageContext.delete(song.getPublicImageId());
            songRepository.delete(song);
            return ApiResponse.success(HttpStatus.OK.value(), "Song deleted successfully", song);
        } catch (ResourceNotFoundException | IOException e) {
            ErrorResponse error = ErrorResponse.builder()
                    .code("SONG_DELETE_FAILED")
                    .message("Failed to delete song" + e.getMessage())
                    .timestamp(Instant.now())
                    .build();
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Unable to delete song",
                    error);
        }
    }


}
