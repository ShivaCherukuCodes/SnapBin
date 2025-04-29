package com.shivacherukucodes.snapbin.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String uploadFile(MultipartFile file, int expiryMinutes);
    byte[] downloadFile(String filename);
}
