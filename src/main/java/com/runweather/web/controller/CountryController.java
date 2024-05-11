package com.runweather.web.controller;

import com.runweather.web.dto.countryDto;
import com.runweather.web.viewEntity.level2SubtaskB.Region;
import com.runweather.web.viewEntity.level2SubtaskB.Table;
import com.runweather.web.viewEntity.level2SubtaskA.level2SubtaskA;
import com.runweather.web.viewEntity.level2SubtaskB.level2SubtaskB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Controller
public class CountryController {
    private com.runweather.web.viewEntity.level2SubtaskB.level2SubtaskB level2SubtaskB;
    private com.runweather.web.service.countryService countryService;

    @Autowired
    public CountryController(com.runweather.web.service.countryService countryService) {
        this.countryService = countryService;
    }

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    //Generate query
    public static String generateQuery(String region, int startYears, int endYears, int page, int pageSize, String sortType, String sortColumn, String selectedCountry) {
        String selectedRegion = null;
        String selectedId = null;


        if ("City".equals(region)) {
            selectedRegion = "city";
            selectedId = "city_id";

        } else if ("State".equals(region)) {
            selectedRegion = "state";
            selectedId = "state_id";

        }

        String parseSortColumn = "";
        if ("0".equals(sortColumn)) {
            parseSortColumn = "AvgDiff";
        } else if ("1".equals(sortColumn)) {
            parseSortColumn = "MaxDiff";
        } else if ("2".equals(sortColumn)) {
            parseSortColumn = "MinDiff";
        } else if ("3".equals(sortColumn)) {
            parseSortColumn = "Correlation";
        } else if ("4".equals(sortColumn)) {
            parseSortColumn = "rank_AVG";
        } else if ("5".equals(sortColumn)) {
            parseSortColumn = "rank_MAX";
        } else if ("6".equals(sortColumn)) {
            parseSortColumn = "rank_MIN";
        }


        StringBuilder query = new StringBuilder();
    if (selectedRegion != null) {

        query.append("With startYears As ( Select c1.name, t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp, c.country_name")
                .append(" From temperature t ")
                .append("   Join public.").append(selectedRegion)
                .append("    c1 on c1.id = t.").append(selectedId)
                .append("   Join country c on c.id = c1.country_id")
                .append("   Join population p on c.id = p.country_id and t.year = p.year")
                .append("   Where t.year = ").append(startYears)
                .append(" Group By c1.name, t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp, c.country_name")
                .append("),")
                .append(" endYears As ( Select c1.name, t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp, c.country_name")
                .append(" From temperature t ")
                .append("   Join public.").append(selectedRegion)
                .append("    c1 on c1.id = t.").append(selectedId)
                .append("   Join country c on c.id = c1.country_id")
                .append("   Join population p on c.id = p.country_id and t.year = p.year")
                .append("   Where t.year = ").append(endYears)
                .append(" Group By c1.name, t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp, c.country_name")
                .append("),")
                .append(" resultData as (")
                .append("Select s.name")
                .append(" ,")
                .append("ROUND(CAST((e.average_temp - s.average_temp) AS numeric), 2) AS AvgDiff,")
                .append("ROUND(CAST((e.maximum_temp - s.maximum_temp) AS numeric), 2) AS MaxDiff,")
                .append("ROUND(CAST((e.minimum_temp - s.minimum_temp) AS numeric), 2) AS MinDiff,")
                .append("ABS(CAST(((e.average_temp - s.average_temp) / (e.number - s.number) * 10000000) AS DECIMAL(16,2))) AS Correlation")
                .append(" from startYears s ")
                .append(" join endYears e on s.name = e.name and s.country_name = e.country_name ")
                .append(" where s.country_name in('").append(selectedCountry).append("')")
                .append("  group by s.name, AvgDiff,MaxDiff,MinDiff,Correlation ),")
                .append("  rankData as (")
                .append("  select r.name, r.AvgDiff, r.MaxDiff, r.MinDiff, r.Correlation,")
                .append("  ROW_NUMBER() OVER (Order by r.AvgDiff) as rank_AVG,")
                .append("  ROW_NUMBER() OVER (Order by r.MaxDiff) as rank_Max,")
                .append("  ROW_NUMBER() OVER (Order by r.MinDiff) as rank_Min")
                .append(" from resultData r")
                .append("  group by r.name, r.AvgDiff, r.MaxDiff, r.MinDiff, r.Correlation)")
                .append("  select *")
                .append(" from rankData r1")
                .append("  Natural join resultData");}



        if (parseSortColumn != null && !parseSortColumn.isEmpty() && sortType != null && !sortType.isEmpty()) {
            query.append(" ORDER BY ").append(parseSortColumn)
                    .append(" ").append(sortType);
        }
        //query.append(" LIMIT ").append(pageSize).append(" OFFSET ").append((page - 1) * pageSize);

        return query.toString();
    }

    public String[][] executeQuery(String region, int startYears, int endYears, int page, int pageSize, String sortType, String sortColumn, String selectedCountry) {
        List<String[]> resultRows = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sqlQuery = generateQuery(region, startYears, endYears, page, pageSize, sortType, sortColumn, selectedCountry);
            System.out.println(sqlQuery);
            if(sqlQuery != null && !sqlQuery.isEmpty()) {

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    // Create an array to store the current row's data
                    String[] row = new String[columnCount];

                    for (int i = 1; i <= columnCount; i++) {
                        // Retrieve data from each column and add it to the row array
                        row[i - 1] = resultSet.getString(i);
                    }

                    // Add the row to the resultRows list
                    resultRows.add(row);
                }
            }

        } }catch (SQLException e) {
            e.printStackTrace();
        }

        String[][] resultArray = new String[resultRows.size()][];
        resultRows.toArray(resultArray);

        return resultArray;

    }

    public static String countTotalPage(String region, int startYears, int endYears, String selectedCountry) {
        StringBuilder query = new StringBuilder();
        String selectedRegion = null;
        String selectedId = null;


        if ("City".equals(region)) {
            selectedRegion = "city";
            selectedId = "city_id";
        } else if ("State".equals(region)) {
            selectedRegion = "state";
            selectedId = "state_id";
        }
        if(selectedRegion != null && !selectedRegion.isEmpty()) {
        query.append("With startYears As ( Select c1.name, t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp, c.country_name")
                .append(" From temperature t ")
                .append("   Join public.").append(selectedRegion)
                .append("    c1 on c1.id = t.").append(selectedId)
                .append("   Join country c on c.id = c1.country_id")
                .append("   Join population p on c.id = p.country_id and t.year = p.year")
                .append("   Where t.year = ").append(startYears)
                .append(" Group By c1.name, t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp, c.country_name")
                .append("),")
                .append(" endYears As ( Select c1.name, t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp, c.country_name")
                .append(" From temperature t ")
                .append("   Join public.").append(selectedRegion)
                .append("    c1 on c1.id = t.").append(selectedId)
                .append("   Join country c on c.id = c1.country_id")
                .append("   Join population p on c.id = p.country_id and t.year = p.year")
                .append("   Where t.year = ").append(endYears)
                .append(" Group By c1.name, t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp, c.country_name")
                .append(")")
                .append(" select COUNT(*) ")
                .append(" from startYears s")
                .append(" join endYears e on s.name = e.name and s.country_name = e.country_name ")
                .append(" where s.country_name in('").append(selectedCountry).append("')");}

        return query.toString();

    }

    public int executeCount(String region, int startYears, int endYears, String selectedCountry) {
        int result = 0;
        try (java.sql.Connection connection = DriverManager.getConnection(url, username, password)) {
            String sqlQuery = countTotalPage(region, startYears, endYears, selectedCountry);
//            System.out.println(sqlQuery);
            if(sqlQuery != null && !sqlQuery.isEmpty()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    // Fetch the result as a String
                    result = resultSet.getInt(1);
                }
            }

        } }catch (SQLException e) {
            e.printStackTrace();
        }

        return result;

    }

    private Region findSelectedRegion(ArrayList<Region> regions) {
        for (Region region : regions) {
            if (region.getSelected()) {
                return region;
            }
        }
        return null; // Return null if no region is selected
    }

    private ArrayList<Region> convertStringToRegion(String regionName) {
        ArrayList<Region> regions = new ArrayList<>();
        regions.add(new Region(1, "City", true));
        regions.add(new Region(2, "State", false));
        for (Region region : regions) {
            if (region.getName().equals(regionName)) {
                region.setSelected(true);
            } else {
                region.setSelected(false);
            }
        }
        return regions;
    }


    @GetMapping("/subtaskA/countries")
    public String listGlobal(Model model,
                             @RequestParam(name = "region", required = false) String region,
                             @RequestParam(name = "endYears", required = false) String endYears,
                             @RequestParam(name = "startYears", required = false) String startYears,
                             @RequestParam(name = "page", required = false) String page,
                             @RequestParam(name = "sortColumn", required = false) String sortColumn,
                             @RequestParam(name = "sortType", required = false) String sortType,
                             @RequestParam(name = "selectedCountry", required = false) String selectedCountry) {
        com.runweather.web.viewEntity.level2SubtaskB.level2SubtaskB modelView = new level2SubtaskB();
        ArrayList<Region> regions = convertStringToRegion(region);


        int parsedEndYears = 0;
        int parsedStartYears = 0;
        int parsedPage = 1;
        int pageSize = 10;
        if (selectedCountry != null && !selectedCountry.isEmpty()) {
            try {
                selectedCountry = selectedCountry.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (endYears != null && !endYears.isEmpty()) {
            try {
                parsedEndYears = Integer.parseInt(endYears);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        if (startYears != null && !startYears.isEmpty()) {
            try {
                parsedStartYears = Integer.parseInt(startYears);
                System.out.println(parsedStartYears);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (page != null && !page.isEmpty()) {
            try {
                parsedPage = Integer.parseInt(page);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        String[] dynamicHeader = new String[]{"Name", "Average Temperature", "Maximum Temperature", "Minimum Temperature","Correlation","Rank-AVG","Rank-MAX","Rank-MIN"};

        String[][] data = executeQuery(region, parsedStartYears, parsedEndYears, parsedPage, pageSize, sortType, sortColumn, selectedCountry);
        double totalPageDouble = (double) executeCount(region, parsedStartYears, parsedEndYears, selectedCountry) / pageSize;

        int totalPage = (int) Math.ceil(totalPageDouble);

        Table table = new Table(dynamicHeader, data);


        modelView.setStartYears(parsedStartYears);
        modelView.setEndYears(parsedEndYears);
        modelView.setPage(parsedPage);
        modelView.setTable(table);
        modelView.setTotalPage(totalPage);
        modelView.setSelectedCountry(selectedCountry);
        modelView.setRegion(regions);


        if ("ASC".equals(sortType)) {
            System.err.println("ASC");
            model.addAttribute("nextSortType", "DESC");

        } else if ("DESC".equals(sortType)) {
            System.err.println("DESC");

            model.addAttribute("nextSortType", "");

        } else {
            System.err.println("null");

            model.addAttribute("nextSortType", "ASC");
        }
        System.err.println("sortType: " + sortType);
        String parsedSelectedCountry = "";
        if (selectedCountry != null && !selectedCountry.isEmpty()) {
            try{
                parsedSelectedCountry = selectedCountry;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        modelView.setSortColumn((sortColumn != null && !sortColumn.isEmpty()) ? sortColumn : "");
        modelView.setSortType((sortType != null && !sortType.isEmpty()) ? sortType : "");
        int length = modelView.getTable().getData().length;
        List<countryDto> countries = countryService.findAllCountry();
        model.addAttribute("lengthData", length);
        model.addAttribute("modelView", modelView);
        model.addAttribute("countries", countries);
        model.addAttribute("selectedCountry", parsedSelectedCountry);



        return "countries-list" ;
    }

}


