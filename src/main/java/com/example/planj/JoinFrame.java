package com.example.planj;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JoinFrame extends JFrame {

    public JoinFrame() {
        setTitle("Plan J");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        JLabel logo1 = new JLabel("Plan J");
        logo1.setFont(new Font("돋움", Font.BOLD, 35));
        logo1.setBounds(440, 120, 150, 30);
        contentPane.add(logo1);

        JLabel logo2 = new JLabel("회원가입");
        logo2.setFont(new Font("돋움", Font.BOLD, 15));
        logo2.setBounds(460, 180, 180, 30);
        contentPane.add(logo2);

        MyPanel line = new MyPanel();
        line.setBounds(370, 200, 220, 20);
        contentPane.add(line);

        RoundTextField phone = new RoundTextField("000-0000-0000");
        phone.setBounds(400, 250, 180, 20);
        contentPane.add(phone);

        RoundTextField email = new RoundTextField("email");
        email.setBounds(400, 280, 180, 20);
        contentPane.add(email);

        RoundTextField username = new RoundTextField("아이디");
        username.setBounds(400, 310, 180, 20);
        contentPane.add(username);

        PasswordField password1 = new PasswordField("비밀번호");
        password1.setBounds(400, 340, 180, 20);
        contentPane.add(password1);

        PasswordField password2 = new PasswordField("비밀번호 확인");
        password2.setBounds(400, 370, 180, 20);
        contentPane.add(password2);


        RoundButton check = new RoundButton("회원가입");
        check.setBounds(430, 420, 120, 30);
        contentPane.add(check);

        check.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String userData = String.format(
                        "{\"phone\":\"%s\", \"email\":\"%s\", \"username\":\"%s\", \"password1\":\"%s\", \"password2\":\"%s\"  }",
                        phone.getText(), email.getText(), username.getText(), password1.getText(), password2.getText()
                );
                registerUser(userData);
            }
        });

        check.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                if (phone.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "전화번호는 필수 항목입니다.");
                    return;
                }

                if (email.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "이메일은 필수 항목입니다.");
                    return;
                }

                if (username.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "아이디는 필수 항목입니다.");
                    return;
                }

                if (password1.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "비밀번호는 필수 항목입니다.");
                    return;
                }

                if (password2.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "비밀번호 확인은 필수 항목입니다.");
                    return;
                }
                
                if (!password1.getText().equals(password2.getText())) {
                    JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
                    return;
                }

                String userData = String.format(
                        "{\"phone\":\"%s\", \"email\":\"%s\", \"username\":\"%s\", \"password\":\"%s\"}",
                        phone.getText(), email.getText(), username.getText(), password1.getText()
                );
                registerUser(userData);
            }
        });


        JLabel myplan = link("myplan", 710, 55, () -> openMyPlan());
        JLabel login = link("로그인", 762, 55, () -> openLogin());

        contentPane.add(myplan);
        contentPane.add(login);

        MyPanel panel1 = new MyPanel();
        panel1.setBounds(100, 60, 800, 50);
        contentPane.add(panel1);

        setVisible(true);
    }


    private void registerUser(String userData) {
        try {
            URL url = new URL("http://localhost:8080/api/users/register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = userData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 응답 확인
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("회원가입 성공: " + response.toString());
                    SwingUtilities.invokeLater(() -> {
                        dispose();
                        new LoginFrame().setVisible(true);
                    });
                }
            } else {
                System.out.println("회원가입 실패: " + responseCode);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void openMyPlan() {

    }

    private void openLogin() {
        SwingUtilities.invokeLater(() -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    private JLabel link(String text, int x, int y, Runnable action) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("돋움", Font.PLAIN, 15));
        label.setBounds(x, y, 50, 20);
        label.setForeground(Color.black);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });
        return label;
    }

    class MyPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(4));
            g2.drawLine(20, 20, 780, 20);

            g2.setFont(new Font("돋움", Font.PLAIN, 15));
            g2.drawString("회원가입", 720, 10);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JoinFrame());
    }
}
