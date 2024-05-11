package com.runweather.web.service.imp;

import com.runweather.web.dto.cityDto;
import com.runweather.web.dto.stateDto;
import com.runweather.web.entity.city;
import com.runweather.web.entity.state;
import com.runweather.web.repository.stateRepository;
import com.runweather.web.service.stateService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class stateServiceImpl implements stateService {
    private stateRepository stateRepository;
    public stateServiceImpl(stateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }
    @Override
    public List<stateDto> getAllStates() {
        List<state> states = stateRepository.findAll();
        return states.stream().map((this::mapTostateDto)).collect(Collectors.toList());

    }
    private stateDto mapTostateDto(state state) {

        return com.runweather.web.dto.stateDto.builder()
                .id(state.getId())
                .name(state.getName())
                .build();

    }
    }

