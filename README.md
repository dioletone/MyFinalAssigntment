# Climate Analysis Platform

A comprehensive Spring Boot web application for analyzing climate change and population dynamics across global, country, state, and city levels over a 260-year timeframe. This platform enables researchers, policymakers, and environmental enthusiasts to explore temperature and population trends through interactive data visualization and analysis tools.

## 🌍 Overview

The Climate Analysis Platform provides data-driven insights into climate change patterns by analyzing:
- **Temperature Data**: Average, minimum, and maximum temperatures across different geographic levels
- **Population Data**: Population changes over time by country
- **Correlation Analysis**: Relationships between temperature changes and population dynamics
- **Trend Identification**: Multi-period comparisons and similarity analysis

## ✨ Features

### Level 1 (Green) - Foundation
- **Landing Page**: Welcome page with navigation and overview
- **Mission Statement**: Project objectives, personas, and team information

### Level 2 (Orange) - High-Level Analysis
- **Subtask A - Global/Country View**:
  - Compare temperature and population changes between start and end years
  - View data globally or by country
  - Sortable tables with pagination
  - Display temperature differences (average, min, max) and population changes
  
- **Subtask B - City/State Analysis**:
  - Analyze temperature changes for cities or states within a selected country
  - Rank locations by temperature change magnitude
  - Calculate correlation metrics between temperature and population

### Level 3 (Red) - Deep Analysis
- **Subtask A - Multi-Period Analysis**:
  - Single Region: Analyze multiple periods for one region
  - Multiple Regions: Compare temperature trends across different regions
  - Filter by temperature range and population range
  - Dynamic table headers based on selected periods
  
- **Subtask B - Similar Periods Finder**:
  - Find periods with similar temperature/population characteristics
  - View by temperature, population, or both
  - Display gap calculations showing changes over time

## 🛠️ Technology Stack

- **Backend Framework**: Spring Boot (Java)
- **Database**: PostgreSQL
- **ORM**: JPA/Hibernate
- **Frontend**: Thymeleaf Templates + Bootstrap 5
- **Data Processing**: Apache Commons CSV
- **Build Tool**: Maven/Gradle (Spring Boot default)

## 📋 Prerequisites

Before running this application, ensure you have:

- **Java**: JDK 17 or higher
- **PostgreSQL**: Version 12 or higher
- **Maven**: Version 3.6+ (if using Maven)
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code (recommended)

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd MyFinalAssigntment
```

### 2. Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE datalearning;
```

2. Update database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/datalearning
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Prepare CSV Data Files

Create a directory structure for your CSV files. Update the `file.upload.path` in `application.properties`:

```properties
file.upload.path=/path/to/your/csv/directory
```

Required CSV files (place them in `{file.upload.path}/Csv/`):
- `country.csv`
- `City.csv`
- `State.csv`
- `Global.csv`
- `Population.csv`
- `Temp.csv`
- `Persona.csv`
- `Student.csv`

**CSV File Format Examples:**

**country.csv:**
```csv
country_code,country_name
US,United States
AU,Australia
```

**City.csv:**
```csv
name,lat,long,country_id
New York,40.7128,-74.0060,1
```

**Temp.csv:**
```csv
year,average_temp,maximum_temp,minimum_temp,city_id,country_id,state_id,global_id
2020,15.5,25.0,5.0,1,1,NULL,NULL
```

### 4. Run the Application

**Option 1: Using IDE**
- Open the project in your IDE
- Run `WebApplication.java` as a Spring Boot application

**Option 2: Using Maven**
```bash
mvn spring-boot:run
```

**Option 3: Using Gradle**
```bash
./gradlew bootRun
```

### 5. Access the Application

Once started, navigate to:
- **Homepage**: http://localhost:8080/
- **Mission Statement**: http://localhost:8080/mission

## 📁 Project Structure

```
MyFinalAssigntment/
├── src/
│   ├── main/
│   │   ├── java/com/runweather/web/
│   │   │   ├── controller/          # REST/Web controllers
│   │   │   │   ├── LandingPageController.java
│   │   │   │   ├── GlobalController.java
│   │   │   │   ├── CountryController.java
│   │   │   │   ├── Level3SubtaskAController.java
│   │   │   │   ├── Level3SubtaskAmultipleController.java
│   │   │   │   └── Level3SubtaskBController.java
│   │   │   ├── entity/               # JPA entities
│   │   │   │   ├── country.java
│   │   │   │   ├── city.java
│   │   │   │   ├── state.java
│   │   │   │   ├── temperature.java
│   │   │   │   ├── population.java
│   │   │   │   └── Global.java
│   │   │   ├── repository/           # Data access layer
│   │   │   │   ├── countryRepository.java
│   │   │   │   ├── cityRepository.java
│   │   │   │   └── impl/             # CSV importers
│   │   │   │       ├── CsvImporter.java
│   │   │   │       ├── CityCsvImporter.java
│   │   │   │       └── ...
│   │   │   ├── service/              # Business logic
│   │   │   │   ├── countryService.java
│   │   │   │   └── imp/              # Service implementations
│   │   │   ├── dto/                  # Data Transfer Objects
│   │   │   ├── viewEntity/           # View models
│   │   │   └── WebApplication.java    # Main application class
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── templates/            # Thymeleaf templates
│   │       └── static/               # CSS, JS, images
│   └── test/                         # Test files
├── Persona.csv
├── Student.csv
└── README.md
```

## 🔧 Configuration

### Application Properties

Key configuration in `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/datalearning
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# File Upload Path (for CSV imports)
file.upload.path=/path/to/csv/directory

# Logging
logging.level.com.runweather.web=DEBUG
```

## 🌐 API Endpoints

### Navigation
- `GET /` - Landing page
- `GET /mission` - Mission statement page

### Level 2 - High-Level Analysis
- `GET /subtaskA/global` - Global temperature and population analysis
  - Query Parameters: `startYears`, `endYears`, `page`, `sortColumn`, `sortType`
  
- `GET /subtaskA/countries` - Country-level analysis
  - Query Parameters: `region` (City/State), `startYears`, `endYears`, `selectedCountry`, `page`, `sortColumn`, `sortType`

### Level 3 - Deep Analysis
- `GET /level3SubtaskA` - Single region multi-period analysis
  - Query Parameters: `region`, `startingYears` (comma-separated), `yearPeriod`, `selectedString`, `minAverageChange`, `maxAverageChange`, `minPopulation`, `maxPopulation`, `page`, `sortColumn`, `sortType`

- `GET /level3SubtaskA/multiple` - Multiple regions analysis
  - Query Parameters: Same as above, with `region` as comma-separated values

- `GET /level3SubtaskB` - Similar periods finder
  - Query Parameters: `region`, `selectedString`, `startingYears`, `yearPeriod`, `viewBy` (temp/pop/both), `number` (number of similar periods), `minAverageChange`, `maxAverageChange`, `minPopulation`, `maxPopulation`

## 🗄️ Database Schema

### Core Entities

**country**
- `id` (PK)
- `country_code`
- `country_name`

**city**
- `id` (PK)
- `name`
- `lat`
- `long`
- `country_id` (FK)

**state**
- `id` (PK)
- `name`
- `country_id` (FK)

**temperature**
- `id` (PK)
- `year`
- `average_temp`
- `maximum_temp`
- `minimum_temp`
- `city_id` (FK, nullable)
- `state_id` (FK, nullable)
- `country_id` (FK, nullable)
- `global_id` (FK, nullable)

**population**
- `id` (PK)
- `year`
- `number`
- `country_id` (FK)

**global**
- `id` (PK)
- `country_name`

## 📊 Data Import

The application automatically imports CSV data on first startup:

1. Checks `ImportStatus` table to prevent duplicate imports
2. Reads CSV files from the configured `file.upload.path` directory
3. Imports data in the following order:
   - Countries → Cities → States → Global → Population → Temperature → Personas → Students
4. Updates import status to prevent re-importing on subsequent starts

To re-import data, delete the `ImportStatus` record or set `dataImported` to `false`.

## 🎨 Frontend Features

- **Responsive Design**: Bootstrap 5 for mobile-friendly layouts
- **Interactive Tables**: Sortable columns with pagination
- **Dynamic Filtering**: Real-time filtering by region, year, temperature, and population
- **Data Visualization**: Formatted display of temperature changes and correlations
- **Custom Styling**: Enhanced CSS for better user experience

## 🔍 Key Features Explained

### Temperature Change Calculation
- **Average Difference**: `(end_year_avg_temp - start_year_avg_temp)`
- **Maximum Difference**: `(end_year_max_temp - start_year_max_temp)`
- **Minimum Difference**: `(end_year_min_temp - start_year_min_temp)`

### Correlation Calculation
- Correlation between temperature change and population change:
  ```
  ABS((avg_temp_diff / population_diff) * 10,000,000)
  ```

### Ranking System
- Ranks locations based on selected metrics (average, min, or max temperature change)
- Uses SQL `ROW_NUMBER()` for efficient ranking

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Verify PostgreSQL is running
   - Check database credentials in `application.properties`
   - Ensure database `datalearning` exists

2. **CSV Import Fails**
   - Verify `file.upload.path` is correct
   - Ensure all CSV files exist in `{file.upload.path}/Csv/` directory
   - Check CSV file formats match expected structure

3. **Port Already in Use**
   - Change port in `application.properties`: `server.port=8081`
   - Or stop the process using port 8080

4. **Data Not Displaying**
   - Check if CSV import completed successfully
   - Verify data exists in database tables
   - Check application logs for SQL errors

## 📝 Development Notes

- The application uses dynamic SQL query generation for flexible data analysis
- Controllers handle complex multi-CTE SQL queries for performance
- View entities separate presentation logic from business logic
- CSV importers use Apache Commons CSV for robust file parsing

## 👥 Authors

- See `/mission` page for author information loaded from `Student.csv`

## 📄 License

This project is developed as a final assignment. All rights reserved.

## 🙏 Acknowledgments

- Spring Boot community
- PostgreSQL team
- Bootstrap team
- Apache Commons CSV

---

**Note**: This application is designed for educational and research purposes. Ensure you have proper data usage rights for any climate data used in production environments.
