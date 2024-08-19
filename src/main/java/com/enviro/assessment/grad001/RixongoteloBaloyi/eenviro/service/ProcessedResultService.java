package com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.service;

import com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.model.ProcessedResult;
import org.springframework.http.ResponseEntity;

public interface ProcessedResultService {
    ResponseEntity<?> getProcessedResultByFileUploadId(Long fileUploadId);
}
