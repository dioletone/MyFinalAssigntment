package com.runweather.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;



@Entity
@Table(name="state")
public class state {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "country_id",nullable = false)
    private country country;
    @OneToMany(mappedBy = "state", cascade = CascadeType.REMOVE)
    private Set<temperature> tempertatures = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<temperature> getTempertatures() {
        return tempertatures;
    }

    public void setTempertatures(Set<temperature> tempertatures) {
        this.tempertatures = tempertatures;
    }

    public com.runweather.web.entity.country getCountry() {
        return country;
    }

    public void setCountry(com.runweather.web.entity.country country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public state(int id, Set<temperature> tempertatures, com.runweather.web.entity.country country, String name) {
        this.id = id;
        this.tempertatures = tempertatures;
        this.country = country;
        this.name = name;
    }

    public state() {
    }
}
