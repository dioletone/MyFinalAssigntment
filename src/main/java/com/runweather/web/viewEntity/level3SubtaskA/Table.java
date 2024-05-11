package com.runweather.web.viewEntity.level3SubtaskA;

public class Table {
    private String[] header;
    private String[][] data;

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
}
