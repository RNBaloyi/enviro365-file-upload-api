package com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.controller;

import com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.service.ProcessedResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/processed-results")
public class ProcessedResultController {

    @Autowired
    private ProcessedResultService processedResultService;

    @GetMapping("/file/{fileUploadId}")
    @Operation(
            summary = "Get processed results by file upload ID",
            description = "Retrieve processed results for a specific file upload ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Processed results retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "File upload ID not found", content = @Content(schema = @Schema(type = "string")))
            }
    )
    public ResponseEntity<?> getProcessedResultsByFileUploadId(
            @Parameter(description = "ID of the file upload", required = true)
            @PathVariable Long fileUploadId) {
        return processedResultService.getProcessedResultByFileUploadId(fileUploadId);
    }
}
