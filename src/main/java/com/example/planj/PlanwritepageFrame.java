package com.example.planj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;


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

        section1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = (Integer) section1.getSelectedItem();
                section2.removeAllItems();
                for (int i = selected; i <= selected + 1; i++) {
                    section2.addItem(i + 1);
                }
            }
        });

        JTextField plan_write = new JTextField();
        plan_write.setBounds(138, 280, 410, 230);
        contentPane.add(plan_write);

        section1.setSelectedIndex(0);

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
            g2.drawLine(123, 85, 866, 85);

            Graphics2D g3 = (Graphics2D) g;
            g3.setStroke(new BasicStroke(1));
            g2.setColor(Color.gray);
            g3.drawRect(123, 200, 743, 330); // 전체
            g2.drawRect(565, 220, 285, 290); // 지도
        }
    }

    public static void main(String[] args) {
        new PlanwritepageFrame();
    }
}
