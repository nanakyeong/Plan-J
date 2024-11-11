package com.example.planj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class UploadpageFrame extends JFrame {

    private Container contentPane;
    private JList<String> list;
    private JScrollPane sp;

    public UploadpageFrame() {
        setTitle("Plan J");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        contentPane = getContentPane();
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

        DefaultListModel<String> plan = new DefaultListModel<>();
        plan.addElement("(MY) 여수 여행 2박 3일 "+"- 2023.11.19");
        plan.addElement("(MY) 제주도 3박 4일 여행 "+"- 2023.08.21");
        plan.addElement("(MY) 세종 당일치기 "+"- 2023.02.08");
        plan.addElement("(MY) 군산 낭만 여행 1박 2일 \"+\"- 2022.12.26");
        plan.addElement("(MY) test");
        plan.addElement("(MY) test");
        plan.addElement("(MY) test");
        plan.addElement("(MY) test");
        plan.addElement("(MY) test");
        plan.addElement("(MY) test");
        plan.addElement("(MY) test");
        plan.addElement("(MY) test");
        plan.addElement("(MY) test");
        plan.addElement("(MY) test");
        plan.addElement("(MY) test");
        plan.addElement("(MY) test");
        plan.addElement("(MY) test");
        plan.addElement("(MY) test");

        JButton newPlanButton = new JButton("+ New Plan");
        newPlanButton.setBounds(123, 225, 150, 40); // 위치와 크기 설정
        newPlanButton.setFont(new Font("돋움", Font.BOLD, 17)); // 글씨 크기 설정
        newPlanButton.setBackground(new Color(255, 255, 255)); // 배경색 설정 (파란색 계열)
        contentPane.add(newPlanButton);

        newPlanButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new PlanwritepageFrame();
                dispose();
            });
        });


        list = new JList<>(plan);
        list.setVisibleRowCount(7); // 보여질 plan 개수
        sp = new JScrollPane(list);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setBounds(123, 270, 738, 200); // 크기와 위치 설정
        contentPane.add(sp);

        MyPanel panel1 = new MyPanel();
        panel1.setBounds(0, 0, 1000, 600);
        contentPane.add(panel1);

        setVisible(true);
    }

    class MyPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.black);
            g2.drawLine(123, 85, 740, 85);
        }
    }

    public static void main(String[] args) {
        new UploadpageFrame();
    }
}
