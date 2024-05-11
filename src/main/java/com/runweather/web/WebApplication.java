package com.runweather.web;

import com.runweather.web.repository.impl.*;
import jakarta.persistence.Entity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class WebApplication implements CommandLineRunner {
	private final CsvImporter csvImporter;
	private final CityCsvImporter cityCsvImporter;
	private final StateImportCsv stateCsvImporter;
	private final GlobalCsvImporter globalCsvImporter;
	private final PopCsvImporter popCsvImporter;
	private final TempCsvImporter tempCsvImporter;
	private final ImportStatusRepository importStatusRepository;

	public WebApplication(CsvImporter csvImporter, CityCsvImporter cityCsvImporter, StateImportCsv stateCsvImporter, GlobalCsvImporter globalCsvImporter, PopCsvImporter popCsvImporter, TempCsvImporter tempCsvImporter, ImportStatusRepository importStatusRepository) {
		this.csvImporter = csvImporter;
		this.cityCsvImporter = cityCsvImporter;
		this.stateCsvImporter = stateCsvImporter;
		this.globalCsvImporter = globalCsvImporter;
        this.popCsvImporter = popCsvImporter;
        this.tempCsvImporter = tempCsvImporter;
        this.importStatusRepository = importStatusRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Check if data has already been imported
		ImportStatus importStatus = importStatusRepository.findById(1L).orElse(new ImportStatus());
		if (!importStatus.isDataImported()) {
			// Change the file paths to your actual CSV file locations
			String filePath = "/Users/phanmanhha/Documents/web2/Csv/country.csv";
			String filepath2 = "/Users/phanmanhha/Documents/web2/Csv/City.csv";
			String filepath3 = "/Users/phanmanhha/Documents/web2/Csv/State.csv";
			String filepath4 = "/Users/phanmanhha/Documents/web2/Csv/Global.csv";
			String filepath5 = "/Users/phanmanhha/Documents/web2/Csv/Population.csv";
			String filepath6 = "/Users/phanmanhha/Documents/web2/Csv/Temp.csv";
			if (Files.exists(Paths.get(filePath))) {
				// Import data only if the file exists
				csvImporter.importCountriesFromCsv(filePath);
				cityCsvImporter.importCitiesFromCsv(filepath2);
				stateCsvImporter.importStatesFromCsv(filepath3);
				globalCsvImporter.importGlobalsFromCsv(filepath4);
				popCsvImporter.importPopulationFromCsv(filepath5);
				tempCsvImporter.importTemperatureFromCsv(filepath6);


				// Update the import status
				importStatus.setDataImported(true);
				importStatusRepository.save(importStatus);
			}
		} else {
			System.out.println("Data has already been imported.");
		}
	}
}
