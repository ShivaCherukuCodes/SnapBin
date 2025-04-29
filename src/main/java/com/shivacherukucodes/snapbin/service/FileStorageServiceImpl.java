package com.shivacherukucodes.snapbin.service;

import com.shivacherukucodes.snapbin.exception.FileException;
import com.shivacherukucodes.snapbin.exception.FileStorageException;
import com.shivacherukucodes.snapbin.model.FileMetadata;
import com.shivacherukucodes.snapbin.repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final FileMetadataRepository fileMetadataRepository;

    @Value("${file.storage.local-path}")
    private String localStoragePath;

    @Value("${file.access.base-url}")
    private String baseUrl;

    @Override
    public String uploadFile(MultipartFile file, int expiryMinutes) {
        try {
            // Ensure storage directory exists
            Path directoryPath = Paths.get(localStoragePath);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
                log.info("Created storage directory at {}", directoryPath);
            }

            // Generate unique filename
            String storedFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetPath = directoryPath.resolve(storedFilename);

            // Save the file locally
            Files.copy(file.getInputStream(), targetPath);

            // Set uploaded and expiry times
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiry = now.plusMinutes(expiryMinutes);

            // Save file metadata
            FileMetadata metadata = FileMetadata.builder()
                    .originalFilename(file.getOriginalFilename())
                    .storedFilename(storedFilename)
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .downloadUrl(baseUrl + "/api/files/download/" + storedFilename)
                    .uploadedAt(now)
                    .expiresAt(expiry)
                    .build();

            fileMetadataRepository.save(metadata);

            log.info("File [{}] uploaded successfully. Stored as [{}]. Expires at [{}]",
                    file.getOriginalFilename(), storedFilename, expiry);

            return metadata.getDownloadUrl();

        } catch (IOException e) {
            log.error("Failed to upload file: {}", e.getMessage());
            throw new FileStorageException("Failed to store file: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] downloadFile(String filename) {
        try {
            FileMetadata metadata = fileMetadataRepository.findByStoredFilename(filename)
                    .orElseThrow(() -> new FileException("File not found: " + filename));

            // Check for expiry
            if (LocalDateTime.now().isAfter(metadata.getExpiresAt())) {
                log.warn("Attempted to download expired file: {}", filename);
                throw new FileException("File has expired and is no longer available.");
            }

            Path filePath = Paths.get(localStoragePath, filename);

            log.info("File [{}] downloaded successfully ✅", filename);
            return Files.readAllBytes(filePath);

        } catch (NoSuchFileException e) {
            log.error("File not found on disk: {}", filename);
            throw new FileException("File not found: " + filename, e);
        } catch (IOException e) {
            log.error("Error reading the file: {}", filename);
            throw new FileException("Error reading the file: " + filename, e);
        }
    }
}
