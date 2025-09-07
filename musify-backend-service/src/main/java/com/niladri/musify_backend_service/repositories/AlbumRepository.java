package com.niladri.musify_backend_service.repositories;

import com.niladri.musify_backend_service.entity.Albums;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlbumRepository extends MongoRepository<Albums,String> {
    // Check for duplicate album name
    boolean existsByNameIgnoreCase(String name);
}
