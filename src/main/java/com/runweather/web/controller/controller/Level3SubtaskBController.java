package com.runweather.web.controller;
import java.io.IOException;
import com.runweather.web.dto.cityDto;
import com.runweather.web.dto.countryDto;
import com.runweather.web.dto.stateDto;
import com.runweather.web.entity.country;
import com.runweather.web.service.cityService;
import com.runweather.web.service.stateService;
import com.runweather.web.viewEntity.level3SubtaskB.Table;
import com.runweather.web.viewEntity.level3SubtaskB.Region;
import com.runweather.web.viewEntity.level3SubtaskB.level3SubtaskB;
import com.runweather.web.viewEntity.level3SubtaskB.level3SubtaskB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
public class Level3SubtaskBController {
   private level3SubtaskB level3SubtaskB;
    private com.runweather.web.service.countryService countryService;
    private cityService cityService;
    private stateService stateService;
    @Autowired
    public Level3SubtaskBController(com.runweather.web.service.countryService countryService, cityService cityService, stateService stateService) {
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


    //Generate query
    public static String generateQuery(String region,String selectedString, int startingYears, int period, double minAverageChange,
                                       double maxAverageChange, long minPopulation, long maxPopulation, int page, int pageSize, String sortType,
                                       String sortColumn,String viewBY) {
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
        for (int i = 0; i < startingYears; i++) {

            if(Integer.toString(i).equals(sortColumn)) {
                parseSortColumn = "AvgDiff"+Integer.toString(i);
            }
        }
        if(Integer.toString(startingYears).equals(sortColumn)) {
            parseSortColumn = "rank";
        }
        if (viewBY== null){ viewBY ="";}
        if (period == 0){ period = 45; }
        int length = 44/period;
        StringBuilder query = new StringBuilder();
        int j = 0;
        if(length != 0){
        query.append("With");
        for ( int i = 0 ; i<length; i ++){
            query.append(" period").append(i)
                    .append(" as (")
                    .append(" select Min(t.year) as startYear,Max(t.year) as endYear,");
            if("temp".equals(viewBY)) {
                    query.append(" Round(Cast(AVG(t.average_temp) as numeric),2) as AvgTemp");}
                    if("pop".equals(viewBY)) {
                    query.append(" Round(CAST(AVG(p.number) as numeric),0) as Avgpop");}
                    else if (!"temp".equals(viewBY) && !"pop".equals(viewBY)) {
                        query.append(" Round(Cast(AVG(t.average_temp) as numeric),2) as AvgTemp,")
                                .append(" Round(CAST(AVG(p.number) as numeric),0) as Avgpop");
                    }
                    query.append(" from temperature t ")
                    .append(" join ").append(selectedRegion)
                    .append(" c1 on c1.id = t.").append(selectedId)
                    .append(" join population p on p.year = t.year")
                    .append(" where c1.")
                    .append("country".equals(selectedRegion)?"country_name":"name")
                    .append(" = '"+selectedString+"' and t.year between "+ (startingYears+i*period)+" and "+(startingYears+period+i*period));
            if (minAverageChange != 0 || maxAverageChange != 0) {
            query.append(" and t.average_temp between" + minAverageChange + " and "+maxAverageChange);}
            if (minPopulation != 0 || maxPopulation != 0) {
                query.append(" AND p.number BETWEEN "+minPopulation +" AND "+ maxPopulation );
            }
                    query.append(" ),");
            j++;}

        query.append(" result0 as (");
        for (int i= 1 ; i <j;i++){
            query.append(" select * from period"+i)
                    .append(i<(j-1)&& (j!=2)?" union all":"");
        }
        query.append(")")
                .append(" select * from period0")
                .append(" union all (")
                .append(" select *")
                .append(" from result0 r")
                .append(" order by ABS((r.")
                .append(viewBY.equals("pop") ? "Avgpop" : "AvgTemp")
                .append(" -(select ")
                .append(viewBY.equals("pop") ? "Avgpop" : "AvgTemp")
                .append(" from period0))) asc )");}






        if (parseSortColumn != null && !parseSortColumn.isEmpty() && sortType != null && !sortType.isEmpty()) {
            query.append(" ORDER BY ").append(parseSortColumn)
                    .append(" ").append(sortType);
        }
        //query.append(" LIMIT ").append(pageSize).append(" OFFSET ").append((page - 1) * pageSize);

        return query.toString();
    }

    public String[][] executeQuery(String region,String selectedString, int startingYears, int period, double minAverageChange,
                                   double maxAverageChange, long minPopulation, long maxPopulation, int page, int pageSize, String sortType,
                                   String sortColumn,String viewBy) {
        List<String[]> resultRows = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sqlQuery = generateQuery(region,selectedString, startingYears, period, minAverageChange, maxAverageChange,
                    minPopulation, maxPopulation, page, pageSize, sortType, sortColumn,viewBy);


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

    }// data1

    public static String generateQuery2(int number, String region,String selectedString, int startingYears, int period, double minAverageChange,
                                        double maxAverageChange, long minPopulation, long maxPopulation, int page, int pageSize, String sortType,
                                        String sortColumn){
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
        for (int i = 0; i < startingYears; i++) {

            if(Integer.toString(i).equals(sortColumn)) {
                parseSortColumn = "AvgDiff"+Integer.toString(i);
            }
        }
        if(Integer.toString(startingYears).equals(sortColumn)) {
            parseSortColumn = "rank";
        }

        StringBuilder query = new StringBuilder();
        query.append(" WITH one AS (")
                .append("SELECT ")
                .append(" c1.").append(selectedName).append(", ")
                .append(" Round(Cast(AVG(t.average_temp) as numeric),2) as AvgTemp")
                .append(" FROM temperature t ")
                .append(" JOIN ").append(selectedRegion).append(" c1 ON c1.id = t.").append(selectedId)
                .append(" JOIN population p ON p.year = t.year ")
                .append(" WHERE c1.").append("country".equals(selectedRegion) ? "country_name" : "name")
                .append(" = '").append(selectedString).append("' AND t.year BETWEEN ")
                .append(startingYears).append(" AND ").append(startingYears + period);

        if (minAverageChange != 0 || maxAverageChange != 0) {
            query.append(" and t.average_temp between" + minAverageChange + " and "+maxAverageChange);}
        if (minPopulation != 0 || maxPopulation != 0) {
            query.append(" AND p.number BETWEEN "+minPopulation +" AND "+ maxPopulation );
        }

        query.append(" GROUP BY c1.").append(selectedName)
                .append(" ), multi AS (")
                .append("SELECT ")
                .append(" c1.").append(selectedName).append(", ")
                .append(" Round(Cast(AVG(t.average_temp) as numeric),2) as AvgTemp")
                .append(" FROM temperature t ")
                .append(" JOIN ").append(selectedRegion).append(" c1 ON c1.id = t.").append(selectedId)
                .append(" JOIN population p ON p.year = t.year ")
                .append(" WHERE c1.").append("country".equals(selectedRegion) ? "country_name" : "name")
                .append(" <> '").append(selectedString).append("' AND t.year BETWEEN ")
                .append(startingYears).append(" AND ").append(startingYears + period)
                .append(" GROUP BY c1.").append(selectedName)
                .append(" ) ")
                .append(" SELECT * FROM one ")
                .append(" UNION ALL ")
                .append(" (SELECT * FROM multi m ")
                .append(" ORDER BY (ABS(m.AvgTemp - (SELECT AvgTemp FROM one))) ASC ")
                .append(" LIMIT ").append(number).append(");");

        ;




        if (parseSortColumn != null && !parseSortColumn.isEmpty() && sortType != null && !sortType.isEmpty()) {
            query.append(" ORDER BY ").append(parseSortColumn)
                    .append(" ").append(sortType);
        }
        //query.append(" LIMIT ").append(pageSize).append(" OFFSET ").append((page - 1) * pageSize);

        return query.toString();

    }
    public String[][] executeQuery2(int number,String region,String selectedString, int startingYears, int period, double minAverageChange,
                                   double maxAverageChange, long minPopulation, long maxPopulation, int page, int pageSize, String sortType,
                                   String sortColumn) {
        List<String[]> resultRows = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sqlQuery = generateQuery2(number,region,selectedString, startingYears, period, minAverageChange, maxAverageChange,
                    minPopulation, maxPopulation, page, pageSize, sortType, sortColumn);


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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[][] resultArray = new String[resultRows.size()][];
        resultRows.toArray(resultArray);

        return resultArray;

    }// data2


    private Region findSelectedRegion(ArrayList<Region> regions) {
        for (Region region : regions) {
            if (region.getSelected()) {
                return region;
            }
        }
        return null; // Return null if no region is selected
    }

    private ArrayList<com.runweather.web.viewEntity.level3SubtaskB.Region> convertStringToRegion(String regionName) {
        ArrayList<com.runweather.web.viewEntity.level3SubtaskB.Region> regions = new ArrayList<>();
        regions.add(new com.runweather.web.viewEntity.level3SubtaskB.Region(1, "Country", true));
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
    private ArrayList<Double> parseGap(String[][] data, int index) {
        ArrayList<Double> gap = new ArrayList<>();
        if (data != null && data.length > 1) { // Check if data is not null and has at least two rows
            for (int i = 0; i < data.length - 1; i++) {
                if (data[i] != null && data[i + 1] != null && data[i].length > index && data[i + 1].length > index) {
                    try {
                        String currentValueStr = data[i + 1][index];
                        String initialvalueStr = data[0][index];
                        if (currentValueStr != null && initialvalueStr != null) { // Additional null check
                            double currentValue = Double.parseDouble(currentValueStr.trim());
                            double initialvalue = Double.parseDouble(initialvalueStr.trim());
                            gap.add(Math.round((currentValue - initialvalue) * 100) / 100.0);
                        } else {
                            // Handle null values if necessary
                        }
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace(); // Handle parsing errors or index out of bounds exceptions
                    }
                }
            }
        }
        return gap;
    }


    private String[][] formatData(String[][] data, ArrayList<Double> gapTemp, ArrayList<Double> gapPop) {
        for (int i = 1; i < data.length; i++) {
            if (gapTemp.size() > i - 1 && gapTemp.get(i - 1) != null) {
                double tempGap = gapTemp.get(i - 1);
                data[i][2] = String.format("%s(%s%.2f)", data[i][2], (tempGap > 0 ? "+" : ""), tempGap);
            }
            if (gapPop.size() > i - 1 && gapPop.get(i - 1) != null) {
                double popGap = gapPop.get(i - 1);
                data[i][3] = String.format("%s(%s%.2f)", data[i][3], (popGap > 0 ? "+" : ""), popGap);
            }
        }
        return data;
    }
    private String[][] formatData2(String[][] data, ArrayList<Double> gapTemp) {
        for (int i = 1; i < data.length; i++) {
            if (gapTemp.size() > i - 1 && gapTemp.get(i - 1) != null) {
                double tempGap = gapTemp.get(i - 1);
                data[i][1] = String.format("%s(%s%.2f)", data[i][1], (tempGap > 0 ? "+" : ""), tempGap);
            }

        }
        return data;
    }




    @GetMapping("/level3SubtaskB")
    public String listGlobal(Model model,
                             @RequestParam(name = "viewBy",required = false) String viewBy,
                             @RequestParam(name = "number", required = false) String number,
                             @RequestParam(name = "selectedString", required = false) String selectedString,
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
        int parsedNumber = 0;
        String parsedViewBy = null;

        if(viewBy != null) {
            try { parsedViewBy = viewBy.toLowerCase();

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        if(number != null && !number.isEmpty()) {
            try {
                parsedNumber = Integer.parseInt(number);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }


        if (yearPeriod != null && !yearPeriod.isEmpty()) {
            try {
               parsedYearPeriod = Integer.parseInt(yearPeriod);
            } catch (NumberFormatException e) {
                // Catch both NumberFormatException (for invalid integers) and StringIndexOutOfBoundsException (for empty strings)
                e.printStackTrace();
            }
        }


        level3SubtaskB modelView = new level3SubtaskB();
        int parsedStartingYears = 0;
        double parsedMinAverageChange = 0.0;
        double parsedMaxAverageChange = 0.0;
        long parsedMinPopulation = 0;
        long parsedMaxPopulation = 0;
        int parsedPage = 1;

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

        if (startingYears != null && !startingYears.isEmpty()) {
            try {
                // Use a regular expression to find the first integer in the string
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(startingYears);

                if (matcher.find()) {
                    // Parse the first integer found
                    parsedStartingYears = Integer.parseInt(matcher.group());
                } else {
                    // Handle the case where no integers are found
                    System.out.println("No integers found in the input string.");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }



        if (region == null) {
            region = "";
        }

        ArrayList<Double> gapTemp = new ArrayList<>();
        ArrayList<Double> gapPop = new ArrayList<>();
        ArrayList<com.runweather.web.viewEntity.level3SubtaskB.Table> tables = new ArrayList<>();


            String[][] data = executeQuery(region, selectedString, parsedStartingYears, parsedYearPeriod,
                    parsedMinAverageChange, parsedMaxAverageChange, parsedMinPopulation, parsedMaxPopulation,
                    parsedPage, pageSize, sortType, sortColumn, parsedViewBy);

            String[][] data2 = executeQuery2(parsedNumber,region, selectedString, parsedStartingYears, parsedYearPeriod,
                    parsedMinAverageChange, parsedMaxAverageChange, parsedMinPopulation, parsedMaxPopulation,
                    parsedPage, pageSize, sortType, sortColumn);

            if (data != null) {
                gapTemp = parseGap(data, 2);
                gapPop = parseGap(data, 3);
                String[][] formatData = formatData(data, gapTemp, gapPop);
            }
            if (data2 != null) {
                gapTemp = parseGap(data2, 1);
                String[][] formatData2 = formatData2(data2, gapTemp);
            }

if ("temp".equals(parsedViewBy)){
    com.runweather.web.viewEntity.level3SubtaskB.Table table = new Table(new String[]{"Start", "End", "AVG-TEMP"}, data);
    tables.add(table);
} else if ("pop".equals(parsedViewBy)){
    com.runweather.web.viewEntity.level3SubtaskB.Table table = new Table(new String[]{"Start", "End", "Population"}, data);
    tables.add(table);
}else if (!"temp".equals(parsedViewBy) && !"pop".equals(parsedViewBy)){
            com.runweather.web.viewEntity.level3SubtaskB.Table table = new Table(new String[]{"Start", "End", "AVG-TEMP", "Population"}, data);
    tables.add(table);
}
        com.runweather.web.viewEntity.level3SubtaskB.Table table2 = new Table(new String[]{"Name", "AVG-TEMP"}, data2);
        tables.add(table2);


        ArrayList<com.runweather.web.viewEntity.level3SubtaskB.Region> regions = convertStringToRegion(region);

        modelView.setRegions(regions);
        modelView.setYearPeriod(parsedYearPeriod);
        modelView.setStartingYears(parsedStartingYears);
        modelView.setMinAverageChange(parsedMinAverageChange);
        modelView.setMaxAverageChange(parsedMaxAverageChange);
        modelView.setMinPopulation(parsedMinPopulation);
        modelView.setMaxPopulation(parsedMaxPopulation);
        modelView.setPage(parsedPage);

        modelView.setTable(tables);

        if ("ASC".equals(sortType)) {
            model.addAttribute("nextSortType", "DESC");
        } else if ("DESC".equals(sortType)) {
            model.addAttribute("nextSortType", "");
        } else {
            model.addAttribute("nextSortType", "ASC");
        }

        Region selectedRegion = findSelectedRegion(regions);
        if (selectedRegion == null) {
            selectedRegion = new Region(1, "Country", true);
        }

        modelView.setSortColumn((sortColumn != null && !sortColumn.isEmpty()) ? sortColumn : "");
        modelView.setSortType((sortType != null && !sortType.isEmpty()) ? sortType : "");
        int length = modelView.getTable().size();
        List<countryDto> countries = countryService.findAllCountry();
        List<cityDto> cities = cityService.getAllCities();
        List<stateDto> states = stateService.getAllStates();
        String filePath = "/Users/phanmanhha/Documents/web/Csv/country.csv";


        boolean isViewByTemp = false;
        if ("temp".equals(parsedViewBy) || "true".equals(parsedViewBy)) { isViewByTemp = true;}
        boolean isViewByPop = false;
        if ("pop".equals(parsedViewBy) || "true".equals(parsedViewBy)) { isViewByPop = true;}



        model.addAttribute("selectedNumber", number);
        model.addAttribute("selectedRegion", region);
        model.addAttribute("selectedString", selectedString);
        model.addAttribute("lengthData", length);
        model.addAttribute("countries", countries);
        model.addAttribute("cities", cities);
        model.addAttribute("states", states);
        model.addAttribute("modelView", modelView);
        model.addAttribute("gapTemp", gapTemp);
        model.addAttribute("gapPop", gapPop);
        model.addAttribute("isViewByTemp", isViewByTemp);
        model.addAttribute("isViewByPop", isViewByPop);

        return "level3SubtaskB";
    }


}
