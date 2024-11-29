package com.example.planj;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.swing.*;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.planj.db")
public class PlanJApplication {

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        ApplicationContext context = SpringApplication.run(PlanJApplication.class, args);

        SwingUtilities.invokeLater(() -> {
            MainpageFrame mainFrame = context.getBean(MainpageFrame.class);
            mainFrame.setVisible(true);
        });
    }
}
