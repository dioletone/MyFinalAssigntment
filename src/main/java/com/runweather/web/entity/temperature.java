package com.runweather.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="temperature")
public class temperature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
   @Column(name="average_temp")
   private double averageTemp;
    @Column(name="maximum_temp")
    private double maximumTemp; ;
    @Column(name="minimum_temp")
    private double minimumTemp;
    @Column(name="year")
    private int year;
    @ManyToOne
    @JoinColumn(name = "country_id",nullable = false)
    private country country;
    @ManyToOne
    @JoinColumn(name = "state_id",nullable = false)
    private state state;
    @ManyToOne
    @JoinColumn(name = "city_id",nullable = false)
    private city city;
    @ManyToOne
    @JoinColumn(name = "global_id",nullable = false)
    private Global global;





}
