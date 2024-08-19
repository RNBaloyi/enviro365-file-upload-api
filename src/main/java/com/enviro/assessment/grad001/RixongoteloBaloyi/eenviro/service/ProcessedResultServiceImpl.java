package com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.service;

import com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.dto.ProcessedDataDTO;
import com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.dto.ProcessedResultResponseDTO;
import com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.model.ProcessedResult;
import com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.repository.ProcessedResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcessedResultServiceImpl implements ProcessedResultService {

    @Autowired
    private ProcessedResultRepository processedResultRepository;

    @Override
    public ResponseEntity<?> getProcessedResultByFileUploadId(Long fileUploadId) {
        List<ProcessedResult> results = processedResultRepository.findByFileUploadId(fileUploadId);

        if (results.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ProcessedResultResponseDTO responseDTO = new ProcessedResultResponseDTO();
        responseDTO.setFileUploadId(fileUploadId);
        responseDTO.setFileName(results.get(0).getFileUpload().getFileName());
        responseDTO.setFileType(results.get(0).getFileUpload().getFileType());
        responseDTO.setUploadDate(results.get(0).getFileUpload().getUploadDate().toString());
        responseDTO.setStatus(results.get(0).getFileUpload().getStatus());

        List<ProcessedDataDTO> processedDataDTOList = results.stream().map(result -> {
            ProcessedDataDTO dto = new ProcessedDataDTO();
            dto.setDataKey(result.getDataKey());
            dto.setCount(result.getCount());
            dto.setAverageValue(result.getAverageValue());
            dto.setMinValue(result.getMinValue());
            dto.setMaxValue(result.getMaxValue());
            return dto;
        }).collect(Collectors.toList());

        responseDTO.setProcessedData(processedDataDTOList);
        return ResponseEntity.ok(responseDTO);
    }
}
