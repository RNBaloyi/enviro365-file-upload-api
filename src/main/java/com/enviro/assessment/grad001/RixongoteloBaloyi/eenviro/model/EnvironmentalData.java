package com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnvironmentalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "file_upload_id")
    private FileUpload fileUpload;

    @Column(name = "data_key")
    private String dataKey; // e.g., "Temperature", "Humidity", etc.

    @Column(name = "data_value")
    private String dataValue; // e.g., "25°C", "60%", etc.

}