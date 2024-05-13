package com.runweather.web.repository.impl;

import com.runweather.web.entity.student;
import com.runweather.web.repository.studentRepository;
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
public class StudentCsvImporter {

    private final studentRepository studentRepository;

    public StudentCsvImporter(studentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void importStudentsFromCsv(String filePath) {
        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            List<student> students = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                student student = new student();
                student.setSid(record.get("s_id"));
                student.setName(record.get("name"));
                // Set other properties as needed

                students.add(student);
            }

            studentRepository.saveAll(students);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
