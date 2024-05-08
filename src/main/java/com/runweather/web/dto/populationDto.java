package com.runweather.web.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class populationDto {
    private int id;
    private long number;
    private int year;

}
