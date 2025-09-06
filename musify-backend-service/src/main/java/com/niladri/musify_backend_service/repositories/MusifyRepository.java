package com.niladri.musify_backend_service.repositories;

import com.niladri.musify_backend_service.entity.Albums;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MusifyRepository extends MongoRepository<Albums,String> {
}
