package com.example.planj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SearchFrame extends JFrame {

    public SearchFrame() {
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

        // 패널 생성
        JPanel searchPanel = new JPanel();
        searchPanel.setBounds(630, 142, 250, 23);
        searchPanel.setLayout(null);
        contentPane.add(searchPanel);

        // 텍스트 필드
        JTextField search_plan = new JTextField();
        search_plan.setBounds(0, 0, 210, 23); // 왼쪽에 공간을 둠
        searchPanel.add(search_plan);

        // 아이콘 또는 특수문자 라벨
        JLabel searchIcon = new JLabel("🔍"); // 아이콘 대신 특수문자 사용
        searchIcon.setBounds(210, 0, 30, 22); // 텍스트 필드 오른쪽 위치
        searchIcon.setHorizontalAlignment(SwingConstants.CENTER);
        searchPanel.add(searchIcon);

        search_plan.addActionListener(e -> {
            String searchText = search_plan.getText();
            System.out.println("검색어: " + searchText);
            // 실제 검색 동작 구현
        });

        // 클릭 효과를 위한 마우스 리스너 추가
        searchIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 클릭 시 동작
                String searchText = search_plan.getText();
                System.out.println("검색어: " + searchText);
            }

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

        //1줄

        JButton btn_plan0 = new JButton();	//게시물1
        btn_plan0.setBounds(123, 230, 120, 120);
        JLabel plan0 = new JLabel("- plan_name");
        plan0.setBounds(143, 350, 100, 20);

        contentPane.add(btn_plan0);
        contentPane.add(plan0);

        JButton btn_plan1 = new JButton();	//게시물1
        btn_plan1.setBounds(323, 230, 120, 120);
        JLabel plan1 = new JLabel("- plan_name");
        plan1.setBounds(343, 350, 100, 20);

        contentPane.add(btn_plan1);
        contentPane.add(plan1);

        JButton btn_plan2 = new JButton();	//게시물2
        btn_plan2.setBounds(533, 230, 120, 120);
        JLabel plan2 = new JLabel("- plan_name");
        plan2.setBounds(563, 350, 100, 20);

        contentPane.add(btn_plan2);
        contentPane.add(plan2);

        JButton btn_plan3 = new JButton();	//게시물3
        btn_plan3.setBounds(743, 230, 120, 120);
        JLabel plan3 = new JLabel("- plan_name");
        plan3.setBounds(763, 350, 100, 20);

        contentPane.add(btn_plan3);
        contentPane.add(plan3);

        //2줄

        JButton btn_plan4 = new JButton();	//게시물4
        btn_plan4.setBounds(123, 400, 120, 120);
        JLabel plan4 = new JLabel("- plan_name");
        plan4.setBounds(143, 520, 100, 20);

        contentPane.add(btn_plan4);
        contentPane.add(plan4);

        JButton btn_plan5 = new JButton();	//게시물5
        btn_plan5.setBounds(323, 400, 120, 120);
        JLabel plan5 = new JLabel("- plan_name");
        plan5.setBounds(343, 520, 100, 20);

        contentPane.add(btn_plan5);
        contentPane.add(plan5);

        JButton btn_plan6 = new JButton();	//게시물6
        btn_plan6.setBounds(533, 400, 120, 120);
        JLabel plan6 = new JLabel("- plan_name");
        plan6.setBounds(563, 520, 100, 20);

        contentPane.add(btn_plan6);
        contentPane.add(plan6);

        JButton btn_plan7 = new JButton();	//게시물7
        btn_plan7.setBounds(743, 400, 120, 120);
        JLabel plan7 = new JLabel("- plan_name");
        plan7.setBounds(763, 520, 100, 20);

        contentPane.add(btn_plan7);
        contentPane.add(plan7);

        MyPanel panel1 = new MyPanel();
        panel1.setBounds(0, 0, 1000, 600);
        contentPane.add(panel1);


        setVisible(true);

    }
    class MyPanel extends JPanel{
        public void paintComponent(Graphics g){

            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.black);
            g2.drawLine(123, 85, 740, 85);
        }
    }

    public static void main(String[] args) {
        new SearchFrame();
    }
}
