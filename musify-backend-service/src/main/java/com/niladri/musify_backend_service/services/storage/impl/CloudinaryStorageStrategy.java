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
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CloudinaryStorageStrategy implements FileStorageStrategy {

    private final Cloudinary cloudinary;

    @Override
    public Map<String, String> upload(MultipartFile file, Map<String, Object> options, String resourceType) {
        try {
            Map<String, Object> opts = new HashMap<>();
            if (options != null) {
                opts.putAll(options);
            }
            opts.putIfAbsent("resource_type", resourceType);
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), opts);
            Object secureUrl = result.get("secure_url");
            String duration = "";
            if (Objects.equals(resourceType, "video")) {
                duration = formatDuration((Double) result.get("duration"));
            }
            Map<String, String> uploadResult = new HashMap<>();
            uploadResult.put("url", secureUrl.toString());
            uploadResult.put("duration", duration);

            return uploadResult;
        } catch (IOException e) {
            throw new FileUploadException("Failed to upload file to Cloudinary", e);
        }
    }

    @Override
    public void delete(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    private String formatDuration(Double durationSeconds) {
        if (durationSeconds == null) {
            return "0:00";
        }

        int minutes = (int) (durationSeconds / 60);
        int seconds = (int) (durationSeconds % 60);

        return String.format("%d:%02d", minutes, seconds);

    }


}
