package com.runweather.web.viewEntity.level3SubtaskAmultiple;

public class Table {
    private String[] header;
    private String[][] data;
    private String selectedRegion;

    public String[] getHeader() {
        return header;
    }

    public void setHeader(String[] header) {
        this.header = header;
    }

    public String[][] getData() {
        return data;
    }

    public void setData(String[][] data) {
        this.data = data;
    }

    public Table(String[] header, String[][] data) {
        this.header = header;
        this.data = data;
    }

    public Table(String[] header, String[][] data, String selectedRegion) {
        this.header = header;
        this.data = data;
        this.selectedRegion = selectedRegion;
    }

    public String getSelectedRegion() {
        return selectedRegion;
    }

    public void setSelectedRegion(String selectedRegion) {
        this.selectedRegion = selectedRegion;
    }
}
