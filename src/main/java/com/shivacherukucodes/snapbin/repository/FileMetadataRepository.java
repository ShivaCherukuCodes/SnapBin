package com.shivacherukucodes.snapbin.repository;

import com.shivacherukucodes.snapbin.model.FileMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileMetadataRepository extends MongoRepository<FileMetadata, String> {
    Optional<FileMetadata> findByStoredFilename(String storedFilename);

    List<FileMetadata> findByExpiresAtBefore(LocalDateTime now);
}
