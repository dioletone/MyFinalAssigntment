package com.runweather.web.viewEntity.level2SubtaskB;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;


@NoArgsConstructor
@AllArgsConstructor
public class level2SubtaskB {
    private ArrayList<Region> region;
    private String selectedCountry;
    private int startYears;
    private int endYears;
    private double averageTemperature;
    private double maxTemperature;
    private double minTemperature;
    private long Population;
    private com.runweather.web.viewEntity.level2SubtaskB.Table table;
    private int Page;
    private int totalPage;
    private int pageSize;
    private String sortColumn;
    private String sortType;

    public ArrayList<Region> getRegion() {
        return region;
    }

    public void setRegion(ArrayList<Region> region) {
        this.region = region;
    }

    public String getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(String selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public int getStartYears() {
        return startYears;
    }

    public void setStartYears(int startYears) {
        this.startYears = startYears;
    }

    public int getEndYears() {
        return endYears;
    }

    public void setEndYears(int endYears) {
        this.endYears = endYears;
    }

    public double getAverageTemperature() {
        return averageTemperature;
    }

    public void setAverageTemperature(double averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public long getPopulation() {
        return Population;
    }

    public void setPopulation(long population) {
        Population = population;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public int getPage() {
        return Page;
    }

    public void setPage(int page) {
        Page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
}
