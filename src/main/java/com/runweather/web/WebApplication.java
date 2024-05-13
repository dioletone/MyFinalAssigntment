package com.runweather.web;

import com.runweather.web.repository.impl.*;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Value;
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
	private final PersonaCsvImporter personaCsvImporter;
	private final StudentCsvImporter studentCsvImporter;
	@Value("${file.upload.path}")
	private  String filePath;
	public WebApplication(CsvImporter csvImporter, CityCsvImporter cityCsvImporter, StateImportCsv stateCsvImporter, GlobalCsvImporter globalCsvImporter, PopCsvImporter popCsvImporter, TempCsvImporter tempCsvImporter, ImportStatusRepository importStatusRepository, PersonaCsvImporter personaCsvImporter, StudentCsvImporter studentCsvImporter) {
		this.csvImporter = csvImporter;
		this.cityCsvImporter = cityCsvImporter;
		this.stateCsvImporter = stateCsvImporter;
		this.globalCsvImporter = globalCsvImporter;
		this.popCsvImporter = popCsvImporter;
		this.tempCsvImporter = tempCsvImporter;
		this.importStatusRepository = importStatusRepository;
		this.personaCsvImporter = personaCsvImporter;
		this.studentCsvImporter = studentCsvImporter;

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

			String filepath1 = filePath+ "/Csv/country.csv";
			String filepath2 = filePath+ "/Csv/City.csv";
			String filepath3 = filePath+ "/Csv/State.csv";
			String filepath4 = filePath+ "/Csv/Global.csv";
			String filepath5 = filePath+ "/Csv/Population.csv";
			String filepath6 = filePath+ "/Csv/Temp.csv";
			String filepath7 = filePath+ "/Csv/Persona.csv";
			String filepath8 = filePath+ "/Csv/Student.csv";
			//System.out.println(filepath7);

			if (Files.exists(Paths.get(filePath))) {
				// Import data only if the file exists
				csvImporter.importCountriesFromCsv(filepath1);
				cityCsvImporter.importCitiesFromCsv(filepath2);
				stateCsvImporter.importStatesFromCsv(filepath3);
				globalCsvImporter.importGlobalsFromCsv(filepath4);
				popCsvImporter.importPopulationFromCsv(filepath5);
				tempCsvImporter.importTemperatureFromCsv(filepath6);
				personaCsvImporter.importPersonasFromCsv(filepath7);
				studentCsvImporter.importStudentsFromCsv(filepath8);



				// Update the import status
				importStatus.setDataImported(true);
				importStatusRepository.save(importStatus);
				System.out.println("Imported successfully");
			}
		} else {
			System.out.println("Data has already been imported.");
		}
	}
}
