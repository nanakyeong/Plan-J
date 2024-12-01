package com.example.planj;

import com.example.planj.frame.LoginFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.swing.*;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.planj")
public class PlanJApplication {

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        ApplicationContext context = SpringApplication.run(PlanJApplication.class, args);

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = context.getBean(LoginFrame.class); // LoginFrame 로드
            loginFrame.setVisible(true);
        });
    }
}

