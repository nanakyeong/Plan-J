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
            existingPlan.setAccommodation(planDTO.getAccommodation());
            existingPlan.setAccommodationsPerDay(planDTO.getAccommodationsPerDay());

            // Map<String, List<String>> -> List<PlacePerDay> 변환
            List<PlacePerDay> placesPerDayList = planDTO.getPlacesPerDay()
                    .entrySet()
                    .stream()
                    .map(entry -> new PlacePerDay(entry.getKey(), entry.getValue()))
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
        planDTO.setAccommodation(plan.getAccommodation());
        planDTO.setDate(plan.getDate());
        planDTO.setAccommodationsPerDay(plan.getAccommodationsPerDay());

        // List<PlacePerDay> -> Map<String, List<String>>
        Map<String, List<String>> placesPerDayMap = plan.getPlacesPerDay().stream()
                .collect(Collectors.toMap(
                        PlacePerDay::getDay,  // 키: day
                        PlacePerDay::getPlaces // 값: places 리스트
                ));
        planDTO.setPlacesPerDay(placesPerDayMap);

        return planDTO;
    }


    private Plan convertToEntity(PlanDTO planDTO) {
        Plan plan = new Plan();
        plan.setId(planDTO.getId());
        plan.setTitle(planDTO.getTitle());
        plan.setNights(planDTO.getNights());
        plan.setDays(planDTO.getDays());
        plan.setAccommodation(planDTO.getAccommodation());
        plan.setDate(planDTO.getDate());
        plan.setAccommodationsPerDay(planDTO.getAccommodationsPerDay());

        // Map<String, List<String>> -> List<PlacePerDay>
        List<PlacePerDay> placesPerDayList = planDTO.getPlacesPerDay().entrySet().stream()
                .map(entry -> new PlacePerDay(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        plan.setPlacesPerDay(placesPerDayList);

        return plan;
    }



}
