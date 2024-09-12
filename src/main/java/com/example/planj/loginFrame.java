package com.example.planj;

import javax.swing.*;
import java.awt.*;

public class loginFrame extends JFrame {
    public loginFrame() {
        setTitle("Plan J");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        JLabel logo1 = new JLabel("Plan J");
        logo1.setFont(new Font("돋움", Font.BOLD, 35));
        logo1.setBounds(440, 120, 150, 30);
        contentPane.add(logo1);

        JLabel logo2 = new JLabel("로그인");
        logo2.setFont(new Font("돋움", Font.BOLD, 15));
        logo2.setBounds(460, 180, 180, 30);
        contentPane.add(logo2);

        MyPanel line = new MyPanel();
        line.setBounds(370, 200, 220, 20);
        contentPane.add(line);

        RoundTextField id = new RoundTextField("아이디");
        id.setBounds(400, 250, 180, 20);
        contentPane.add(id);

        RoundTextField password = new RoundTextField("비밀번호");
        password.setBounds(400, 290, 180, 20);
        contentPane.add(password);

        RoundButton check = new RoundButton("확인");
        check.setBounds(430, 350, 120, 30);
        contentPane.add(check);

        MyPanel panel1 = new MyPanel();
        panel1.setBounds(100, 60, 800, 50);
        contentPane.add(panel1);

        setVisible(true);
    }

    class MyPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(4));
            g2.drawLine(20, 20, 780, 20);

            g2.setFont(new Font("돋움", Font.PLAIN, 16));
            g2.drawString("myplan", 600, 15);
            g2.drawString("로그인", 660, 15);
            g2.drawString("회원가입", 720, 15);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new loginFrame());
    }
}
