package com.niladri.musify_backend_service.services.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface FileStorageStrategy {

    Map<String, String> upload(MultipartFile file, Map<String, Object> options,String resourceType) throws IOException;
    void delete(String publicId) throws IOException;

}

