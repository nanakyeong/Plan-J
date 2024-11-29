package com.example.planj.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public List<PlanDTO> getAllPlans() {
        return planRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
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

            // 기본 정보 설정
            existingPlan.setTitle(planDTO.getTitle());
            existingPlan.setNights(planDTO.getNights());
            existingPlan.setDays(planDTO.getDays());
            existingPlan.setRegion(planDTO.getRegion());
            existingPlan.setDistrict(planDTO.getDistrict());

            // 날짜별 숙소 및 장소 데이터 설정
            Map<String, String> accommodationsPerDay = planDTO.getAccommodationsPerDay();
            Map<String, List<String>> placesPerDayMap = planDTO.getPlacesPerDay();

            // Map<String, List<String>> -> List<PlacePerDay> 변환
            List<PlacePerDay> placesPerDayList = placesPerDayMap.entrySet()
                    .stream()
                    .map(entry -> {
                        String day = entry.getKey();
                        List<String> places = entry.getValue();
                        String accommodation = accommodationsPerDay.get(day); // 해당 날짜의 숙소 정보
                        return new PlacePerDay(day, places, accommodation); // PlacePerDay 생성 시 숙소 정보 추가
                    })
                    .collect(Collectors.toList());

            existingPlan.setPlacesPerDay(placesPerDayList);

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


}
