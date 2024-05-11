package com.runweather.web.viewEntity.level3SubtaskAmultiple;

import java.util.ArrayList;

public class level3SubtaskAmultiple {
    private ArrayList<Region> regions;
    private int yearPeriod;
    private int[] startingYears;
    private double minAverageChange;
    private double maxAverageChange;
    private long minPopulation;
    private long maxPopulation;
    private ArrayList<Table> table;
    private int page;
    private int pageSize;
    private int totalPage;
    private String sortColumn;
    private String sortType;

    public ArrayList<Region> getRegions() {
        return regions;
    }

    public void setRegions(ArrayList<Region> regions) {
        this.regions = regions;
    }

    public int getYearPeriod() {
        return yearPeriod;
    }

    public void setYearPeriod(int yearPeriod) {
        this.yearPeriod = yearPeriod;
    }

    public int[] getStartingYears() {
        return startingYears;
    }

    public void setStartingYears(int[] startingYears) {
        this.startingYears = startingYears;
    }

    public double getMinAverageChange() {
        return minAverageChange;
    }

    public void setMinAverageChange(double minAverageChange) {
        this.minAverageChange = minAverageChange;
    }

    public double getMaxAverageChange() {
        return maxAverageChange;
    }

    public void setMaxAverageChange(double maxAverageChange) {
        this.maxAverageChange = maxAverageChange;
    }

    public long getMinPopulation() {
        return minPopulation;
    }

    public void setMinPopulation(long minPopulation) {
        this.minPopulation = minPopulation;
    }

    public long getMaxPopulation() {
        return maxPopulation;
    }

    public void setMaxPopulation(long maxPopulation) {
        this.maxPopulation = maxPopulation;
    }

    public ArrayList<Table> getTable() {
        return table;
    }

    public void setTable(ArrayList<Table> table) {
        this.table = table;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
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
