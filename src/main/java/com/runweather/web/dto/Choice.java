package com.runweather.web.dto;

import com.runweather.web.entity.country;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

import java.util.List;



public class Choice {


    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String[] getList(){
        return getName().split(",");
    }
}

