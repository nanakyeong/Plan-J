package com.example.planj;

import com.example.planj.frame.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.planj")
public class PlanJApplication {

    public class FontLoader {
        private static final Map<String, Font> fontRegistry = new HashMap<>();

        // 폰트를 로드하여 등록하는 메서드
        public static void loadCustomFont(String fontPath, String fontName) {
            try {
                File fontFile = new File(fontPath);
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);
                fontRegistry.put(fontName, customFont); // 폰트를 Map에 저장
                System.out.println("폰트 로드 성공: " + fontName + " (" + fontPath + ")");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("폰트 로드 실패: " + fontName + " (" + fontPath + ")");
            }
        }
        public static Font getFont(String fontName, float size, int style) {
            Font font = fontRegistry.get(fontName);
            if (font != null) {
                return font.deriveFont(style, size);
            }
            System.out.println("등록되지 않은 폰트: " + fontName);
            return new Font("Default", style, Math.round(size)); // 대체 폰트 반환
        }
    }
    public static void main(String[] args) {
        JoinFrame.FontLoader.loadCustomFont(
                "C:\\Users\\Owner\\Desktop\\workspace\\java\\Plan-J\\src\\main\\java\\com\\example\\planj\\font\\Gumi Romance.ttf",
                "낭만있구미체"
        );
        JoinFrame.FontLoader.loadCustomFont(
                "C:\\Users\\Owner\\Desktop\\workspace\\java\\Plan-J\\src\\main\\java\\com\\example\\planj\\font\\SejongGeulggot.otf",
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
