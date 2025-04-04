package com.example.planj.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String title;

    private int nights;

    private int days;

    private int areaCode;

    @Column(nullable = false)
    private String region;

    private boolean isRegistered;

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public void RegisteredTrue() {
        isRegistered = true;
    }

    @Column(nullable = false)
    private String district;

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

    public Plan(String title, int nights, int days, LocalDate date, String region, String district,
                Map<String, String> accommodationsPerDay, Map<String, List<String>> placesPerDay) {
        this.title = title;
        this.nights = nights;
        this.days = days;
        this.district = district;
        this.date = date != null ? date : LocalDate.now();
        this.accommodationsPerDay = accommodationsPerDay;
        this.region = region;

        // Map<String, List<String>> -> List<PlacePerDay>
        if (placesPerDay != null) {
            this.placesPerDay = placesPerDay.entrySet().stream()
                    .map(entry -> new PlacePerDay(entry.getKey(), entry.getValue(), accommodationsPerDay.get(entry.getKey())))
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
