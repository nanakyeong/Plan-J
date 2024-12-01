package com.example.planj.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public void createPlan(Plan plan) {
        PlanDTO planDTO = new PlanDTO();
        planDTO.setTitle(plan.getTitle());
        planDTO.setRegion(plan.getRegion());
        planDTO.setAreaCode(plan.getAreaCode());
        planDTO.setDistrict(plan.getDistrict());
        createPlan(planDTO); // 계획 한번 이미 저장됨
        planRepository.save(plan); // 여기서도 저장이 되어버림
    }

    public List<PlanDTO> getAllPlans() {
        return planRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<PlanDTO> getIsRegisteredTrue() {
        return planRepository.findByIsRegistered(true).stream().map(this::convertToDTO).toList();
    }

    public Plan getPlanByTitle(String title) {
        return planRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Not Found By Title"));
    }

    public PlanDTO getPlanById(Long id) {
        Optional<Plan> plan = planRepository.findById(id);
        return plan.map(this::convertToDTO).orElse(null);
    }

    public PlanDTO createPlan(PlanDTO planDTO) {
        Plan plan = convertToEntity(planDTO);
        Plan savedPlan = planRepository.save(plan);
        return convertToDTO(savedPlan);
    }

    public PlanDTO updatePlan(Long id, PlanDTO planDTO) {
        Optional<Plan> optionalPlan = planRepository.findById(id);
        if (optionalPlan.isPresent()) {
            Plan existingPlan = optionalPlan.get();

            // 기본 정보 업데이트
            existingPlan.setTitle(planDTO.getTitle());
            existingPlan.setNights(planDTO.getNights());
            existingPlan.setDays(planDTO.getDays());
            existingPlan.setRegion(planDTO.getRegion());
            existingPlan.setDistrict(planDTO.getDistrict());

            // 날짜별 숙소와 장소 데이터를 반영
            List<PlacePerDay> updatedPlaces = planDTO.getPlacesPerDay().entrySet().stream()
                    .map(entry -> new PlacePerDay(
                            entry.getKey(),
                            entry.getValue(),
                            planDTO.getAccommodationsPerDay().get(entry.getKey()) // 해당 날짜의 숙소 정보 반영
                    ))
                    .collect(Collectors.toList());
            existingPlan.setPlacesPerDay(updatedPlaces);

            // 데이터베이스 저장
            Plan updatedPlan = planRepository.save(existingPlan);
            return convertToDTO(updatedPlan);
        }
        return null;
    }

    public boolean deletePlan(Long id) {
        if (planRepository.existsById(id)) {
            planRepository.deleteById(id);
        }
        return false;
    }

    private PlanDTO convertToDTO(Plan plan) {
        PlanDTO planDTO = new PlanDTO();
        planDTO.setId(plan.getId());
        planDTO.setTitle(plan.getTitle());
        planDTO.setNights(plan.getNights());
        planDTO.setDays(plan.getDays());
        planDTO.setDate(plan.getDate());
        planDTO.setRegion(plan.getRegion());
        planDTO.setDistrict(plan.getDistrict());
        planDTO.setAccommodationsPerDay(plan.getAccommodationsPerDay());

        // List<PlacePerDay> -> Map<String, List<String>>
        Map<String, List<String>> placesPerDayMap = new HashMap<>();
        Map<String, String> accommodationsPerDay = new HashMap<>();
        for (PlacePerDay placePerDay : plan.getPlacesPerDay()) {
            placesPerDayMap.put(placePerDay.getDay(), placePerDay.getPlaces());
            accommodationsPerDay.put(placePerDay.getDay(), placePerDay.getAccommodation()); // 추가
        }

        planDTO.setPlacesPerDay(placesPerDayMap);
        planDTO.setAccommodationsPerDay(accommodationsPerDay);

        return planDTO;
    }

    private Plan convertToEntity(PlanDTO planDTO) {
        Plan plan = new Plan();
        plan.setId(planDTO.getId());
        plan.setTitle(planDTO.getTitle());
        plan.setNights(planDTO.getNights());
        plan.setDays(planDTO.getDays());
        plan.setDate(planDTO.getDate());
        plan.setDistrict(planDTO.getDistrict());
        plan.setRegion(planDTO.getRegion());
        plan.setAccommodationsPerDay(planDTO.getAccommodationsPerDay());

        // Map<String, List<String>> -> List<PlacePerDay>
        List<PlacePerDay> placesPerDayList = planDTO.getPlacesPerDay().entrySet().stream()
                .map(entry -> new PlacePerDay(
                        entry.getKey(),
                        entry.getValue(),
                        planDTO.getAccommodationsPerDay().get(entry.getKey()) // 숙소 정보 반영
                ))
                .collect(Collectors.toList());
        plan.setPlacesPerDay(placesPerDayList);

        return plan;
    }

    public void modifyRegistered(Plan currentPlan) {
        currentPlan.RegisteredTrue();
        planRepository.save(currentPlan);
    }
}

