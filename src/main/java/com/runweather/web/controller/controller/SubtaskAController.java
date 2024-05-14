package com.runweather.web.controller;

import com.runweather.web.dto.Choice;
import com.runweather.web.dto.countryDto;
import com.runweather.web.service.cityService;
import com.runweather.web.service.countryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SubtaskAController {
    private com.runweather.web.service.countryService countryService;
    private com.runweather.web.service.cityService cityService;

    @Autowired
    public SubtaskAController(com.runweather.web.service.countryService countryService) {
        this.countryService = countryService;
    }
    @Autowired
    public void setCityService(cityService cityService) {
        this.cityService = cityService;
    }



    @GetMapping("/subtaskA")
    public String listSelectedCountries(Model model) {

        return "subtaskA";
    }


}
