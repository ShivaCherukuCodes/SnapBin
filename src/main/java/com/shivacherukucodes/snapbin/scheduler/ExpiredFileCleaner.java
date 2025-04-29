package com.shivacherukucodes.snapbin.scheduler;

import com.shivacherukucodes.snapbin.model.FileMetadata;
import com.shivacherukucodes.snapbin.repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpiredFileCleaner {

    private final FileMetadataRepository fileMetadataRepository;

    @Value("${file.storage.local-path}")
    private String localStoragePath;

    @Scheduled(cron = "0 0 */3 * * ?", zone = "Asia/Kolkata")
    public void cleanExpiredFiles() {
        log.info("Starting expired file cleanup...");

        List<FileMetadata> expiredFiles = fileMetadataRepository.findByExpiresAtBefore(LocalDateTime.now());

        for (FileMetadata file : expiredFiles) {
            try {
                Path filePath = Paths.get(localStoragePath, file.getStoredFilename());
                Files.deleteIfExists(filePath);
                fileMetadataRepository.delete(file);
                log.info("Deleted expired file: {}", file.getOriginalFilename());
            } catch (Exception e) {
                log.error("Failed to delete file: {}", file.getOriginalFilename(), e);
            }
        }

        log.info("Expired file cleanup finished. Total deleted: {}", expiredFiles.size());
    }
}
