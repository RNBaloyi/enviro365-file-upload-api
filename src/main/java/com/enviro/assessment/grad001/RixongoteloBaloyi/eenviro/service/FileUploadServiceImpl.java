package com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.service;

import com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.exception.InvalidFileFormatException;
import com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.model.EnvironmentalData;
import com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.model.FileUpload;
import com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.model.ProcessedResult;
import com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.repository.EnvironmentalDataRepository;
import com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.repository.FileUploadRepository;
import com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.repository.ProcessedResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Autowired
    private EnvironmentalDataRepository environmentalDataRepository;

    @Autowired
    private ProcessedResultRepository processedResultRepository;

    @Autowired
    private ProcessedResultService processedResultService;

    @Override
    public ResponseEntity<?> uploadFile(MultipartFile file) {
        try {
            validateFile(file);

            FileUpload fileUpload = new FileUpload();
            fileUpload.setFileName(file.getOriginalFilename());
            fileUpload.setFileType(file.getContentType());
            fileUpload.setUploadDate(LocalDateTime.now());
            fileUpload.setStatus("Uploaded");

            fileUpload = fileUploadRepository.save(fileUpload);

            processFile(file, fileUpload);

            ResponseEntity<?> processedResult = processedResultService.getProcessedResultByFileUploadId(fileUpload.getId());

            if (processedResult != null) {
                return processedResult;
            } else {
                return ResponseEntity.status(404).body("Processed result not found for file upload ID: " + fileUpload.getId());
            }
        } catch (InvalidFileFormatException e) {
            logger.error("Error during file processing: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid file format: " + e.getMessage());
        } catch (IOException e) {
            logger.error("File upload failed: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateFileStatus(Long id, String status) {
        return fileUploadRepository.findById(id)
                .map(fileUpload -> {
                    fileUpload.setStatus(status);
                    fileUploadRepository.save(fileUpload);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<?> getAllFileUploads() {
        List<FileUpload> fileUploads = fileUploadRepository.findAll();
        return ResponseEntity.ok(fileUploads);
    }

    @Override
    public ResponseEntity<?> getFileUploadById(Long id) {
        return fileUploadRepository.findById(id)
                .map(fileUpload -> ResponseEntity.ok(fileUpload))
                .orElse(ResponseEntity.notFound().build());
    }

    private void processFile(MultipartFile file, FileUpload fileUpload) throws IOException {
        Map<String, List<String>> dataMap = new HashMap<>();
        List<String> invalidLinesSummary = new ArrayList<>();

        logger.info("Starting to process file: {} (ID: {})", fileUpload.getFileName(), fileUpload.getId());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Trim leading and trailing whitespace
                if (line.isEmpty()) {
                    lineNumber++;
                    continue; // Skip empty lines
                }

                String[] data = line.split(",");
                if (data.length != 2) {
                    invalidLinesSummary.add(String.format("Line %d: %s", lineNumber, line));
                    lineNumber++;
                    continue; // Skip lines that do not conform to the expected format
                }

                String dataKey = data[0].trim();
                String dataValue = data[1].trim();

                if (isValidDataKey(dataKey)) {
                    try {
                        EnvironmentalData environmentalData = new EnvironmentalData();
                        environmentalData.setFileUpload(fileUpload);
                        environmentalData.setDataKey(dataKey);
                        environmentalData.setDataValue(dataValue);
                        environmentalDataRepository.save(environmentalData);

                        dataMap.computeIfAbsent(dataKey, k -> new ArrayList<>()).add(dataValue);
                        logger.debug("Processed line {}: DataKey={}, DataValue={}", lineNumber, dataKey, dataValue);
                    } catch (NumberFormatException e) {
                        logger.warn("Failed to convert data value to number for file {} (ID: {}): DataValue={}", fileUpload.getFileName(), fileUpload.getId(), dataValue, e);
                    }
                } else {
                    logger.warn("Skipping unknown data key in file {} (ID: {}): DataKey={}", fileUpload.getFileName(), fileUpload.getId(), dataKey);
                }

                lineNumber++;
            }
        }

        if (!invalidLinesSummary.isEmpty()) {
            logger.warn("Invalid lines detected in file {} (ID: {}): {}", fileUpload.getFileName(), fileUpload.getId(), invalidLinesSummary);
            throw new InvalidFileFormatException("Invalid lines detected in the file: " + invalidLinesSummary);
        }

        logger.info("File processing completed successfully: {} (ID: {})", fileUpload.getFileName(), fileUpload.getId());

        for (Map.Entry<String, List<String>> entry : dataMap.entrySet()) {
            String dataKey = entry.getKey();
            List<String> values = entry.getValue();

            ProcessedResult processedResult = new ProcessedResult();
            processedResult.setFileUpload(fileUpload);
            processedResult.setDataKey(dataKey);
            processedResult.setCount(values.size());

            if (dataKey.equals("Temperature") || dataKey.equals("Pressure") || dataKey.equals("Precipitation") || dataKey.equals("WindSpeed")) {
                List<Double> numericValues = values.stream()
                        .map(value -> {
                            try {
                                return Double.parseDouble(value.replaceAll("[^\\d.]", ""));
                            } catch (NumberFormatException e) {
                                logger.debug("Failed to parse numeric value from: {}", value, e);
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                if (!numericValues.isEmpty()) {
                    double average = numericValues.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                    String averageValue = String.format("%.2f", average) + extractUnit(values.get(0));

                    double min = numericValues.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
                    String minValue = min + extractUnit(values.get(0));

                    double max = numericValues.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
                    String maxValue = max + extractUnit(values.get(0));

                    processedResult.setAverageValue(averageValue);
                    processedResult.setMinValue(minValue);
                    processedResult.setMaxValue(maxValue);

                    logger.info("Processed result for key '{}': Avg={}, Min={}, Max={}", dataKey, averageValue, minValue, maxValue);
                } else {
                    processedResult.setAverageValue("N/A");
                    processedResult.setMinValue("N/A");
                    processedResult.setMaxValue("N/A");

                    logger.info("No numeric values for key '{}', defaulting to 'N/A'", dataKey);
                }
            } else if (dataKey.equals("WindDirection")) {
                String frequentValue = values.stream()
                        .collect(Collectors.groupingBy(v -> v, Collectors.counting()))
                        .entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse("N/A");

                processedResult.setAverageValue(frequentValue);
                processedResult.setMinValue("N/A");
                processedResult.setMaxValue("N/A");

                logger.info("Processed result for WindDirection: Most frequent value='{}'", frequentValue);
            } else {
                String frequentValue = values.stream()
                        .collect(Collectors.groupingBy(v -> v, Collectors.counting()))
                        .entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse("N/A");

                processedResult.setAverageValue(frequentValue);
                processedResult.setMinValue(frequentValue);
                processedResult.setMaxValue(frequentValue);

                logger.info("Processed result for non-numeric key '{}': Most frequent value='{}'", dataKey, frequentValue);
            }

            processedResultRepository.save(processedResult);
        }

        logger.info("All processed results saved successfully for file {} (ID: {})", fileUpload.getFileName(), fileUpload.getId());
    }


    private boolean isValidDataKey(String dataKey) {
        return Arrays.asList("Temperature", "Humidity", "Pressure", "WindSpeed", "WindDirection", "Precipitation").contains(dataKey);
    }

    private String extractUnit(String value) {
        return value.replaceAll("[\\d.]", "");
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileFormatException("The file is empty.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("text/plain")) {
            throw new InvalidFileFormatException("Only text files are supported.");
        }
    }
}
