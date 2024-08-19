package com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.repository;

import com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.model.EnvironmentalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvironmentalDataRepository extends JpaRepository<EnvironmentalData,Long> {

}
