package com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedResultResponseDTO {
    private Long fileUploadId;
    private String fileName;
    private String fileType;
    private String uploadDate;
    private String status;
    private List<ProcessedDataDTO> processedData;
}