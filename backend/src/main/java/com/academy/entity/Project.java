package com.academy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Project {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String tech;

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTech() { return tech; }
    public void setTech(String tech) { this.tech = tech; }
}
