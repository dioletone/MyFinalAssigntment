package com.runweather.web.service.imp;

import com.runweather.web.dto.cityDto;
import com.runweather.web.dto.countryDto;
import com.runweather.web.entity.city;
import com.runweather.web.entity.country;
import com.runweather.web.repository.cityRepository;
import com.runweather.web.service.cityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class cityServiceImpl implements cityService {
    private cityRepository repository;
@Autowired
    public cityServiceImpl(cityRepository repository) {
    this.repository = repository;
}
@Override
    public List<cityDto> getAllCities(){
    List<city> cities = repository.findAll();
    return cities.stream().map((this::mapTocityDto)).collect(Collectors.toList());
}


    private cityDto mapTocityDto(city city) {
        cityDto cityDto = com.runweather.web.dto.cityDto.builder()
                .id(city.getId())
                .name(city.getName())
                .lng(city.getLng())
                .lat(city.getLat())




                .build();
        return cityDto;
    }
}
