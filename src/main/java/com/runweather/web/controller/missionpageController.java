package com.runweather.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class missionpageController {
    @GetMapping("/mission")
    public String missionpage() {
        return "missionpage";
    }
}
