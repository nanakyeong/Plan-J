package com.example.planj.db;

import java.time.LocalDate;
import java.util.*;

public class PlanDTO {

    private Long id;
    private String title;
    private int nights;
    private int days;
    private String region;
    private String district;
    private LocalDate date;

    // 날짜별 숙소
    private Map<String, String> accommodationsPerDay = new HashMap<>();
    // 날짜별 장소
    private Map<String, List<String>> placesPerDay = new HashMap<>();

    public PlanDTO() {
        this.date = LocalDate.now();
    }

    public PlanDTO(Long id, String title, int nights, int days, String region, String district, LocalDate date,
                   Map<String, String> accommodationsPerDay, Map<String, List<String>> placesPerDay) {
        this.id = id;
        this.title = title;
        this.nights = nights;
        this.days = days;
        this.region = region;
        this.district = district;
        this.date = date != null ? date : LocalDate.now();
        this.accommodationsPerDay = accommodationsPerDay != null ? accommodationsPerDay : new HashMap<>();
        this.placesPerDay = placesPerDay != null ? placesPerDay : new HashMap<>();
    }

    // 날짜별 숙소 추가
    public void addAccommodation(String day, String accommodation) {
        accommodationsPerDay.put(day, accommodation);
    }

    // 날짜별 장소 추가
    public void addPlace(String day, String place) {
        placesPerDay.computeIfAbsent(day, k -> new ArrayList<>()).add(place);
    }

    // 특정 날짜의 숙소 가져오기
    public Optional<String> getAccommodation(String day) {
        return Optional.ofNullable(accommodationsPerDay.get(day));
    }

    // 특정 날짜의 장소 가져오기
    public Optional<List<String>> getPlaces(String day) {
        return Optional.ofNullable(placesPerDay.get(day));
    }

    // Getter and Setter
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
