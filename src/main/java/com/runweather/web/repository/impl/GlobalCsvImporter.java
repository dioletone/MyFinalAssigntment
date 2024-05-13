package com.runweather.web.repository.impl;

import com.runweather.web.entity.Global;

import com.runweather.web.repository.globalRepository;
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
public class GlobalCsvImporter {

    private final globalRepository globalRepository;

    public GlobalCsvImporter(globalRepository globalRepository) {
        this.globalRepository = globalRepository;
    }

    public void importGlobalsFromCsv(String filePath) {
        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            List<Global> globals = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                Global global = new Global();
                global.setName(record.get("name"));
                // You may need to set other properties as well

                globals.add(global);
            }

            globalRepository.saveAll(globals);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
