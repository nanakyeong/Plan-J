package com.example.planj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Plan J");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        JLabel logo1 = new JLabel("Plan J");
        logo1.setFont(new Font("돋움", Font.BOLD, 35));
        logo1.setBounds(220, 120, 150, 30);
        contentPane.add(logo1);

        JLabel myplan = new JLabel("myplan");
        myplan.setBounds(710, 55, 100, 20);

        JLabel login = new JLabel("로그인");
        login.setBounds(762, 55, 100, 20);

        JLabel join = new JLabel("회원가입");
        join.setBounds(814, 55, 100, 20);


        contentPane.add(myplan);
        contentPane.add(login);
        contentPane.add(join);




        setVisible(true);

    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
