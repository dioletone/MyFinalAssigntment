package com.runweather.web.dto;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class countryDto {
    private int id;
    private String code;
    private String name;




}
