package com.example.planj;

import com.example.planj.frame.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.swing.*;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.planj")
public class PlanJApplication {


    public static void main(String[] args) {
        // 클래스패스를 통해 폰트 로드
        JoinFrame.FontLoader.loadCustomFont(
                JoinFrame.class.getResourceAsStream("/Gumi_Romance.otf"),
                "낭만있구미체"
        );
        JoinFrame.FontLoader.loadCustomFont(
                JoinFrame.class.getResourceAsStream("/SejongGeulggot.otf"),
                "세종글꽃체"
        );

        System.setProperty("java.awt.headless", "false");

        ApplicationContext context = SpringApplication.run(PlanJApplication.class, args);

        SwingUtilities.invokeLater(() -> {
            MainpageFrame mainpageFrame = context.getBean(MainpageFrame.class);
            mainpageFrame.setVisible(true);
        });
    }
}
