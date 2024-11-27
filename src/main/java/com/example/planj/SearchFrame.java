package com.example.planj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SearchFrame extends JFrame {
    private String searchText; // 현재 검색어 저장
    private JPanel contentPanel; // 게시물 표시 패널
    private JLabel searchResultLabel; // 검색 결과 라벨
    private JTextField search_plan; // 검색 텍스트 필드

    public SearchFrame(String initialSearchText) {
        this.searchText = initialSearchText; // 초기 검색어 저장

        setTitle("Plan J Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        JLabel logo1 = new JLabel("Plan J");
        logo1.setFont(new Font("돋움", Font.BOLD, 35));
        logo1.setBounds(123, 135, 150, 30);
        contentPane.add(logo1);

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
        JPanel searchPanel = new JPanel();
        searchPanel.setBounds(630, 142, 250, 23);
        searchPanel.setLayout(null);
        contentPane.add(searchPanel);

        // 검색 텍스트 필드
        search_plan = new JTextField(initialSearchText); // 초기 검색어 표시
        search_plan.setBounds(0, 0, 210, 23);
        searchPanel.add(search_plan);

        // 검색 아이콘
        // 아이콘 또는 특수문자 라벨
        JLabel searchIcon = new JLabel("🔍"); // 아이콘 대신 특수문자 사용
        searchIcon.setBounds(210, 0, 30, 22); // 텍스트 필드 오른쪽 위치
        searchIcon.setHorizontalAlignment(SwingConstants.CENTER);
        searchPanel.add(searchIcon);

        search_plan.addActionListener(e -> {
            String searchText = search_plan.getText().trim();
            if (searchText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "검색어를 입력하세요!", "오류", JOptionPane.WARNING_MESSAGE);
            } else {
                performSearch(searchText);
            }
        });

        // 클릭 효과를 위한 마우스 리스너 추가
        searchIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // 마우스 오버 시 효과 (배경 색 변경)
                searchIcon.setOpaque(true);
                searchIcon.setBackground(Color.LIGHT_GRAY);
                searchIcon.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 손 모양 커서
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // 마우스가 나가면 원래 상태로
                searchIcon.setOpaque(false);
                searchIcon.setBackground(null);
            }
        });

        // 검색 결과 라벨
        searchResultLabel = new JLabel("\"" + searchText + "\"가 포함된 검색 결과입니다.");
        searchResultLabel.setFont(new Font("돋움", Font.BOLD, 18));
        searchResultLabel.setBounds(123, 190, 500, 30);
        contentPane.add(searchResultLabel);

        // 게시물 표시 패널
        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBounds(0, 230, 1000, 340); // 위치 지정
        contentPane.add(contentPanel);

        // 초기 검색 결과 표시
        performSearch(searchText);

        // 검색 필드 동작
        search_plan.addActionListener(e -> triggerSearch());

        // 검색 아이콘 동작
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

    // 검색 트리거 메서드
    private void triggerSearch() {
        String inputSearchText = search_plan.getText().trim();
        if (inputSearchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "검색어를 입력하세요!", "오류", JOptionPane.WARNING_MESSAGE);
        } else {
            performSearch(inputSearchText);
        }
    }

    // 검색 수행 메서드
    private void performSearch(String searchText) {
        this.searchText = searchText; // 검색어 저장

        // 검색 결과 라벨 업데이트
        searchResultLabel.setText("\"" + searchText + "\"가 포함된 검색 결과입니다.");

        // 게시물 갱신
        contentPanel.removeAll(); // 기존 게시물 제거

        for (int i = 0; i < 8; i++) {
            int x = 123 + (i % 4) * 200;
            int y = (i / 4) * 170;

            JButton btn_plan = new JButton("결과 " + (i + 1));
            btn_plan.setBounds(x, y, 120, 120);
            contentPanel.add(btn_plan);

            JLabel planLabel = new JLabel("- " + searchText + " 결과 " + (i + 1));
            planLabel.setBounds(x + 20, y + 130, 100, 20);
            contentPanel.add(planLabel);
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
}
