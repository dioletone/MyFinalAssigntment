package com.runweather.web.controller;

import com.runweather.web.viewEntity.level2SubtaskA.Table;
import com.runweather.web.viewEntity.level2SubtaskA.level2SubtaskA;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class GlobalController {
    private com.runweather.web.viewEntity.level2SubtaskA.level2SubtaskA level2SubtaskA;
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;



    //Generate query
    public static String generateQuery(int startYears, int endYears, int page, int pageSize, String sortType, String sortColumn ){
        String parseSortColumn = "";
        if("0".equals(sortColumn)) {
            parseSortColumn = "AvgDiff";
        }
        else if("1".equals(sortColumn)){
            parseSortColumn = "MaxDiff";
        }
        else if("2".equals(sortColumn)){
            parseSortColumn= "MinDiff";
        }
        else if ("3".equals(sortColumn)) {
            parseSortColumn="PopulationDifference";
        } else {
            parseSortColumn= "Correlation";
        }
        StringBuilder query = new StringBuilder();
        query.append("With startYears As ( Select c.country_name, t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp")
                .append(" From temperature t ")
                .append("   Join public.global c on c.id = t.global_id")
                .append("   Join population p on c.id = p.country_id and t.year = p.year")
                .append("   Where t.year = ").append(startYears)
                .append("   Group By c.country_name, t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp")
                .append("),")
                .append(" endYears As ( Select c.country_name, t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp")
                .append(" From temperature t ")
                .append("   Join public.global c on c.id = t.global_id")
                .append("   Join population p on c.id = p.country_id and t.year = p.year")
                .append("   Where t.year=").append(endYears)
                .append("   Group By c.country_name, t.year, p.number, t.maximum_temp, t.minimum_temp, t.average_temp")
                .append(")")
                .append("Select s.country_name as country ,")
                .append("ROUND(CAST((e.average_temp - s.average_temp) AS numeric), 2) AS AvgDiff,")
                .append("ROUND(CAST((e.maximum_temp - s.maximum_temp) AS numeric), 2) AS MaxDiff,")
                .append("ROUND(CAST((e.minimum_temp - s.minimum_temp) AS numeric), 2) AS MinDiff,")
                .append("CAST((e.number - s.number) AS integer) AS PopulationDifference,")
                .append("ABS(CAST(((e.average_temp - s.average_temp) / (e.number - s.number) * 10000000) AS DECIMAL(16,2))) AS Correlation")
                .append(" from startYears s ")
                .append(" join endYears e on s.country_name = e.country_name");


        if (parseSortColumn != null && !parseSortColumn.isEmpty() && sortType != null && !sortType.isEmpty()) {
            query.append(" ORDER BY ").append(parseSortColumn)
                    .append(" ").append(sortType);
        }
        query.append(" LIMIT ").append(pageSize).append(" OFFSET ").append((page - 1) * pageSize);

        return query.toString();
    }
    public String[][] executeQuery(int startYears, int endYears, int page, int pageSize, String sortType, String sortColumn) {
        List<String[]> resultRows = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sqlQuery = generateQuery( startYears, endYears, page, pageSize, sortType, sortColumn);
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

    }




    @GetMapping("/subtaskA/global")
    public String listGlobal(Model model,
        @RequestParam(name = "endYears", required = false) String endYears,
        @RequestParam(name = "startYears", required = false) String startYears,
        @RequestParam(name = "page", required = false) String page,
        @RequestParam(name = "sortColumn", required = false) String sortColumn,
        @RequestParam(name = "sortType", required = false) String sortType){
        level2SubtaskA modelView = new level2SubtaskA();


        int parsedEndYears = 0;
        int parsedStartYears = 0;
        int parsedPage = 1;
        int pageSize = 10;

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
        String[] dynamicHeader = new String[]{"Name", "Average Temperature", "Maximum Temperature", "Minimum Temperature" , "Population", "Correlation"};


        String[][] data = executeQuery(parsedStartYears, parsedEndYears, parsedPage, pageSize, sortType, sortColumn);

        Table table = new Table(dynamicHeader, data);



        modelView.setStartYears(parsedStartYears);
        modelView.setEndYears(parsedEndYears);
        modelView.setPage(parsedPage);
        modelView.setTable(table);


        if ("ASC".equals(sortType)) {
            System.err.println("ASC");
            model.addAttribute("nextSortType", "DESC");

        } else if ("DESC".equals(sortType)) {
            System.err.println("DESC");

            model.addAttribute("nextSortType", "");

        } else {


            model.addAttribute("nextSortType", "ASC");
        }


        modelView.setSortColumn((sortColumn != null && !sortColumn.isEmpty()) ? sortColumn : "");
        modelView.setSortType((sortType != null && !sortType.isEmpty()) ? sortType : "");
 int length = modelView.getTable().getData().length;
        model.addAttribute("lengthData", length);
        model.addAttribute("modelView", modelView);




        return "globaltest";
    }
    @PostMapping("/subtaskA/global/")
    public String listGLobalPost(@ModelAttribute("level2SubtaksA") level2SubtaskA modelView,Model model,
                                 @RequestParam(name = "endYears", required = false) String endYears,
                                 @RequestParam(name = "startYears", required = false) String startYears,
                                 @RequestParam(name = "page", required = false) String page,
                                 @RequestParam(name = "sortColumn", required = false) String sortColumn,
                                 @RequestParam(name = "sortType", required = false) String sortType){



            int parsedEndYears = 0;
            int parsedStartYears = 0;
            int parsedPage = 1;
            int pageSize = 10;



            if (page != null && !page.isEmpty()) {
                try {
                    parsedPage = Integer.parseInt(page);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            String[] dynamicHeader = new String[]{"Name", "Average Temperature", "Maximum Temperature", "Minimum Temperature" , "Population", "Correlation"};


            String[][] data = executeQuery(parsedStartYears, parsedEndYears, parsedPage, pageSize, sortType, sortColumn);


            Table table = new Table(dynamicHeader, data);



            modelView.setStartYears(parsedStartYears);
            modelView.setEndYears(parsedEndYears);
            modelView.setPage(parsedPage);
            modelView.setTable(table);


            if ("ASC".equals(sortType)) {
                System.err.println("ASC");
                model.addAttribute("nextSortType", "DESC");

            } else if ("DESC".equals(sortType)) {
                System.err.println("DESC");

                model.addAttribute("nextSortType", "");

            } else {


                model.addAttribute("nextSortType", "ASC");
            }


            modelView.setSortColumn((sortColumn != null && !sortColumn.isEmpty()) ? sortColumn : "");
            modelView.setSortType((sortType != null && !sortType.isEmpty()) ? sortType : "");




            model.addAttribute("modelView", modelView);
        return "globaltest" + endYears + startYears;
    }
}
