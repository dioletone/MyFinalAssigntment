package com.runweather.web.service.imp;

import com.runweather.web.dto.countryDto;
import com.runweather.web.dto.populationDto;
import com.runweather.web.entity.country;
import com.runweather.web.entity.population;
import com.runweather.web.repository.populationRepository;
import com.runweather.web.service.populationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class populationServiceImpl implements populationService {
    private populationRepository repository;
    @Autowired
    public populationServiceImpl(populationRepository repository) {
        this.repository = repository;
    }
    @Override
    public List<populationDto> getPopulation() {

        List<population> populations = repository.findAll();
        return populations.stream().map((this::mapTopopulationDto)).collect(Collectors.toList());
    }
    private populationDto mapTopopulationDto(population population) {
        populationDto populationDto = com.runweather.web.dto.populationDto.builder()
                .id(population.getId())
                .number(population.getNumber())
                .year(population.getYear())
                .build();
        return populationDto;
    }
}
