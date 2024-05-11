package com.runweather.web.service;

import com.runweather.web.dto.cityDto;
import com.runweather.web.entity.city;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface cityService {
    List<cityDto> getAllCities();




}
