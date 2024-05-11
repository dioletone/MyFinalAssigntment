package com.runweather.web.repository.impl;

import com.runweather.web.entity.country;
import com.runweather.web.entity.population;
import com.runweather.web.repository.countryRepository;
import com.runweather.web.repository.populationRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PopCsvImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PopCsvImporter.class);

    private final populationRepository populationRepository;
    private final countryRepository countryRepository;

    public PopCsvImporter(populationRepository populationRepository, countryRepository countryRepository) {
        this.populationRepository = populationRepository;
        this.countryRepository = countryRepository;
    }

    public void importPopulationFromCsv(String filePath) {
        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            List<population> populations = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                population pop = new population();

                String populationNumberString = record.get("population_number");
                LOGGER.debug("Population Number String: \"{}\"", populationNumberString);

                Long populationNumber = parseLongOrNull(populationNumberString);
                if (populationNumber == null) {
                    // Set a default value if populationNumber is null
                    populationNumber = 0L; // Or any other default value you prefer
                }
                pop.setNumber(populationNumber);

                pop.setYear(Integer.parseInt(record.get("Year")));

                int countryCode = Integer.parseInt(record.get("country_id"));
                country country = findCountryById(countryCode);
                pop.setCountry(country);

                populations.add(pop);
            }

            populationRepository.saveAll(populations);

        } catch (IOException e) {
            LOGGER.error("Error reading CSV file: {}", e.getMessage());
        }
    }

    private country findCountryById(int countryCode) {
        Optional<country> optionalCountry = countryRepository.findById(countryCode);
        return optionalCountry.orElse(null);
    }

    private Long parseLongOrNull(String numberString) {
        if (numberString == null || numberString.isEmpty()) {
            return null;
        } else {
            try {
                return Long.parseLong(numberString);
            } catch (NumberFormatException e) {
                LOGGER.error("Error parsing population number: {}", e.getMessage());
                return null;
            }
        }
    }
}
