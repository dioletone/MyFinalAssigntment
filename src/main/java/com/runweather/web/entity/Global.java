package com.runweather.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "global")
public class Global {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private int id;
    @Column(name ="country_name")
    private String name;
    @OneToMany(mappedBy = "global", cascade = CascadeType.REMOVE)
    private Set<temperature> temperatures = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<temperature> getTemperatures() {
        return temperatures;
    }

    public void setTemperatures(Set<temperature> temperatures) {
        this.temperatures = temperatures;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Global() {
    }

    public Global(int id, Set<temperature> temperatures, String name) {
        this.id = id;
        this.temperatures = temperatures;
        this.name = name;
    }
}