package com.runweather.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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





}
