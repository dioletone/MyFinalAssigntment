package com.runweather.web.controller;

import com.runweather.web.entity.persona;
import com.runweather.web.entity.student;
import com.runweather.web.repository.personaRepository;
import com.runweather.web.repository.studentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class missionpageController {
    private final com.runweather.web.repository.personaRepository personaRepository;
    private final com.runweather.web.repository.studentRepository studentRepository;

    public missionpageController(personaRepository personaRepository, studentRepository studentRepository) {
        this.personaRepository = personaRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/mission")
    public String missionpage(Model model) {
        List<persona> personas = personaRepository.findAll();
        List<student> students = studentRepository.findAll();
        model.addAttribute("personas", personas);
        model.addAttribute("students", students);




        return "missionpage";
    }
}
