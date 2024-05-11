package com.runweather.web.repository;

import com.runweather.web.entity.Global;
import org.springframework.data.jpa.repository.JpaRepository;

public interface globalRepository extends JpaRepository<Global, Integer> {
}
