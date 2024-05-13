package com.runweather.web.repository;



import com.runweather.web.entity.temperature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface temperatureRepository extends JpaRepository<temperature, Integer> {
//List<temperature> findAll();
}
