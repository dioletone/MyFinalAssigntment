package com.runweather.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;


@Entity
@Table(name="population")
public class population {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="number", nullable=true)
    private long number;
    @Column(name="year")
    private int year;
    @ManyToOne
    @JoinColumn(name = "country_id",nullable = false)
    private country country;

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public com.runweather.web.entity.country getCountry() {
        return country;
    }

    public void setCountry(com.runweather.web.entity.country country) {
        this.country = country;
    }

    public population() {
    }

    public population(int id, com.runweather.web.entity.country country, int year, long number) {
        this.id = id;
        this.country = country;
        this.year = year;
        this.number = number;
    }
}
