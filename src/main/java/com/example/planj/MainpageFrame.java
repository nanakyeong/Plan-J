package com.example.planj;

import javax.swing.*;
import java.awt.*;

public class MainpageFrame extends JFrame {

    public MainpageFrame() {
        setTitle("Plan J");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        JLabel logo1 = new JLabel("Plan J");
        logo1.setFont(new Font("돋움", Font.BOLD, 35));
        logo1.setBounds(123, 120, 150, 30);
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

        //1줄

        JButton btn_newplan = new JButton("+");	//+ 버튼
        btn_newplan.setFont(new Font("돋움", Font.BOLD, 45));
        btn_newplan.setBounds(123, 230, 120, 120);
        JLabel newplan = new JLabel("plan 불러오기");
        newplan.setBounds(143, 350, 100, 20);

        contentPane.add(btn_newplan);
        contentPane.add(newplan);

        JButton btn_plan1 = new JButton();	//게시물1
        btn_plan1.setFont(new Font("돋움", Font.BOLD, 45));
        btn_plan1.setBounds(323, 230, 120, 120);
        JLabel plan1 = new JLabel("- plan_name");
        plan1.setBounds(343, 350, 100, 20);

        contentPane.add(btn_plan1);
        contentPane.add(plan1);

        JButton btn_plan2 = new JButton();	//게시물2
        btn_plan2.setFont(new Font("돋움", Font.BOLD, 45));
        btn_plan2.setBounds(533, 230, 120, 120);
        JLabel plan2 = new JLabel("- plan_name");
        plan2.setBounds(563, 350, 100, 20);

        contentPane.add(btn_plan2);
        contentPane.add(plan2);

        JButton btn_plan3 = new JButton();	//게시물3
        btn_plan3.setFont(new Font("돋움", Font.BOLD, 45));
        btn_plan3.setBounds(743, 230, 120, 120);
        JLabel plan3 = new JLabel("- plan_name");
        plan3.setBounds(763, 350, 100, 20);

        contentPane.add(btn_plan3);
        contentPane.add(plan3);

        //2줄

        JButton btn_plan4 = new JButton();	//게시물4
        btn_plan4.setFont(new Font("돋움", Font.BOLD, 45));
        btn_plan4.setBounds(123, 400, 120, 120);
        JLabel plan4 = new JLabel("- plan_name");
        plan4.setBounds(143, 520, 100, 20);

        contentPane.add(btn_plan4);
        contentPane.add(plan4);

        JButton btn_plan5 = new JButton();	//게시물5
        btn_plan5.setFont(new Font("돋움", Font.BOLD, 45));
        btn_plan5.setBounds(323, 400, 120, 120);
        JLabel plan5 = new JLabel("- plan_name");
        plan5.setBounds(343, 520, 100, 20);

        contentPane.add(btn_plan5);
        contentPane.add(plan5);

        JButton btn_plan6 = new JButton();	//게시물6
        btn_plan6.setFont(new Font("돋움", Font.BOLD, 45));
        btn_plan6.setBounds(533, 400, 120, 120);
        JLabel plan6 = new JLabel("- plan_name");
        plan6.setBounds(563, 520, 100, 20);

        contentPane.add(btn_plan6);
        contentPane.add(plan6);

        JButton btn_plan7 = new JButton();	//게시물7
        btn_plan7.setFont(new Font("돋움", Font.BOLD, 45));
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
        new MainpageFrame();
    }
}
