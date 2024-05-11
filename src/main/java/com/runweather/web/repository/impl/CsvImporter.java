package com.runweather.web.repository.impl;

import com.runweather.web.dto.countryDto;
import com.runweather.web.entity.city;
import com.runweather.web.entity.country;
import com.runweather.web.repository.cityRepository;
import com.runweather.web.repository.countryRepository;
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
public class CsvImporter {

    private final countryRepository countryRepository;

    public CsvImporter(countryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }



    public void importCountriesFromCsv(String filePath) {
        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            List<country> countries = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                country country = new country();
                country.setCode(record.get("country_code"));
                country.setName(record.get("country_name"));
                countries.add(country);
            }

            countryRepository.saveAll(countries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
