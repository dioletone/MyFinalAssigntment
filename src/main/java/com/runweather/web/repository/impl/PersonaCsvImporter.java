package com.runweather.web.repository.impl;

import com.runweather.web.entity.persona;
import com.runweather.web.repository.personaRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonaCsvImporter {

    private final personaRepository personaRepository;

    public PersonaCsvImporter(personaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public void importPersonasFromCsv(String filePath) {
        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            List<persona> personas = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                persona p = new persona();
                p.setBackground(record.get("background"));
                p.setGoals(record.get("goals"));
                p.setName(record.get("name"));
                p.setNeeds(record.get("needs"));
                p.setSkillsAndExperiences(record.get("skills_and_experiences"));
                p.setFrustration(record.get("frustration"));

                personas.add(p);
            }

            personaRepository.saveAll(personas);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
