package com.runweather.web.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student")
public class student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "s_id")
    private String sid;
    @Column(name= "name")
    private String name;

    public student(int id,String sid, String name) {
        this.id = id;
        this.name = name;
        this.sid = sid;
    }
    public student() {}

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

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
