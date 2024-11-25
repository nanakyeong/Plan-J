package com.example.planj.db;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RestController
@RequestMapping("/plans")
@CrossOrigin(origins = "http://localhost:3000")
public class PlanController {

    @Autowired
    private PlanRepository planRepository;

    @GetMapping
    public ResponseEntity<List<Plan>> getAllPlans() {
        List<Plan> plans = planRepository.findAll();
        return new ResponseEntity<>(plans, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plan> getPlanById(@PathVariable Integer id) {
        Optional<Plan> plan = planRepository.findById(Long.valueOf(id));
        return plan.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Plan> createPlan(@RequestBody Plan plan) {
        Plan savedPlan = planRepository.save(plan);
        return new ResponseEntity<>(savedPlan, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Plan> updatePlan(@PathVariable Integer id, @RequestBody Plan planDetails) {
        Optional<Plan> optionalPlan = planRepository.findById(Long.valueOf(id));
        if (optionalPlan.isPresent()) {
            Plan existingPlan = optionalPlan.get();
            existingPlan.setTitle(planDetails.getTitle());
            existingPlan.setNights(planDetails.getNights());
            existingPlan.setDays(planDetails.getDays());
            existingPlan.setRegion(planDetails.getRegion());
            existingPlan.setAccommodation(planDetails.getAccommodation());
            existingPlan.setPlaces(planDetails.getPlaces());

            Plan updatedPlan = planRepository.save(existingPlan);
            return new ResponseEntity<>(updatedPlan, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Integer id) {
        if (planRepository.existsById(Long.valueOf(id))) {
            planRepository.deleteById(Long.valueOf(id));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}


