package com.runweather.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="temperature")
public class temperature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="average_temp", nullable= true)
    private Double averageTemp;

    @Column(name="maximum_temp" , nullable= true)
    private Double maximumTemp;

    @Column(name="minimum_temp", nullable= true)
    private Double minimumTemp;

    @Column(name="year")
    private int year;

    @ManyToOne
    @JoinColumn(name = "city_id",nullable = true)
    private city city;

    @ManyToOne
    @JoinColumn(name = "country_id",nullable = true)
    private country country;

    @ManyToOne
    @JoinColumn(name = "state_id",nullable = true)
    private state state;

    @ManyToOne
    @JoinColumn(name = "global_id",nullable = true)
    private Global global;

    public temperature(int id, Double averageTemp, Double maximumTemp, Double minimumTemp, int year, Integer cityId, Integer countryId, Integer stateId, Integer globalId) {
        this.id = id;
        this.averageTemp = averageTemp;
        this.maximumTemp = maximumTemp;
        this.minimumTemp = minimumTemp;
        this.year = year;
        if (cityId != null) {
            this.city = new city();
            this.city.setId(cityId);
        }
        if (countryId != null) {
            this.country = new country();
            this.country.setId(countryId);
        }
        if (stateId != null) {
            this.state = new state();
            this.state.setId(stateId);
        }
        if (globalId != null) {
            this.global = new Global();
            this.global.setId(globalId);
        }
    }

    public temperature() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Global getGlobal() {
        return global;
    }

    public void setGlobal(Global global) {
        this.global = global;
    }

    public state getState() {
        return state;
    }

    public void setStateId(Integer stateId) {
        if (stateId != null) {
            if (this.state == null) {
                this.state = new state();
            }
            this.state.setId(stateId);
        }
    }

    public void setState(state state) {
        this.state = state;
    }

    public country getCountry() {
        return country;
    }

    public void setCountryId(Integer countryId) {
        if (countryId != null) {
            if (this.country == null) {
                this.country = new country();
            }
            this.country.setId(countryId);
        }
    }

    public void setCountry(country country) {
        this.country = country;
    }

    public city getCity() {
        return city;
    }

    public void setCityId(Integer cityId) {
        if (cityId != null) {
            if (this.city == null) {
                this.city = new city();
            }
            this.city.setId(cityId);
        }
    }

    public void setCity(city city) {
        this.city = city;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Double getMinimumTemp() {
        return minimumTemp;
    }

    public void setMinimumTemp(Double minimumTemp) {
        this.minimumTemp = minimumTemp;
    }

    public Double getMaximumTemp() {
        return maximumTemp;
    }

    public void setMaximumTemp(Double maximumTemp) {
        this.maximumTemp = maximumTemp;
    }

    public Double getAverageTemp() {
        return averageTemp;
    }

    public void setAverageTemp(Double averageTemp) {
        this.averageTemp = averageTemp;
    }

    // Method to set global ID
    public void setGlobalId(Integer globalId) {
        if (globalId != null) {
            if (this.global == null) {
                this.global = new Global();
            }
            this.global.setId(globalId);
        }
    }
}
