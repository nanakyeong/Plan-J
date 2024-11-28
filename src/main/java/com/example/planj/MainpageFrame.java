package com.example.planj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainpageFrame extends JFrame {
    private JPanel contentPanel; // 게시물 표시 패널
    private JLabel searchResultLabel; // 검색 결과 라벨
    private JTextField search_plan; // 검색 텍스트 필드

    public MainpageFrame() {
        setTitle("Plan J");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        JLabel logo1 = new JLabel("Plan J");
        logo1.setFont(new Font("돋움", Font.BOLD, 35));
        logo1.setBounds(123, 135, 150, 30);
        contentPane.add(logo1);

        // Plan J 로고 클릭 시 초기화
        logo1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logo1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetToDefault(); // 초기 상태로 되돌림
            }
        });

        JLabel myplan = new JLabel("myplan");
        myplan.setBounds(700, 55, 100, 20);
        JLabel login = new JLabel("로그인");
        login.setBounds(762, 55, 100, 20);
        JLabel join = new JLabel("회원가입");
        join.setBounds(814, 55, 100, 20);
        contentPane.add(myplan);
        contentPane.add(login);
        contentPane.add(join);

        // 검색 패널
        // 검색 패널
        JPanel searchPanel = new JPanel();
        searchPanel.setBounds(500, 142, 380, 23); // 크기를 늘려서 라디오 버튼 추가 가능
        searchPanel.setLayout(null);
        contentPane.add(searchPanel);

        // 라디오 버튼 그룹 생성
        ButtonGroup radioGroup = new ButtonGroup();
        JRadioButton regionRadioButton = new JRadioButton("지역");
        regionRadioButton.setBounds(0, 0, 60, 23);
        regionRadioButton.setSelected(true); // 기본 선택
        searchPanel.add(regionRadioButton);

        JRadioButton placeRadioButton = new JRadioButton("장소");
        placeRadioButton.setBounds(60, 0, 60, 23);
        searchPanel.add(placeRadioButton);

        // 라디오 버튼 그룹에 추가
        radioGroup.add(regionRadioButton);
        radioGroup.add(placeRadioButton);

        // 검색 텍스트 필드
        search_plan = new JTextField();
        search_plan.setBounds(120, 0, 210, 23);
        searchPanel.add(search_plan);

        // 검색 아이콘
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setBounds(330, 0, 30, 22);
        searchIcon.setHorizontalAlignment(SwingConstants.CENTER);
        searchPanel.add(searchIcon);

        // 검색 결과 라벨
        searchResultLabel = new JLabel("");
        searchResultLabel.setFont(new Font("돋움", Font.BOLD, 18));
        searchResultLabel.setBounds(123, 190, 500, 30);
        contentPane.add(searchResultLabel);

        // 게시물 표시 패널
        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBounds(0, 230, 1000, 340);
        contentPane.add(contentPanel);

        // 기본 게시물 표시
        initializeDefaultContent();

        // 검색 동작 설정
        search_plan.addActionListener(e -> triggerSearch());
        searchIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                triggerSearch();
            }
        });

        // 하단 라인 그리기
        MyPanel panel1 = new MyPanel();
        panel1.setBounds(0, 0, 1000, 600);
        contentPane.add(panel1);

        setVisible(true);
    }

    // 초기 상태로 되돌리는 메서드
    private void resetToDefault() {
        searchResultLabel.setText(""); // 라벨 비우기
        initializeDefaultContent(); // 기본 게시물 표시
    }

    // 기본 게시물 표시
    // 기본 게시물 표시
    private void initializeDefaultContent() {
        contentPanel.removeAll();

        // 버튼과 라벨 위치 및 텍스트 설정
        int[][] positions = {
                {123, 0}, {323, 0}, {533, 0}, {743, 0}, // 첫 번째 줄
                {123, 170}, {323, 170}, {533, 170}, {743, 170}  // 두 번째 줄
        };

        String[] labels = {
                "plan 업로드", "- plan_name", "- plan_name", "- plan_name",
                "- plan_name", "- plan_name", "- plan_name", "- plan_name"
        };

        for (int i = 0; i < positions.length; i++) {
            // 버튼 생성
            JButton btn_plan = new JButton(i == 0 ? "+" : ""); // 첫 번째 버튼은 "+"
            btn_plan.setBounds(positions[i][0], positions[i][1], 120, 120);
            contentPanel.add(btn_plan);

            // 라벨 생성
            JLabel label = new JLabel(labels[i]);
            label.setBounds(positions[i][0] + 20, positions[i][1] + 120, 100, 20);
            contentPanel.add(label);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }


    // 검색 동작 트리거
    private void triggerSearch() {
        String searchText = search_plan.getText().trim();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "검색어를 입력하세요!", "오류", JOptionPane.WARNING_MESSAGE);
        } else {
            performSearch(searchText);
        }
    }

    // 검색 결과 표시
    private void performSearch(String searchText) {
        searchResultLabel.setText("\"" + searchText + "\"가 포함된 검색 결과입니다.");
        contentPanel.removeAll();

        // 버튼과 라벨 위치 및 텍스트 설정
        int[][] positions = {
                {123, 0}, {323, 0}, {533, 0}, {743, 0}, // 첫 번째 줄
                {123, 170}, {323, 170}, {533, 170}, {743, 170}  // 두 번째 줄
        };

        for (int i = 0; i < positions.length; i++) {
            // 버튼 생성
            JButton btn_plan = new JButton("결과 " + (i + 1));
            btn_plan.setBounds(positions[i][0], positions[i][1], 120, 120);
            contentPanel.add(btn_plan);

            // 라벨 생성
            JLabel label = new JLabel("- " + searchText + " 결과 " + (i + 1));
            label.setBounds(positions[i][0] + 20, positions[i][1] + 120, 100, 20);
            contentPanel.add(label);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }


    // 하단 라인 그리기
    class MyPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.black);
            g2.drawLine(123, 85, 866, 85);
        }
    }

    public static void main(String[] args) {
        new MainpageFrame();
    }
}
