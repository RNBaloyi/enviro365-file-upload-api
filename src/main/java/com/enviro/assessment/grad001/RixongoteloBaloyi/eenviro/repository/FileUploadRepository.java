package com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.repository;

import com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.model.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload,Long> {
}
