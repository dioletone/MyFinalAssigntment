package com.runweather.web.controller;

import com.runweather.web.dto.cityDto;
import com.runweather.web.dto.countryDto;
import com.runweather.web.dto.stateDto;
import com.runweather.web.service.cityService;
import com.runweather.web.service.stateService;
import com.runweather.web.viewEntity.level3SubtaskA.Region;
import com.runweather.web.viewEntity.level3SubtaskA.Table;
import com.runweather.web.viewEntity.level3SubtaskA.level3SubtaskA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class Level3SubtaskAController {
    private com.runweather.web.viewEntity.level3SubtaskA.level3SubtaskA level3SubtaskA;
    private com.runweather.web.service.countryService countryService;
    private com.runweather.web.service.cityService cityService;
    private com.runweather.web.service.stateService stateService;
    @Autowired
    public Level3SubtaskAController(com.runweather.web.service.countryService countryService, cityService cityService, stateService stateService) {
        this.countryService = countryService;
        this.cityService = cityService;
        this.stateService = stateService;
    }

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;




    //Parsing startYears
    private int[] parseStartingYears(String startingYears) {
        if (startingYears != null && !startingYears.isEmpty()) {
            String[] yearsArray = startingYears.split(",");
            int[] parsedYears = new int[yearsArray.length];

            for (int i = 0; i < yearsArray.length; i++) {
                try {
                    parsedYears[i] = Integer.parseInt(yearsArray[i].trim());
                } catch (NumberFormatException e) {
                    // Handle the case where a year is not a valid integer
                    e.printStackTrace();
                }
            }
            return parsedYears;
        } else {
            return new int[0]; // Return an empty array if startingYears is null or empty
        }
    }

    //Generate query
    public static String generateQuery(String region,ArrayList<String> selectedString, int[] startingYears, int period, double minAverageChange,
                                       double maxAverageChange, long minPopulation, long maxPopulation, int page, int pageSize, String sortType,
                                       String sortColumn) {
        String selectedRegion = null;
        String selectedId = null;
        String selectedName = null;


        if ("City".equals(region)) {
            selectedRegion = "city";
            selectedId = "city_id";
            selectedName = "name";

        } else if ("State".equals(region)) {
            selectedRegion = "state";
            selectedId = "state_id";
            selectedName = "name";

        }
        else if ("Country".equals(region)) {
            selectedRegion = "country";
            selectedId = "country_id";
            selectedName = "country_name";
        }
        else if ("Global".equals(region)) {
            selectedRegion = "global";
            selectedId = "global_id";
        }

        String parseSortColumn = "";
        for (int i = 0; i < startingYears.length; i++) {

            if(Integer.toString(i).equals(sortColumn)) {
                parseSortColumn = "AvgDiff"+Integer.toString(i);
            }
        }
        if(Integer.toString(startingYears.length).equals(sortColumn)) {
            parseSortColumn = "rank";
        }








        StringBuilder query = new StringBuilder();
        if (startingYears.length > 0) {
        query.append("With ");
        for (int i = 0; i < startingYears.length; i++) {

           query.append(" startYears").append(i)
                .append("  As ( Select c1.").append(selectedName)
                .append(" as c1name, t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp, c.country_name")
                .append(" From temperature t ")
                .append("   Join public.").append(selectedRegion)
                .append("    c1 on c1.id = t.").append(selectedId)
                .append("   Join country c on c.id = c1.");
           if(!"country".equals(selectedRegion)){query.append("country_id");}
           else { query.append("id");}
           query.append("   Join population p on c.id = p.country_id and t.year = p.year")
                .append("   Where t.year = ").append(startingYears[i]);
            if(selectedString != null && !selectedString.isEmpty()) {
                query.append(" and c1.").append(selectedName).append(" in (");
                for (int j = 0; j < selectedString.size(); j++) {
                    query.append("'"+selectedString.get(j)+"'");
                    if (j<selectedString.size()-1) {
                        query.append(",");
                    }
                }
                query.append(")");
            }
            if(minPopulation != 0 && maxPopulation != 0) { query.append(" and p.number between ").append(minPopulation).append(" and ").append(maxPopulation);}
            if(minAverageChange != 0 && maxAverageChange != 0) {query.append(" and t.average_temp between ").append(minAverageChange).append(" and ").append(maxAverageChange);}
           query.append(" Group By c1.").append(selectedName)
                .append(", t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp, c.country_name")
                .append("),")
                .append(" endYears").append(i)
                   .append("  As ( Select c1.").append(selectedName)
                   .append(" as c1name, t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp, c.country_name")
                   .append(" From temperature t ")
                   .append("   Join public.").append(selectedRegion)
                   .append("    c1 on c1.id = t.").append(selectedId)
                   .append("   Join country c on c.id = c1.");
            if(!"country".equals(selectedRegion)){query.append("country_id");}
            else { query.append("id");}
            query.append("   Join population p on c.id = p.country_id and t.year = p.year")
                    .append("   Where t.year = ").append(startingYears[i]+period);
            if(selectedString != null && !selectedString.isEmpty()) {
                query.append(" and c1.").append(selectedName).append(" in (");
                for (int j = 0; j < selectedString.size(); j++) {
                    query.append("'"+selectedString.get(j)+"'");
                    if (j<selectedString.size()-1) {
                        query.append(",");
                    }
                }
                query.append(")");
            }
            if(minPopulation != 0 && maxPopulation != 0) { query.append(" and p.number between ").append(minPopulation).append(" and ").append(maxPopulation);}
            if(minAverageChange != 0 && maxAverageChange != 0) {query.append(" and t.average_temp between ").append(minAverageChange).append(" and ").append(maxAverageChange);}
            query.append(" Group By c1.").append(selectedName)
                    .append(", t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp, c.country_name")
                    .append("),")
                .append(" resultData").append(i)
                .append(" As (")
                .append("Select s.c1name as rname ")
                .append(" ,")
                .append("ROUND(CAST((e.average_temp + s.average_temp)/2 AS numeric), 2) AS AvgDiff")
                .append(" from startYears").append(i)
                .append(" s")
                .append(" join endYears").append(i)
                .append("   e on s.c1name = e.c1name and s.country_name = e.country_name ")
                .append("  group by rname")
                .append(",AvgDiff)");
           if(i< startingYears.length-1) {query.append(",");}}

           query.append(" Select r0.rname");

        for (int i = 0; i < startingYears.length; i++) {
            query.append(",r").append(i).append(".AvgDiff").append(" as AvgDiff").append(i);}
        query.append(" , ROW_NUMBER() OVER(Order by (");
        for (int i = 0; i < startingYears.length; i++) {
            query.append("r").append(i).append(".AvgDiff");
            if(i<startingYears.length-1) {query.append(",");}
        }
        query.append(" )) as rank ");
                query.append(" from resultData0 r0");
        for (int i = 1; i < startingYears.length; i++) {
            query.append(" join resultData").append(i).append(" r").append(i).append(" on r").append(i-1).append(".rname").append(" = r").append(i).append(".rname");}}






        if (parseSortColumn != null && !parseSortColumn.isEmpty() && sortType != null && !sortType.isEmpty()) {
            query.append(" ORDER BY ").append(parseSortColumn)
                    .append(" ").append(sortType);
        }
        //query.append(" LIMIT ").append(pageSize).append(" OFFSET ").append((page - 1) * pageSize);

        return query.toString();
    }

    public String[][] executeQuery(String region,ArrayList<String> selectedString, int[] startingYears, int period, double minAverageChange,
                                   double maxAverageChange, long minPopulation, long maxPopulation, int page, int pageSize, String sortType,
                                   String sortColumn) {
        List<String[]> resultRows = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sqlQuery = generateQuery(region,selectedString, startingYears, period, minAverageChange, maxAverageChange,
                    minPopulation, maxPopulation, page, pageSize, sortType, sortColumn);
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

        }} catch (SQLException e) {
            e.printStackTrace();
        }

        String[][] resultArray = new String[resultRows.size()][];
        resultRows.toArray(resultArray);

        return resultArray;

    }

    public static String countTotalPage(String region, int[] startingYears, int period, double minAverageChange,
                                        double maxAverageChange, long minPopulation, long maxPopulation) {
        StringBuilder query = new StringBuilder();
        String selectedRegion = null;
        String selectedId = null;
        String selectedName = null;


        if ("City".equals(region)) {
            selectedRegion = "city";
            selectedId = "city_id";
            selectedName = "name";

        } else if ("State".equals(region)) {
            selectedRegion = "state";
            selectedId = "state_id";
            selectedName = "name";

        }
        else if ("Country".equals(region)) {
            selectedRegion = "country";
            selectedId = "country_id";
            selectedName = "country_name";
        }
        else if ("Global".equals(region)) {
            selectedRegion = "global";
            selectedId = "global_id";
        }
        if( startingYears.length >0){
        query.append("With ");
        for (int i = 0; i < startingYears.length; i++) {

            query.append(" startYears").append(i)
                    .append("  As ( Select c1.").append(selectedName)
                    .append(" as c1name, t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp, c.country_name")
                    .append(" From temperature t ")
                    .append("   Join public.").append(selectedRegion)
                    .append("    c1 on c1.id = t.").append(selectedId)
                    .append("   Join country c on c.id = c1.");
            if(!"country".equals(selectedRegion)){query.append("country_id");}
            else { query.append("id");}
            query.append("   Join population p on c.id = p.country_id and t.year = p.year")
                    .append("   Where t.year = ").append(startingYears[i]);
            if(minPopulation != 0 && maxPopulation != 0) { query.append(" and p.number between ").append(minPopulation).append(" and ").append(maxPopulation);}
            if(minAverageChange != 0 && maxAverageChange != 0) {query.append(" and t.average_temp between ").append(minAverageChange).append(" and ").append(maxAverageChange);}
            query.append(" Group By c1.").append(selectedName)
                    .append(", t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp, c.country_name")
                    .append("),")
                    .append(" endYears").append(i)
                    .append("  As ( Select c1.").append(selectedName)
                    .append(" as c1name, t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp, c.country_name")
                    .append(" From temperature t ")
                    .append("   Join public.").append(selectedRegion)
                    .append("    c1 on c1.id = t.").append(selectedId)
                    .append("   Join country c on c.id = c1.");
            if(!"country".equals(selectedRegion)){query.append("country_id");}
            else { query.append("id");}
            query.append("   Join population p on c.id = p.country_id and t.year = p.year")
                    .append("   Where t.year = ").append(startingYears[i]+period);
            if(minPopulation != 0 && maxPopulation != 0) { query.append(" and p.number between ").append(minPopulation).append(" and ").append(maxPopulation);}
            if(minAverageChange != 0 && maxAverageChange != 0) {query.append(" and t.average_temp between ").append(minAverageChange).append(" and ").append(maxAverageChange);}
            query.append(" Group By c1.").append(selectedName)
                    .append(", t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp, c.country_name")
                    .append("),")
                    .append(" resultData").append(i)
                    .append(" As (")
                    .append("Select s.c1name as rname ")
                    .append(" ,")
                    .append("ROUND(CAST((e.average_temp + s.average_temp)/2 AS numeric), 2) AS AvgDiff")
                    .append(" from startYears").append(i)
                    .append(" s")
                    .append(" join endYears").append(i)
                    .append("   e on s.c1name = e.c1name and s.country_name = e.country_name ")
                    .append("  group by rname")
                    .append(",AvgDiff)");
            if(i< startingYears.length-1) {query.append(",");}}


        query.append(" Select COUNT(*)").append(" from resultData0 r0");
        for (int i = 1; i < startingYears.length; i++) {
            query.append(" join resultData").append(i).append(" r").append(i).append(" on r").append(i-1).append(".").append(selectedRegion).append(" = r").append(i).append(".").append(selectedRegion);}}





        return query.toString();

    }

    public int executeCount(String region, int[] startingYears, int period, double minAverageChange,
                            double maxAverageChange, long minPopulation, long maxPopulation) {
        int result = 0;
        try (java.sql.Connection connection = DriverManager.getConnection(url, username, password)) {
            String sqlQuery = countTotalPage(region, startingYears, period, minAverageChange, maxAverageChange,
                    minPopulation, maxPopulation);
//            System.out.println(sqlQuery);
            if(sqlQuery != null && !sqlQuery.isEmpty()){
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    // Fetch the result as a String
                    result = resultSet.getInt(1);
                }
            }

        }} catch (SQLException e) {
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

    private ArrayList<com.runweather.web.viewEntity.level3SubtaskA.Region> convertStringToRegion(String regionName) {
        ArrayList<com.runweather.web.viewEntity.level3SubtaskA.Region> regions = new ArrayList<>();
        regions.add(new com.runweather.web.viewEntity.level3SubtaskA.Region(1, "Country", true));
        regions.add(new Region(2, "State", false));
        regions.add(new Region(3, "City", false));
        regions.add(new Region(4, "Global", false));
        for (Region region : regions) {
            if (region.getName().equals(regionName)) {
                region.setSelected(true);
            } else {
                region.setSelected(false);
            }
        }
        return regions;
    }


    @GetMapping("/level3SubtaskA")
    public String listGlobal(Model model,
                             @RequestParam(name = "selectedString",required = false) ArrayList<String> selectedString,
                             @RequestParam(name = "yearPeriod", required = false) String yearPeriod,
                             @RequestParam(name = "region", required = false) String region,
                             @RequestParam(name = "startingYears", required = false) String startingYears,
                             @RequestParam(name = "minAverageChange", required = false) String minAverageChange,
                             @RequestParam(name = "maxAverageChange", required = false) String maxAverageChange,
                             @RequestParam(name = "minPopulation", required = false) String minPopulation,
                             @RequestParam(name = "maxPopulation", required = false) String maxPopulation,
                             @RequestParam(name = "page", required = false) String page,
                             @RequestParam(name = "sortColumn", required = false) String sortColumn,
                             @RequestParam(name = "sortType", required = false) String sortType) {
        int parsedYearPeriod = 0;
        int pageSize = 10;
        if (yearPeriod != null && !yearPeriod.isEmpty()) {
            try {
                parsedYearPeriod = Integer.parseInt(yearPeriod);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        level3SubtaskA modelView = new level3SubtaskA();

        double parsedMinAverageChange = 0.0;
        double parsedMaxAverageChange = 0.0;
        long parsedMinPopulation = 0;
        long parsedMaxPopulation = 0;
        int parsedPage = 1;

        int[] parsedStartingYears = new int[0];
        if(startingYears != null && !startingYears.isEmpty()){
            try {
                parsedStartingYears = parseStartingYears(startingYears);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        System.out.println("parsedStartingYears: " + parseStartingYears(startingYears));
        if (minAverageChange != null && !minAverageChange.isEmpty()) {
            try {
                parsedMinAverageChange = Double.parseDouble(minAverageChange);
            } catch (NumberFormatException e) {

                e.printStackTrace();
            }
        }

        if (maxAverageChange != null && !maxAverageChange.isEmpty()) {
            try {
                parsedMaxAverageChange = Double.parseDouble(maxAverageChange);
            } catch (NumberFormatException e) {

                e.printStackTrace();
            }
        }

        if (minPopulation != null && !minPopulation.isEmpty()) {
            try {
                parsedMinPopulation = Long.parseLong(minPopulation);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (maxPopulation != null && !maxPopulation.isEmpty()) {
            try {
                parsedMaxPopulation = Long.parseLong(maxPopulation);
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

        int[] staringYears = parseStartingYears(startingYears);
        int lengthOfYears =1;
        int[] parsedYears ={1};
        if (startingYears != null && !startingYears.isEmpty()) {
            lengthOfYears =staringYears.length;
            String[] yearsArray = startingYears.split(",");
            parsedYears = new int[yearsArray.length];
            for (int i = 0; i < yearsArray.length; i++) {
                try {
                    parsedYears[i] = Integer.parseInt(yearsArray[i].trim());
                } catch (NumberFormatException e) {
                    // Handle the case where a year is not a valid integer
                    e.printStackTrace();
                }
            }
        }
        String[] dynamicHeader = new String[lengthOfYears+2];
        System.out.println(lengthOfYears);
        System.out.println(parsedYears.length);
        dynamicHeader[0]="Name";
        for(int i =1; i < lengthOfYears+1; i++){
            dynamicHeader[i] = parsedYears[i-1] + " - " + (parsedYears[i-1]+parsedYearPeriod);
        }
        dynamicHeader[lengthOfYears+1]="Rank";
        System.out.println(dynamicHeader[dynamicHeader.length-1]);




        String[][] data = executeQuery(region, selectedString, parsedStartingYears, parsedYearPeriod, parsedMinAverageChange,
                parsedMaxAverageChange, parsedMinPopulation, parsedMaxPopulation, parsedPage, pageSize, sortType,
                sortColumn);

        Table table = new Table(dynamicHeader,data);
        ArrayList<com.runweather.web.viewEntity.level3SubtaskA.Region> regions = convertStringToRegion(region);


        modelView.setRegions(regions);
        modelView.setYearPeriod(parsedYearPeriod);
        modelView.setStartingYears(parsedStartingYears);
        modelView.setMinAverageChange(parsedMinAverageChange);
        System.err.println(parsedMinAverageChange);
        modelView.setMaxAverageChange(parsedMaxAverageChange);
        modelView.setMinPopulation(parsedMinPopulation);
        modelView.setMaxPopulation(parsedMaxPopulation);
        modelView.setPage(parsedPage);

        modelView.setTable(table);


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
Region selectedRegion = findSelectedRegion(regions);
        if (selectedRegion == null)
            selectedRegion = new Region(1,"Country", true);
        modelView.setSortColumn((sortColumn != null && !sortColumn.isEmpty()) ? sortColumn : "");
        modelView.setSortType((sortType != null && !sortType.isEmpty()) ? sortType : "");
        int length = modelView.getTable().getData().length;
        List<countryDto> countries = countryService.findAllCountry();
        List<cityDto> cities = cityService.getAllCities();
        List<stateDto> states = stateService.getAllStates();
String listYear = "";
for (int i = 0 ; i< parsedStartingYears.length; i ++){
    listYear += parsedStartingYears[i];
    if ( i < parsedStartingYears.length-1){ listYear += ",";}

}
        ArrayList<String> selectedList = new ArrayList<>();
        if(selectedString != null && !selectedString.isEmpty()){
            try {


                selectedList = selectedString;}
            catch (Exception e) {
                e.printStackTrace();
            }
        }


        model.addAttribute("listYear", listYear);
        model.addAttribute("countries", countries);
        model.addAttribute("cities", cities);
        model.addAttribute("states", states);
        model.addAttribute("selectedRegion", selectedRegion);
        model.addAttribute("lengthData", length);
        model.addAttribute("modelView", modelView);
        model.addAttribute("selectedList", selectedList);




        return "level3SubtaskA";
    }

}
