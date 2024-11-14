package com.example.planj;

import com.example.planj.db.PlanService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.example.planj")
public class AppConfig {
    @Bean
    public PlanService planService() {
        return new PlanService();
    }
}
