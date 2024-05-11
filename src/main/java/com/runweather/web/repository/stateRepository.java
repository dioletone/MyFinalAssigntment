package com.runweather.web.repository;

import com.runweather.web.entity.state;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface stateRepository extends JpaRepository<state, Integer> {
//List<state> findAll();
}
