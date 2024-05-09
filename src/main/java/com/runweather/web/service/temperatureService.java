package com.runweather.web.service;

import com.runweather.web.dto.temperatureDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface temperatureService {
    List<temperatureDto> getTemperature();
}
