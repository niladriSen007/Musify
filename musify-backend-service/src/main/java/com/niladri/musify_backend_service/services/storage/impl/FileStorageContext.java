package com.niladri.musify_backend_service.services.storage.impl;

import com.niladri.musify_backend_service.services.storage.FileStorageStrategy;
import com.niladri.musify_backend_service.exceptions.ProviderNotSupportedException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
public class FileStorageContext implements FileStorageStrategy {

    private FileStorageStrategy strategy;



    @Override
    public Map<String, String> upload(MultipartFile file, Map<String, Object> options, String resourceType) throws IOException {
        if (strategy == null) {
            throw new ProviderNotSupportedException("No storage provider registered");
        }
        Objects.requireNonNull(file, "file must not be null");
        return strategy.upload(file, options,resourceType);
    }

    @Override
    public void delete(String publicId) throws IOException {
        if (strategy == null) {
            throw new ProviderNotSupportedException("No storage provider registered");
        }
        strategy.delete(publicId);
    }


}
