package com.runweather.web.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class stateDto {

    private int id;

    private String name;


}
