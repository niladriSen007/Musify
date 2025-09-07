package com.niladri.musify_backend_service.dtos.response.songs;

import com.niladri.musify_backend_service.entity.Songs;

import java.util.List;

public record SongResponse(boolean success, List<Songs> songs) {
}
