package com.runweather.web;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import org.springframework.data.annotation.Id;

@Entity
public class ImportStatus {

    @jakarta.persistence.Id
    @GeneratedValue
    @Id
	private Long id = 1L; // Use a fixed ID for simplicity
	private boolean dataImported;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public boolean isDataImported() {
		return dataImported;
	}

	public void setDataImported(boolean dataImported) {
		this.dataImported = dataImported;
	}
// Getters and setters
}
