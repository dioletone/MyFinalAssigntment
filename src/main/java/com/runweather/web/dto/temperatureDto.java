package com.runweather.web.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class temperatureDto {
    private int id;

    private double averageTemp;

    private double maximumTemp; ;

    private double minimumTemp;

    private int year;



}
