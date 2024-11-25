package com.example.planj.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public List<PlanDTO> getAllPlans() {
        return planRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public PlanDTO getPlanById(Long id) {
        Optional<Plan> plan = planRepository.findById((long) id.intValue());
        return plan.map(this::convertToDTO).orElse(null);
    }

    public PlanDTO createPlan(PlanDTO planDTO) {
        Plan plan = convertToEntity(planDTO);
        Plan savedPlan = planRepository.save(plan);
        return convertToDTO(savedPlan);
    }

    public PlanDTO updatePlan(Long id, PlanDTO planDTO) {
        Optional<Plan> optionalPlan = planRepository.findById((long) id.intValue());
        if (optionalPlan.isPresent()) {
            Plan existingPlan = optionalPlan.get();
            existingPlan.setTitle(planDTO.getTitle());
            existingPlan.setNights(planDTO.getNights());
            existingPlan.setDays(planDTO.getDays());
            existingPlan.setRegion(planDTO.getRegion());
            existingPlan.setAccommodation(planDTO.getAccommodation());
            existingPlan.setPlaces(planDTO.getPlaces());

            Plan updatedPlan = planRepository.save(existingPlan);
            return convertToDTO(updatedPlan);
        }
        return null;
    }

    public void deletePlan(Long id) {
        if (planRepository.existsById((long) id.intValue())) {
            planRepository.deleteById((long) id.intValue());
        }
    }

    private PlanDTO convertToDTO(Plan plan) {
        PlanDTO planDTO = new PlanDTO();
        planDTO.setId(plan.getId());
        planDTO.setTitle(plan.getTitle());
        planDTO.setNights(plan.getNights());
        planDTO.setDays(plan.getDays());
        planDTO.setRegion(plan.getRegion());
        planDTO.setAccommodation(plan.getAccommodation());
        planDTO.setPlaces(plan.getPlaces());
        return planDTO;
    }

    private Plan convertToEntity(PlanDTO planDTO) {
        Plan plan = new Plan();
        plan.setTitle(planDTO.getTitle());
        plan.setNights(planDTO.getNights());
        plan.setDays(planDTO.getDays());
        plan.setRegion(planDTO.getRegion());
        plan.setAccommodation(planDTO.getAccommodation());
        plan.setPlaces(planDTO.getPlaces());
        return plan;
    }
}