package com.runweather.web.service;

import com.runweather.web.dto.stateDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface stateService {
    List<stateDto> getAllStates();
}
