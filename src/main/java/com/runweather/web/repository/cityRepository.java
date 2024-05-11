package com.runweather.web.repository;

import com.runweather.web.entity.city;
import org.springframework.data.jpa.repository.JpaRepository;

public interface cityRepository extends JpaRepository<city, Integer> {
    // You don't need to define the saveAll method here
}
