package com.runweather.web.repository;

import com.runweather.web.entity.city;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface cityRepository extends JpaRepository<city, Integer> {
    List<city> findAll();



}
