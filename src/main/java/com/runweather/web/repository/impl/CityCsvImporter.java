    package com.runweather.web.repository.impl;

    import com.runweather.web.entity.city;
    import com.runweather.web.entity.country;
    import com.runweather.web.entity.state;
    import com.runweather.web.repository.cityRepository;
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
    public class CityCsvImporter {

        private final com.runweather.web.repository.cityRepository cityRepository ;
        private final countryRepository countryRepo;

        public CityCsvImporter(cityRepository cityRepository, countryRepository countryRepo) {
            this.cityRepository = cityRepository;

            this.countryRepo = countryRepo;
        }

        public void importCitiesFromCsv(String filePath) {
            try (Reader reader = new FileReader(filePath);
                 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

                List<city> cities = new ArrayList<>();
                for (CSVRecord record : csvParser) {
                    city city = new city();
                    city.setName(record.get("name"));
                    city.setLat(record.get("latitude"));
                    city.setLng(record.get("longitude"));
                    // You may need to set other properties as well
                    int countryCode = Integer.parseInt(record.get("country_id"));
                    country country = findCountryById(countryCode); // Implement this method
                    city.setCountry(country);

                    cities.add(city);
                }

                // No need to create new city entities here, just save the cities retrieved from CSV
                cityRepository.saveAll(cities);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        private country findCountryById(int countryCode) {
            Optional<country> optionalCountry = countryRepo.findById(countryCode);
            return optionalCountry.orElse(null);
        }



    }







