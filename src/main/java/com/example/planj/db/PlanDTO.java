package com.example.planj.db;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Data
public class PlanDTO {

    private Long id;
    private String title;
    private int nights;
    private int days;
    private String region;
    private String district;
    private LocalDate date;
    private int areaCode;
    private boolean isRegistered;
    // 날짜별 숙소
    private Map<String, String> accommodationsPerDay = new HashMap<>();
    // 날짜별 장소
    private Map<String, List<String>> placesPerDay = new HashMap<>();

    public PlanDTO() {
        this.date = LocalDate.now();
    }

    public Plan toPlan() {
        Plan plan = new Plan();
        plan.setTitle(this.title);
        plan.setDistrict(this.district);
        plan.setRegistered(this.isRegistered);
        return plan;
    }
}

