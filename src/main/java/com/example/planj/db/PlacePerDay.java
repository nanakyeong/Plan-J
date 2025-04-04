package com.example.planj.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class PlacePerDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String day;

    @Column(length = 50, nullable = false)
    private String accommodation;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "places", joinColumns = @JoinColumn(name = "place_per_day_id"))
    @Column(name = "place")
    private List<String> places;

    public PlacePerDay() {
    }

    public PlacePerDay(String day, List<String> places, String accommodation) {
        this.day = day;
        this.places = places;
        this.accommodation = accommodation;
    }
}
