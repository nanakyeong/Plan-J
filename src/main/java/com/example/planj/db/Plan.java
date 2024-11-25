package com.example.planj.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    private int nights;
    private int days;

    @Column(length = 50, nullable = false)
    private String region;

    @Column(length = 100)
    private String accommodation;

    @ElementCollection
    @CollectionTable(name = "plan_places", joinColumns = @JoinColumn(name = "plan_id"))
    @Column(name = "place")
    private List<String> places;

    public Plan() {
    }

    public Plan(String title, int nights, int days, String region, String accommodation, List<String> places) {
        this.title = title;
        this.nights = nights;
        this.days = days;
        this.region = region;
        this.accommodation = accommodation;
        this.places = places;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<String> getPlaces() {
        return places;
    }

    public void setPlaces(List<String> places) {
        this.places = places;
    }
}
