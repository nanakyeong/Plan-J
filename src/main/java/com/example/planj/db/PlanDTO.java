package com.example.planj.db;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class PlanDTO {
    private Long id;
    private String title;
    private int nights;
    private int days;
    private String region;
    private String accommodation;
    private List<String> places;
    private LocalDate date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(String accommodation) {
        this.accommodation = accommodation;
    }

    public List<String> getPlaces() {
        return places;
    }

    public void setPlaces(List<String> places) {
        this.places = places;
    }

    public LocalDate getDate() {return date != null ? date : LocalDate.now();}

    public void setDate(LocalDate date) {this.date = date;}
}
