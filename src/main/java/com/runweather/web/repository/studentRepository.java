package com.runweather.web.repository;

import com.runweather.web.entity.student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface studentRepository extends JpaRepository<student, Integer> {
}
