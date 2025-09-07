package com.niladri.musify_backend_service.controllers;


import com.niladri.musify_backend_service.dtos.response.ApiResponse;
import com.niladri.musify_backend_service.services.songs.ISongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/core/songs")
public class SongController {

    private final ISongService songService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<?>> createAlbum(@RequestPart("request") String request,
                                                      @RequestPart("audio") MultipartFile audio,
                                                      @RequestPart("image") MultipartFile image
    ) {
        ApiResponse<?> response = songService.createSong(request, audio, image);
        return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<?>> getAllSong() {
        ApiResponse<?> response = songService.getAllSongs();
        return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteSong(@PathVariable String id) {
        ApiResponse<?> response = songService.deleteSong(id);
        return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
    }

}
