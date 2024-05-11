package com.runweather.web.viewEntity.level3SubtaskA;

public class Region {
    private String name;
    private int id;
    private Boolean selected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Region( int id,String name, Boolean selected) {
        this.name = name;
        this.id = id;
        this.selected = selected;
    }
}
