package com.example.planj.db;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

public class PlanDTO {

    private Long id;
    private String title;
    private int nights;
    private int days;
    private String accommodation;
    private LocalDate date;


    private Map<String, String> accommodationsPerDay = new HashMap<>();
    private Map<String, List<String>> placesPerDay = new HashMap<>();

    public PlanDTO() {
        this.date = LocalDate.now();
    }

    public PlanDTO(Long id, String title, int nights, int days, String accommodation, LocalDate date,
                   Map<String, String> accommodationsPerDay, Map<String, List<String>> placesPerDay) {
        this.id = id;
        this.title = title;
        this.nights = nights;
        this.days = days;
        this.accommodation = accommodation;
        this.date = date != null ? date : LocalDate.now();
        this.accommodationsPerDay = accommodationsPerDay != null ? accommodationsPerDay : new HashMap<>();
        this.placesPerDay = placesPerDay != null ? placesPerDay : new HashMap<>();
    }

    public void addAccommodation(String day, String accommodation) {
        accommodationsPerDay.put(day, accommodation);
    }

    public void addPlace(String day, String place) {
        placesPerDay.computeIfAbsent(day, k -> new ArrayList<>()).add(place);
    }

    public Optional<String> getAccommodation(String day) {
        return Optional.ofNullable(accommodationsPerDay.get(day));
    }

    public Optional<List<String>> getPlaces(String day) {
        return Optional.ofNullable(placesPerDay.get(day));
    }

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

    public String getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(String accommodation) {
        this.accommodation = accommodation;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Map<String, String> getAccommodationsPerDay() {
        return accommodationsPerDay;
    }

    public void setAccommodationsPerDay(Map<String, String> accommodationsPerDay) {
        this.accommodationsPerDay = accommodationsPerDay;
    }

    public Map<String, List<String>> getPlacesPerDay() {
        return placesPerDay;
    }

    public void setPlacesPerDay(Map<String, List<String>> placesPerDay) {
        this.placesPerDay = placesPerDay;
    }

    @Override
    public String toString() {
        return "PlanDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", nights=" + nights +
                ", days=" + days +
                ", accommodation='" + accommodation + '\'' +
                ", date=" + date +
                ", accommodationsPerDay=" + accommodationsPerDay +
                ", placesPerDay=" + placesPerDay +
                '}';
    }
}
