package com.runweather.web.repository;

import com.runweather.web.entity.country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface countryRepository extends JpaRepository<country, Integer> {
    // No need to define methods like findAll() and saveAll() as they are already provided by JpaRepository

}
