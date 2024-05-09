package com.runweather.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="country")
public class country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="country_code")
    private String code;
    @Column(name="country_name")
    private String name;


    @OneToMany(mappedBy = "country", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private Set<city> cities = new HashSet<>();
    @OneToMany(mappedBy = "country", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private Set<state> states =new HashSet<>();
    @OneToMany(mappedBy = "country", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private Set<population> populations =new HashSet<>();
    @OneToMany(mappedBy = "country", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private Set<temperature> tempertatures =new HashSet<>();


    // Add a static method to convert CSVRecord to country object
    public static country fromCSVRecord(CSVRecord record) {
        return country.builder()
                .id(Integer.parseInt(record.get("id")))
                .code(record.get("country_code"))
                .name(record.get("country_name"))
                .build();
    }

    // Add a static method to read CSV file and create a list of country objects
    public static List<country> readWithCsvBeanReader(String filePath) throws IOException {
        List<country> countries = new ArrayList<>();

        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord record : csvParser) {
                country c = fromCSVRecord(record);
                countries.add(c);
            }
        }
return countries;}
}
