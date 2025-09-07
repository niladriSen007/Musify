package com.niladri.musify_backend_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "songs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Songs {
    @Id
    @JsonProperty("_id")
    private String id;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "Album is required")
    private String album;
    @NotNull(message = "Image is required")
    private String image;
    private String publicImageId;
    @NotNull(message = "Audio file is required")
    private String audio;
    private String publicAudioId;
    @NotNull(message = "Duration is required")
    private String duration;
}
