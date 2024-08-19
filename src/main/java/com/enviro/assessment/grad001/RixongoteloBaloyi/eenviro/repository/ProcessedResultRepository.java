package com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.repository;

import com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.model.ProcessedResult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProcessedResultRepository extends JpaRepository<ProcessedResult,Long> {
    List<ProcessedResult> findByFileUploadId(Long fileUploadId);
}
