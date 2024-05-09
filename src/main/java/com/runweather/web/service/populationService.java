package com.runweather.web.service;


import com.runweather.web.dto.populationDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface populationService {
    List<populationDto> getPopulation();
}
