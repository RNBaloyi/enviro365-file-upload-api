package com.enviro.assessment.grad001.RixongoteloBaloyi.eenviro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedDataDTO {
    private String dataKey;
    private int count;
    private String averageValue;
    private String minValue;
    private String maxValue;
}