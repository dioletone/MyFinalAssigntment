package com.runweather.web.entity;

import jakarta.persistence.*;
import lombok.Builder;


@Entity
@Table(name = "Persona")
public class persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Lob
    @Column(name = "background", columnDefinition = "TEXT")
    private String background;
    @Lob
    @Column(name = "goals", columnDefinition = "TEXT")
    private String goals;
    @Lob
    @Column(name = "name ", columnDefinition = "TEXT")
    private String name;
    @Lob
    @Column(name = "needs", columnDefinition = "TEXT")
    private String needs;
    @Lob
    @Column(name="skills_and_experiences", columnDefinition = "TEXT")
    private String skillsAndExperiences;
    @Lob
    @Column(name = "frustration", columnDefinition = "TEXT")
    private String frustration;

    public persona(int id, String background, String goals, String name, String needs, String skillsAndExperiences, String frustration) {
        this.id = id;
        this.background = background;
        this.goals = goals;
        this.name = name;
        this.needs = needs;
        this.skillsAndExperiences = skillsAndExperiences;
        this.frustration = frustration;
    }

    public persona() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNeeds() {
        return needs;
    }

    public void setNeeds(String needs) {
        this.needs = needs;
    }

    public String getSkillsAndExperiences() {
        return skillsAndExperiences;
    }

    public void setSkillsAndExperiences(String skillsAndExperiences) {
        this.skillsAndExperiences = skillsAndExperiences;
    }

    public String getFrustration() {
        return frustration;
    }

    public void setFrustration(String frustration) {
        this.frustration = frustration;
    }
}
