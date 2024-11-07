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

    private JPanel planPanel;

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

        JTextField planTitle = new JTextField();
        planTitle.setBounds(178, 220, 200, 23);
        contentPane.add(planTitle);

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

        section1.addActionListener(e -> updateDays(section1, section2));
        section2.addActionListener(e -> updatePlanPanel(section2));

        // 일정 리스트 패널
        planPanel = new JPanel();
        planPanel.setLayout(new BoxLayout(planPanel, BoxLayout.Y_AXIS));
        planPanel.setBackground(Color.WHITE); // 흰색 배경 설정

        JScrollPane scrollPane = new JScrollPane(planPanel);
        scrollPane.setBounds(138, 280, 408, 228);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scrollPane);

        section1.setSelectedIndex(0);

        MyPanel panel1 = new MyPanel();
        panel1.setBounds(0, 0, 1000, 600);
        contentPane.add(panel1);

        setVisible(true);
    }

    private void updateDays(JComboBox<Integer> section1, JComboBox<Integer> section2) {
        int selectedNights = (Integer) section1.getSelectedItem();
        section2.removeAllItems();

        int minDays = selectedNights + 1;
        int maxDays = selectedNights + 2;

        for (int i = minDays; i <= maxDays; i++) {
            section2.addItem(i);
        }
    }

    private void updatePlanPanel(JComboBox<Integer> section2) {
        planPanel.removeAll();

        if (section2.getSelectedItem() != null) {
            int days = (Integer) section2.getSelectedItem();
            for (int i = 1; i <= days; i++) {
                addDayPanel(planPanel, i + "일차");
            }
        }

        planPanel.revalidate();
        planPanel.repaint();
    }

    private void addDayPanel(JPanel planPanel, String dayText) {
        JPanel dayPanel = new JPanel();
        dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));
        dayPanel.setBackground(Color.WHITE);

        JLabel dayLabel = new JLabel(dayText);
        dayLabel.setFont(new Font("돋움", Font.BOLD, 17));
        dayLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        dayLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dayPanel.add(dayLabel);

        JButton addPlaceButton = new JButton("장소를 추가하려면 클릭하세요...");
        addPlaceButton.setFont(new Font("돋움", Font.PLAIN, 12));
        addPlaceButton.setForeground(Color.GRAY);
        addPlaceButton.setFocusPainted(false);
        addPlaceButton.setContentAreaFilled(false);
        addPlaceButton.setBorderPainted(false);
        addPlaceButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addPlaceButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 0, 0));
        addPlaceButton.addActionListener(e -> openAddPlaceDialog(dayPanel));
        dayPanel.add(addPlaceButton);

        planPanel.add(dayPanel);
        planPanel.add(Box.createVerticalStrut(5));
    }

    private void openAddPlaceDialog(JPanel dayPanel) {
        PopupDialog dialog = new PopupDialog(this, dayPanel);
        dialog.setVisible(true);
    }

    public void addPlaceToSchedule(String placeName, JPanel dayPanel) {
        // 기존의 "장소를 추가하려면 클릭하세요..." 버튼 제거
        if (dayPanel.getComponentCount() > 0) {
            Component lastComponent = dayPanel.getComponent(dayPanel.getComponentCount() - 1);
            if (lastComponent instanceof JButton && ((JButton) lastComponent).getText().equals("장소를 추가하려면 클릭하세요...")) {
                dayPanel.remove(lastComponent);
            }
        }

        // 장소 항목 패널 생성
        JPanel placePanel = new JPanel();
        placePanel.setLayout(new BoxLayout(placePanel, BoxLayout.X_AXIS));
        placePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        placePanel.setBackground(Color.WHITE);
        placePanel.setMaximumSize(new Dimension(400, 30));

        // 장소 이름 라벨
        JLabel placeLabel = new JLabel(placeName);
        placeLabel.setFont(new Font("돋움", Font.PLAIN, 12));
        placeLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
        placeLabel.setAlignmentY(Component.CENTER_ALIGNMENT); // 세로 중앙 정렬
        placePanel.add(placeLabel);

        // 위로 이동 버튼
        JButton upButton = new JButton("▲");
        upButton.setFont(new Font("돋움", Font.PLAIN, 13));
        upButton.setFocusPainted(false);
        upButton.setContentAreaFilled(false);
        upButton.setBorderPainted(false);
        upButton.setPreferredSize(new Dimension(50, 20));
        upButton.setAlignmentY(Component.CENTER_ALIGNMENT); // 세로 중앙 정렬
        upButton.addActionListener(e -> movePlacePanelUp(dayPanel, placePanel));

        // 아래로 이동 버튼
        JButton downButton = new JButton("▼");
        downButton.setFont(new Font("돋움", Font.PLAIN, 13));
        downButton.setFocusPainted(false);
        downButton.setContentAreaFilled(false);
        downButton.setBorderPainted(false);
        downButton.setPreferredSize(new Dimension(50, 20));
        downButton.setAlignmentY(Component.CENTER_ALIGNMENT); // 세로 중앙 정렬
        downButton.addActionListener(e -> movePlacePanelDown(dayPanel, placePanel));

        // 삭제 버튼
        JButton deleteButton = new JButton("×");
        deleteButton.setFont(new Font("돋움", Font.BOLD, 14));
        deleteButton.setForeground(Color.RED);
        deleteButton.setFocusPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setBorderPainted(false);
        deleteButton.setPreferredSize(new Dimension(50, 20));
        deleteButton.setAlignmentY(Component.CENTER_ALIGNMENT); // 세로 중앙 정렬
        deleteButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 15));
        deleteButton.addActionListener(e -> {
            dayPanel.remove(placePanel);
            dayPanel.revalidate();
            dayPanel.repaint();
        });

        // 버튼들을 placePanel에 추가
        placePanel.add(Box.createHorizontalGlue()); // 라벨과 버튼들 사이의 여백
        placePanel.add(upButton);
        placePanel.add(downButton);
        placePanel.add(deleteButton);

        // dayPanel에 placePanel 추가
        dayPanel.add(placePanel);

        // 새로운 "장소를 추가하려면 클릭하세요..." 버튼 추가
        JButton addPlaceButton = new JButton("장소를 추가하려면 클릭하세요...");
        addPlaceButton.setFont(new Font("돋움", Font.PLAIN, 12));
        addPlaceButton.setForeground(Color.GRAY);
        addPlaceButton.setFocusPainted(false);
        addPlaceButton.setContentAreaFilled(false);
        addPlaceButton.setBorderPainted(false);
        addPlaceButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addPlaceButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 0, 0));
        addPlaceButton.addActionListener(e -> openAddPlaceDialog(dayPanel));
        dayPanel.add(addPlaceButton);

        // 레이아웃 업데이트
        dayPanel.revalidate();
        dayPanel.repaint();
    }

    // 위로 이동하는 메서드
    private void movePlacePanelUp(JPanel dayPanel, JPanel placePanel) {
        int index = dayPanel.getComponentZOrder(placePanel);
        if (index > 1) { // 첫 번째 위치로 이동하지 않도록 제한
            dayPanel.remove(placePanel);
            dayPanel.add(placePanel, index - 1);
            dayPanel.revalidate();
            dayPanel.repaint();
        }
    }

    // 아래로 이동하는 메서드
    private void movePlacePanelDown(JPanel dayPanel, JPanel placePanel) {
        int index = dayPanel.getComponentZOrder(placePanel);
        if (index < dayPanel.getComponentCount() - 2) { // 마지막 위치로 이동하지 않도록 제한
            dayPanel.remove(placePanel);
            dayPanel.add(placePanel, index + 1);
            dayPanel.revalidate();
            dayPanel.repaint();
        }
    }

    class MyPanel extends JPanel {
        private JFXPanel jfxPanel;

        public MyPanel() {
            setLayout(null);

            jfxPanel = new JFXPanel();
            jfxPanel.setBounds(566, 221, 284, 288);
            add(jfxPanel);

            Platform.runLater(() -> {
                WebView webView = new WebView();
                WebEngine webEngine = webView.getEngine();

                URL url = getClass().getResource("/map.html");
                if (url != null) {
                    webEngine.load(url.toExternalForm());
                } else {
                    webEngine.loadContent("<html><body><h1>파일을 찾을 수 없습니다.</h1></body></html>");
                }

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
            g2.drawRect(123, 200, 743, 330);
            g2.drawRect(565, 220, 285, 290);
        }
    }

    public static void main(String[] args) {
        new PlanwritepageFrame();
    }
}
