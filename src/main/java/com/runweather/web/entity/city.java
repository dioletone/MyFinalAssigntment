package com.runweather.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;




@Entity
@Table(name="city")
public class city {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="lat")
    private String lat;
    @Column(name="long")
    private String lng;
    @Column(name="name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "country_id",nullable = false)
    private country country;
    @OneToMany(mappedBy = "city", cascade = CascadeType.REMOVE)
    private Set<temperature> tempertatures = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public com.runweather.web.entity.country getCountry() {
        return country;
    }

    public void setCountry(com.runweather.web.entity.country country) {
        this.country = country;
    }

    public Set<temperature> getTempertatures() {
        return tempertatures;
    }

    public void setTempertatures(Set<temperature> tempertatures) {
        this.tempertatures = tempertatures;
    }

    public city() {
    }
}
