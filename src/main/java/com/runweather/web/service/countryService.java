package com.runweather.web.service;

import com.runweather.web.dto.countryDto;
import com.runweather.web.entity.country;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
@Service
public interface countryService {
    List<countryDto> findAllCountry();



}
