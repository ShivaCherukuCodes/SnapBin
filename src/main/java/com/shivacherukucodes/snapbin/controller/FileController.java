package com.shivacherukucodes.snapbin.controller;

import com.shivacherukucodes.snapbin.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "expiry", defaultValue = "10") int expiryMinutes
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Uploaded file is empty ❌");
        }
        if (expiryMinutes <= 0) {
            return ResponseEntity.badRequest().body("Expiry time must be greater than 0 minutes ❌");
        }

        String fileUrl = fileStorageService.uploadFile(file, expiryMinutes);
        log.info("File [{}] uploaded with expiry of [{}] minutes", file.getOriginalFilename(), expiryMinutes);

        return ResponseEntity.ok("✅ File uploaded successfully!\nAccess it here: " + fileUrl + "\nExpires in: " + expiryMinutes + " minutes.");
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {
        byte[] data = fileStorageService.downloadFile(filename);

        // Default to binary if unknown
        String contentType = "application/octet-stream";
        if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
            contentType = MediaType.IMAGE_JPEG_VALUE;
        } else if (filename.toLowerCase().endsWith(".png")) {
            contentType = MediaType.IMAGE_PNG_VALUE;
        } else if (filename.toLowerCase().endsWith(".pdf")) {
            contentType = MediaType.APPLICATION_PDF_VALUE;
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(data);
    }
}
