package com.runweather.web.service.imp;

import com.runweather.web.dto.stateDto;
import com.runweather.web.dto.temperatureDto;
import com.runweather.web.entity.state;
import com.runweather.web.entity.temperature;
import com.runweather.web.entity.temperature;
import com.runweather.web.repository.temperatureRepository;
import com.runweather.web.service.temperatureService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class temperatureServiceImpl implements temperatureService {
    private temperatureRepository repository;
    public temperatureServiceImpl(temperatureRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<temperatureDto> getTemperature() {
       List<temperature> tempertatures = repository.findAll();
        return tempertatures.stream().map((this::mapTo)).collect(Collectors.toList());

    }
    private temperatureDto mapTo(temperature temperature) {

        return com.runweather.web.dto.temperatureDto.builder()
                .id(temperature.getId())
                .averageTemp( temperature.getAverageTemp())
                .maximumTemp(temperature.getMaximumTemp())
                .minimumTemp(temperature.getMinimumTemp())
                .build();


    }
}
