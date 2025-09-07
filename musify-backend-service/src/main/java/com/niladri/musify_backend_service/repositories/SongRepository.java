package com.niladri.musify_backend_service.repositories;

import com.niladri.musify_backend_service.entity.Songs;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SongRepository extends MongoRepository<Songs,String> {
}
