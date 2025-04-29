package com.shivacherukucodes.snapbin.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileMetadata {

    @Id
    private String id;

    private String originalFilename;
    private String storedFilename;
    private String contentType;
    private long size;

    private String downloadUrl;
    private LocalDateTime uploadedAt;
    private LocalDateTime expiresAt;
}
