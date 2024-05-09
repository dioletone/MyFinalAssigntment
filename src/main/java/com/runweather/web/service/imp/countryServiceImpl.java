package com.runweather.web.service.imp;

import com.runweather.web.dto.countryDto;
import com.runweather.web.entity.country;
import com.runweather.web.repository.countryRepository;
import com.runweather.web.service.countryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class countryServiceImpl implements countryService {
    private countryRepository repository;
    @Autowired
    public countryServiceImpl(countryRepository repository) {
        this.repository = repository;
    }
    @Override
    @Transactional
    public List<countryDto> findAllCountry() {

        List<country> countries = repository.findAll();
      return countries.stream().map((this::mapTocountryDto)).collect(Collectors.toList());
    }


    private countryDto mapTocountryDto(country country) {
        countryDto countryDto = com.runweather.web.dto.countryDto.builder()
                .id(country.getId())
                .code(country.getCode())
                .name(country.getName())




                .build();
        return countryDto;
    }
}
