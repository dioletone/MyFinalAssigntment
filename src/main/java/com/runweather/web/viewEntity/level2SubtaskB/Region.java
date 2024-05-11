package com.runweather.web.viewEntity.level2SubtaskB;

public class Region {
    private int id;
    private String name;
    private Boolean selected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Region(int id, String name, Boolean selected) {
        this.id = id;
        this.name = name;
        this.selected = selected;
    }
}
