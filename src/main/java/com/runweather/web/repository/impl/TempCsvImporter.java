package com.runweather.web.repository.impl;

import com.runweather.web.entity.*;
import com.runweather.web.repository.*;
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
public class TempCsvImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TempCsvImporter.class);

    private final temperatureRepository temperatureRepository;
    private final countryRepository countryRepository;
    private final cityRepository cityRepository;
    private final com.runweather.web.repository.stateRepository stateRepository;
    private final com.runweather.web.repository.globalRepository globalRepository;

    // Store the single global ID


    public TempCsvImporter(temperatureRepository temperatureRepository, countryRepository countryRepository, cityRepository cityRepository, stateRepository stateRepository, globalRepository globalRepository) {
        this.temperatureRepository = temperatureRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.globalRepository = globalRepository;
    }

    public void importTemperatureFromCsv(String filePath) {
        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            List<temperature> temperatures = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                temperature temp = new temperature();

                // Handling average_temp
                Double averageTemp = null;
                String averageTempString = record.get("average_temperature");
                if (averageTempString != null && !averageTempString.isEmpty()) {
                    averageTemp = parseDoubleOrNull(averageTempString);
                    if (averageTemp == null) {
                        LOGGER.error("Failed to parse average_temp for record: {}", record);
                    }
                }
                temp.setAverageTemp(averageTemp);

// Handling maximum_temp
                Double maximumTemp = null;
                String maximumTempString = record.get("maximum_temperature");
                if (maximumTempString != null && !maximumTempString.isEmpty()) {
                    maximumTemp = parseDoubleOrNull(maximumTempString);
                    if (maximumTemp == null) {
                        LOGGER.error("Failed to parse maximum_temp for record: {}", record);
                    }
                }
                temp.setMaximumTemp(maximumTemp);

// Handling minimum_temp
                Double minimumTemp = null;
                String minimumTempString = record.get("minimum_temperature");
                if (minimumTempString != null && !minimumTempString.isEmpty()) {
                    minimumTemp = parseDoubleOrNull(minimumTempString);
                    if (minimumTemp == null) {
                        LOGGER.error("Failed to parse minimum_temp for record: {}", record);
                    }
                }
                temp.setMinimumTemp(minimumTemp);


                Integer year = parseIntOrNull(record.get("year"));
                if (year == null) {
                    // Handle the case where year cannot be parsed
                    LOGGER.error("Failed to parse year for record: {}", record);
                    // Set a default value or skip the record
                    continue; // Skip this record and move to the next one
                }
                temp.setYear(year);








                // Handling country_id
                Integer countryId = null;
                String countryIdString = record.get("country_id");
                if (countryIdString != null && !countryIdString.isEmpty()) {
                    countryId = parseIntOrNull(countryIdString);
                    if (countryId == null) {
                        LOGGER.error("Failed to parse country_id for record: {}", record);
                        continue; // Skip this record and move to the next one
                    }
                }

// Find and set country
                country country = null;
                if (countryId != null) {
                    country = findCountryById(countryId);
                    if (country == null) {
                        LOGGER.error("Country not found for countryId: {}", countryId);
                        continue; // Skip this record and move to the next one
                    }
                }
                temp.setCountry(country);

// Handling city_id
                Integer cityId = null;
                String cityIdString = record.get("city_id");
                if (cityIdString != null && !cityIdString.isEmpty()) {
                    cityId = parseIntOrNull(cityIdString);
                    if (cityId == null) {
                        LOGGER.error("Failed to parse city_id for record: {}", record);
                        continue; // Skip this record and move to the next one
                    }
                }

// Find and set city
                city city = null;
                if (cityId != null) {
                    city = findCityById(cityId);
                    if (city == null) {
                        LOGGER.error("City not found for cityId: {}", cityId);
                        continue; // Skip this record and move to the next one
                    }
                }
                temp.setCity(city);

// Handling state_id
                Integer stateId = null;
                String stateIdString = record.get("state_id");
                if (stateIdString != null && !stateIdString.isEmpty()) {
                    stateId = parseIntOrNull(stateIdString);
                    if (stateId == null) {
                        LOGGER.error("Failed to parse state_id for record: {}", record);
                        continue; // Skip this record and move to the next one
                    }
                }

// Find and set state
                state state = null;
                if (stateId != null) {
                    state = findStateById(stateId);
                    if (state == null) {
                        LOGGER.error("State not found for stateId: {}", stateId);
                        continue; // Skip this record and move to the next one
                    }
                }
                temp.setState(state);

// Handling global_id
                Integer globalId = null;
                String globalIdString = record.get("global_id");
                if (globalIdString != null && !globalIdString.isEmpty()) {
                    globalId = parseIntOrNull(globalIdString);
                    if (globalId == null) {
                        LOGGER.error("Failed to parse global_id for record: {}", record);
                        continue; // Skip this record and move to the next one
                    }
                }

// Find and set global
                Global global = null;
                if (globalId != null) {
                    global = findGlobalById(globalId);
                    if (global == null) {
                        LOGGER.error("Global not found for globalId: {}", globalId);
                        continue; // Skip this record and move to the next one
                    }
                }
                temp.setGlobal(global);




                temperatures.add(temp);
            }

            temperatureRepository.saveAll(temperatures);

        } catch (IOException e) {
            LOGGER.error("Error reading CSV file: {}", e.getMessage());
        }
    }

    private country findCountryById(int countryId) {
        Optional<country> optionalCountry = countryRepository.findById(countryId);
        return optionalCountry.orElse(null);
    }
    private state findStateById(int countryId) {
        Optional<state> optionalCountry = stateRepository.findById(countryId);
        return optionalCountry.orElse(null);
    }
    private city findCityById(int countryId) {
        Optional<city> optionalCountry = cityRepository.findById(countryId);
        return optionalCountry.orElse(null);
    }
    private Global findGlobalById(int countryId) {
        Optional<Global> optionalCountry = globalRepository.findById(countryId);
        return optionalCountry.orElse(null);
    }


    private Integer parseIntOrNull(String value) {
        if (value != null && !value.isEmpty()) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                LOGGER.error("Error parsing integer value: {}", e.getMessage());
            }
        }
        return null;
    }

    private Double parseDoubleOrNull(String value) {
        if (value != null && !value.isEmpty()) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                LOGGER.error("Error parsing double value: {}", e.getMessage());
            }
        }
        return null;
    }
}
