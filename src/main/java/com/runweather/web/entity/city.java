package com.runweather.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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





}
