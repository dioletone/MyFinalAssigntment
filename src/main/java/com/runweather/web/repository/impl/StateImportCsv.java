package com.runweather.web.repository.impl;

import com.runweather.web.entity.country;
import com.runweather.web.entity.state;
import com.runweather.web.repository.countryRepository;
import com.runweather.web.repository.stateRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class StateImportCsv {

    private final stateRepository stateRepo;
    private final countryRepository countryRepo;


    public StateImportCsv(stateRepository stateRepo, countryRepository countryRepo) {
        this.stateRepo = stateRepo;
        this.countryRepo = countryRepo;
    }

    public void importStatesFromCsv(String filePath) {
        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            List<state> states = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                state s = new state();
                s.setName(record.get("name"));
                // Assuming the country code is provided in the CSV and mapped to a country entity
                // You may need to adjust this based on your actual data structure
                int countryCode =   Integer.parseInt(record.get("country_id"))  ;
                country country = findCountryById(countryCode); // Implement this method
                s.setCountry(country);
                states.add(s);
            }

            stateRepo.saveAll(states);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private country findCountryById(int countryCode) {
        Optional<country> optionalCountry = countryRepo.findById(countryCode);
        return optionalCountry.orElse(null);
    }

}



