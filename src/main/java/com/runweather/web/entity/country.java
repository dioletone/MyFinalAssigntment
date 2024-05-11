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



@Builder
@Entity
@Table(name = "country")
public class country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "country_code")
    private String code;

    @Column(name = "country_name")
    private String name;

    @OneToMany(mappedBy = "country", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private Set<city> cities = new HashSet<>();

    @OneToMany(mappedBy = "country", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private Set<state> states = new HashSet<>();

    @OneToMany(mappedBy = "country", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private Set<population> populations = new HashSet<>();

    @OneToMany(mappedBy = "country", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private Set<temperature> temperatures = new HashSet<>();

    // Add a static method to convert CSVRecord to Country object


    // Add a static method to read CSV file and create a list of Country objects

    public country() {
    }

    public country(int id, String code, String name, Set<city> cities, Set<state> states, Set<population> populations, Set<temperature> temperatures) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.cities = cities;
        this.states = states;
        this.populations = populations;
        this.temperatures = temperatures;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<city> getCities() {
        return cities;
    }

    public void setCities(Set<city> cities) {
        this.cities = cities;
    }

    public Set<state> getStates() {
        return states;
    }

    public void setStates(Set<state> states) {
        this.states = states;
    }

    public Set<population> getPopulations() {
        return populations;
    }

    public void setPopulations(Set<population> populations) {
        this.populations = populations;
    }

    public Set<temperature> getTemperatures() {
        return temperatures;
    }

    public void setTemperatures(Set<temperature> temperatures) {
        this.temperatures = temperatures;
    }
}
