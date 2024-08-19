package com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "file_upload_id")
    private FileUpload fileUpload;

    @Column(name = "data_key")
    private String dataKey; // e.g., "Temperature", "Humidity", "Pressure"

    @Column(name = "count")
    private Integer count; // Number of occurrences for this data key

    @Column(name = "average_value")
    private String averageValue; // e.g., "50°C", "50%", "1051.14 hPa"

    @Column(name = "min_value")
    private String minValue; // e.g., "25°C", "40%", "1013 hPa"

    @Column(name = "max_value")
    private String maxValue; // e.g., "75°C", "60%", "1089 hPa"
}
