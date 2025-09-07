package com.niladri.musify_backend_service.controllers;

import com.niladri.musify_backend_service.dtos.response.ApiResponse;
import com.niladri.musify_backend_service.services.album.IAlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/core/albums")
public class AlbumController {
    private final IAlbumService albumService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<?>> createAlbum(@RequestPart("request") String request,
                                                      @RequestPart("file")MultipartFile file) {
        ApiResponse<?> response = albumService.createAlbum(request,file);
        return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<?>> getAllAlbums() {
        ApiResponse<?> response = albumService.getAllAlbums();
        return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteAlbum(@PathVariable("id") String id) {
        ApiResponse<?> response = albumService.deleteAlbum(id);
        return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
    }
}
