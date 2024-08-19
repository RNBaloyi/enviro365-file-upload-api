package com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.controller;

import com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload")
    @Operation(
            summary = "Upload a file",
            description = "Uploads a file containing environmental data for processing.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "File uploaded successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid file format", content = @Content(schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema = @Schema(type = "string")))
            }
    )
    public ResponseEntity<?> uploadFile(
            @Parameter(description = "File to upload", required = true)
            @RequestParam("file") MultipartFile file) {
        return fileUploadService.uploadFile(file);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get file upload by ID",
            description = "Retrieve details of a file upload by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "File upload details retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "File upload not found", content = @Content(schema = @Schema(type = "string")))
            }
    )
    public ResponseEntity<?> getFileUploadById(
            @Parameter(description = "ID of the file upload", required = true)
            @PathVariable Long id) {
        return fileUploadService.getFileUploadById(id);
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Update file status",
            description = "Update the status of a file upload.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "File status updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid status or file ID", content = @Content(schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "404", description = "File upload not found", content = @Content(schema = @Schema(type = "string")))
            }
    )
    public ResponseEntity<?> updateFileStatus(
            @Parameter(description = "ID of the file upload", required = true)
            @PathVariable Long id,
            @Parameter(description = "New status of the file upload", required = true)
            @RequestParam String status) {
        return fileUploadService.updateFileStatus(id, status);
    }

    @GetMapping
    @Operation(
            summary = "Get all file uploads",
            description = "Retrieve a list of all file uploads.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of file uploads retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema = @Schema(type = "string")))
            }
    )
    public ResponseEntity<?> getAllFileUploads() {
        return fileUploadService.getAllFileUploads();
    }
}
