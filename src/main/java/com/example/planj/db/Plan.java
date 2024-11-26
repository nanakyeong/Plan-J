package com.example.planj.db;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private String accommodation;

    @Column(nullable = false, updatable = false)
    private LocalDate date;

    @ElementCollection
    @CollectionTable(name = "plan_accommodations", joinColumns = @JoinColumn(name = "plan_id"))
    @MapKeyColumn(name = "day")
    @Column(name = "accommodation")
    private Map<String, String> accommodationsPerDay; // 날짜별 숙소

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id")
    private List<PlacePerDay> placesPerDay = new ArrayList<>();

    public Plan() {
    }

    public Plan(String title, int nights, int days, String accommodation, LocalDate date,
                Map<String, String> accommodationsPerDay, Map<String, List<String>> placesPerDay) {
        this.title = title;
        this.nights = nights;
        this.days = days;
        this.accommodation = accommodation;
        this.date = date != null ? date : LocalDate.now();
        this.accommodationsPerDay = accommodationsPerDay;

        // Map<String, List<String>> -> List<PlacePerDay>
        if (placesPerDay != null) {
            this.placesPerDay = placesPerDay.entrySet().stream()
                    .map(entry -> new PlacePerDay(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
        } else {
            this.placesPerDay = new ArrayList<>();
        }
    }


    @PrePersist
    public void prePersist() {
        if (this.date == null) {
            this.date = LocalDate.now();
        }
    }

    // Getters and Setters
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

    public List<PlacePerDay> getPlacesPerDay() {
        return placesPerDay;
    }

    public void setPlacesPerDay(List<PlacePerDay> placesPerDay) {
        this.placesPerDay = placesPerDay;
    }


    @Override
    public String toString() {
        return "Plan{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", nights=" + nights +
                ", days=" + days +
                ", accommodation='" + accommodation + '\'' +
                ", date=" + date +
                ", accommodationsPerDay=" + accommodationsPerDay +
                ", placesPerDay=" + placesPerDay.stream()
                .map(PlacePerDay::toString)
                .collect(Collectors.joining(", ")) +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plan plan = (Plan) o;
        return Objects.equals(id, plan.id) && Objects.equals(title, plan.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

}