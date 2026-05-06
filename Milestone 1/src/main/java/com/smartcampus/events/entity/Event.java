package com.smartcampus.events.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    @Column(length = 1000)
    private String description;

    @NotNull(message = "Event date and time is required")
    @FutureOrPresent(message = "Event date must be in the present or future")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "event_date")
    private LocalDateTime date;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Event type is required")
    @Column(name = "event_type")
    private String eventType;

    @NotBlank(message = "Location is required")
    private String location;

    // Constructors
    public Event() {}

    public Event(String title, String description, LocalDateTime date, String department, String eventType, String location) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.department = department;
        this.eventType = eventType;
        this.location = location;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
