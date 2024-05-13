package com.runweather.web.repository;

import com.runweather.web.entity.persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface personaRepository extends JpaRepository<persona, Integer> {
}
