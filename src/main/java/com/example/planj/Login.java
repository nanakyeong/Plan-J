package com.example.planj;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {
    public Login() {
        setTitle("Plan J");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000,600);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        JLabel logo = new JLabel("Plan J");
        logo.setBounds(460, 10, 100, 20);
        contentPane.add(logo);

        JButton check = new JButton("확인");
        check.setBounds(450,500,100,20);
        contentPane.add(check);

        JTextField id = new JTextField("아이디");
        id.setBounds(450,100,100,20);
        contentPane.add(id);
        id.setColumns(10);

        JTextField password = new JTextField("비밀번호");
        password.setBounds(450,130,100,20);
        contentPane.add(password);
        password.setColumns(10);

        setVisible(true);
    }

    public static void main(String[] args) {
        Login login = new Login();
    }
}
