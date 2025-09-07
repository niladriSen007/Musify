package com.niladri.musify_backend_service.services.storage.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.niladri.musify_backend_service.exceptions.FileUploadException;
import com.niladri.musify_backend_service.services.storage.FileStorageStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CloudinaryStorageStrategy implements FileStorageStrategy {

    private final Cloudinary cloudinary;

    @Override
    public String upload(MultipartFile file, Map<String, Object> options) {
        try {
            Map<String, Object> opts = new HashMap<>();
            if (options != null) {
                opts.putAll(options);
            }
            // default to image
            opts.putIfAbsent("resource_type", "image");
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), opts);
            Object secureUrl = result.get("secure_url");
            return secureUrl != null ? secureUrl.toString() : null;
        } catch (IOException e) {
            throw new FileUploadException("Failed to upload file to Cloudinary", e);
        }
    }

    @Override
    public void delete(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }


}
