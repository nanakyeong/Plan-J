package com.example.planj.db;

import javax.persistence.*;
import java.util.List;

@Entity
public class PlacePerDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String day;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "places", joinColumns = @JoinColumn(name = "place_per_day_id"))
    @Column(name = "place")
    private List<String> places;

    public PlacePerDay() {
    }

    public PlacePerDay(String day, List<String> places) {
        this.day = day;
        this.places = places;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<String> getPlaces() {
        return places;
    }

    public void setPlaces(List<String> places) {
        this.places = places;
    }
}

