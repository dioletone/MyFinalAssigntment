package com.runweather.web.repository;

import com.runweather.web.entity.population;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface populationRepository extends JpaRepository<population, Long> {
    List<population> findAll();
}
