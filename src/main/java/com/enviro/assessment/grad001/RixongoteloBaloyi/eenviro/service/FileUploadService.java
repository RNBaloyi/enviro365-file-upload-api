package com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    ResponseEntity<?> uploadFile(MultipartFile file);

    ResponseEntity<?> getFileUploadById(Long id);

    ResponseEntity<?> updateFileStatus(Long id, String status);

    ResponseEntity<?> getAllFileUploads();
}
