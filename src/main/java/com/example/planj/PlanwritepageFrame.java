package com.example.planj;

import javax.swing.*;
import java.awt.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.net.URL;

public class PlanwritepageFrame extends JFrame {

    public PlanwritepageFrame() {
        setTitle("Plan J");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        JLabel logo1 = new JLabel("Plan J");
        logo1.setFont(new Font("돋움", Font.BOLD, 35));
        logo1.setBounds(123, 135, 150, 30);
        contentPane.add(logo1);

        JLabel ai = new JLabel("AI");
        ai.setBounds(668, 55, 100, 20);
        JLabel myplan = new JLabel("myplan");
        myplan.setBounds(700, 55, 100, 20);
        JLabel login = new JLabel("로그인");
        login.setBounds(762, 55, 100, 20);
        JLabel join = new JLabel("회원가입");
        join.setBounds(814, 55, 100, 20);
        contentPane.add(ai);
        contentPane.add(myplan);
        contentPane.add(login);
        contentPane.add(join);

        JLabel title = new JLabel("제목 : ");
        title.setBounds(138, 220, 210, 20);
        contentPane.add(title);

        JTextField plan_title = new JTextField();
        plan_title.setBounds(178, 220, 200, 23);
        contentPane.add(plan_title);

        Integer[] choices1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        JComboBox<Integer> section1 = new JComboBox<>(choices1);
        section1.setBounds(385, 220, 50, 23);
        contentPane.add(section1);

        JLabel day1 = new JLabel("박");
        day1.setBounds(450, 220, 30, 20);
        contentPane.add(day1);

        JComboBox<Integer> section2 = new JComboBox<>();
        section2.setBounds(475, 220, 50, 23);
        contentPane.add(section2);

        JLabel day2 = new JLabel("일");
        day2.setBounds(530, 220, 30, 20);
        contentPane.add(day2);

        section1.addActionListener(e -> {
            int selected = (Integer) section1.getSelectedItem();
            section2.removeAllItems();
            for (int i = selected; i <= selected + 1; i++) {
                section2.addItem(i + 1);
            }
        });

        JTextField plan_write = new JTextField();
        plan_write.setBounds(138, 280, 408, 228);
        contentPane.add(plan_write);

        section1.setSelectedIndex(0);

        MyPanel panel1 = new MyPanel();
        panel1.setBounds(0, 0, 1000, 600);
        contentPane.add(panel1);

        setVisible(true);
    }

    class MyPanel extends JPanel {
        private JFXPanel jfxPanel;

        public MyPanel() {
            setLayout(null);

            // JFXPanel을 생성하여 JavaFX WebView 포함
            jfxPanel = new JFXPanel();
            jfxPanel.setBounds(566, 221, 284, 288); // 지도 위치 지정
            add(jfxPanel);

            // JavaFX 스레드에서 WebView 초기화
            Platform.runLater(() -> {
                WebView webView = new WebView();
                WebEngine webEngine = webView.getEngine();

                // resources 폴더에 있는 map.html 파일을 로드
                URL url = getClass().getResource("/map.html");
                if (url != null) {
                    webEngine.load(url.toExternalForm());
                } else {
                    webEngine.loadContent("<html><body><h1>파일을 찾을 수 없습니다.</h1></body></html>");
                }

                // WebView를 JFXPanel에 설정
                jfxPanel.setScene(new Scene(webView));
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.black);
            g2.drawLine(123, 85, 866, 85);

            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.gray);
            g2.drawRect(123, 200, 743, 330); // 전체
            g2.drawRect(565, 220, 285, 290); // 지도 위치
        }
    }

    public static void main(String[] args) {
        new PlanwritepageFrame();
    }
}
