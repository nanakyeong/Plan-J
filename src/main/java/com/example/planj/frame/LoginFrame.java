package com.example.planj.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;




public class LoginFrame extends JFrame {
    private JLabel loginLabel;

    public LoginFrame() {
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

        RoundTextField username = new RoundTextField("아이디");
        username.setBounds(400, 250, 180, 20);
        contentPane.add(username);

        PasswordField password = new PasswordField("비밀번호");
        password.setBounds(400, 290, 180, 20);
        contentPane.add(password);

        RoundButton check = new RoundButton("로그인");
        check.setBounds(430, 350, 120, 30);
        contentPane.add(check);

        JLabel findPassword = link("비밀번호 변경", 500, 320, () -> openFindPassword());
        contentPane.add(findPassword);

        JLabel findUsername = link("아이디 찾기", 400, 320, () -> openFindUsername());
        contentPane.add(findUsername);

        loginLabel = link("로그인", 755, 55, () -> openJoin());
        contentPane.add(loginLabel);

        check.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 로그인 시도
                String user = username.getText();
                String pass = password.getText();
                login(user, pass);
            }
        });

        JLabel myplan = link("myplan", 700, 55, () -> openMyPlan());
        JLabel join = link("회원가입", 820, 55, () -> openJoin());

        contentPane.add(myplan);
        contentPane.add(join);

        MyPanel panel1 = new MyPanel();
        panel1.setBounds(100, 60, 800, 50);
        contentPane.add(panel1);

        setVisible(true);
    }

    private void openFindUsername() {
        String email = JOptionPane.showInputDialog(this, "등록된 이메일을 입력하세요:", "아이디 찾기", JOptionPane.PLAIN_MESSAGE);
        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "이메일을 입력하세요.");
            return;
        }

        // DB 연결 정보
        String url = "jdbc:mysql://localhost:3306/planj_db"; // 데이터베이스 이름으로 수정
        String username = "root"; // MySQL 사용자 이름
        String password = "0000"; // MySQL 비밀번호

        // SQL 쿼리
        String query = "SELECT username FROM site_user WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // SQL 파라미터 설정
            stmt.setString(1, email);

            // 쿼리 실행
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String foundUsername = rs.getString("username");
                    JOptionPane.showMessageDialog(this, "아이디는: " + foundUsername);
                } else {
                    JOptionPane.showMessageDialog(this, "등록된 이메일이 없습니다.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "데이터베이스 연결 중 오류가 발생했습니다.");
        }
    }

    private void openFindPassword() {
        JTextField usernameField = new JTextField();
        JTextField emailField = new JTextField();
        Object[] message = {
                "아이디:", usernameField,
                "이메일:", emailField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "비밀번호 변경", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();

            if (username.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "아이디와 이메일을 모두 입력하세요.");
                return;
            }

            // DB 연결 정보
            String url = "jdbc:mysql://localhost:3306/planj_db"; // 데이터베이스 이름
            String dbUsername = "root"; // MySQL 사용자 이름
            String dbPassword = "0000"; // MySQL 비밀번호

            // SQL 쿼리
            String query = "SELECT password FROM site_user WHERE username = ? AND email = ?";

            try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                // SQL 파라미터 설정
                stmt.setString(1, username);
                stmt.setString(2, email);

                // 쿼리 실행
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // 사용자 존재 시 비밀번호 재설정 안내
                        JOptionPane.showMessageDialog(this, "비밀번호를 새로 설정하세요!");

                        // 새 비밀번호 입력창 (비밀번호는 *로 표시)
                        JPasswordField newPasswordField = new JPasswordField();
                        JPasswordField confirmPasswordField = new JPasswordField();
                        Object[] passwordMessage = {
                                "새 비밀번호:", newPasswordField,
                                "새 비밀번호 확인:", confirmPasswordField
                        };

                        // 새 비밀번호를 올바르게 입력할 때까지 반복문 사용
                        boolean validPassword = false;
                        while (!validPassword) {
                            int passwordOption = JOptionPane.showConfirmDialog(this, passwordMessage, "새 비밀번호 설정", JOptionPane.OK_CANCEL_OPTION);
                            if (passwordOption == JOptionPane.CANCEL_OPTION) {
                                break; // 취소 버튼을 클릭하면 종료
                            }

                            String newPassword = new String(newPasswordField.getPassword()).trim();
                            String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

                            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                                JOptionPane.showMessageDialog(this, "새 비밀번호와 확인 비밀번호를 모두 입력하세요.");
                                return;
                            }

                            // 기존 비밀번호와 동일한지 확인
                            String foundPassword = rs.getString("password");
                            if (BCrypt.checkpw(newPassword, foundPassword)) {
                                JOptionPane.showMessageDialog(this, "기존 비밀번호와 일치합니다! 새 비밀번호를 다시 입력해주세요.");
                                continue; // 기존 비밀번호와 동일하면 새 비밀번호를 다시 입력하게 함
                            }

                            // 새 비밀번호가 확인 비밀번호와 일치하는지 체크
                            if (!newPassword.equals(confirmPassword)) {
                                JOptionPane.showMessageDialog(this, "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
                                continue; // 비밀번호가 일치하지 않으면 다시 입력
                            }

                            // 새 비밀번호를 bcrypt로 해시
                            String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12)); // cost factor를 12로 지정

                            // 비밀번호 업데이트 SQL 쿼리
                            String updateQuery = "UPDATE site_user SET password = ? WHERE username = ? AND email = ?";

                            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                                updateStmt.setString(1, hashedNewPassword);
                                updateStmt.setString(2, username);
                                updateStmt.setString(3, email);
                                int rowsUpdated = updateStmt.executeUpdate();

                                if (rowsUpdated > 0) {
                                    JOptionPane.showMessageDialog(this, "성공적으로 비밀번호가 변경되었습니다!");
                                    validPassword = true; // 비밀번호가 변경되면 종료
                                } else {
                                    JOptionPane.showMessageDialog(this, "비밀번호 변경 실패: 해당 사용자가 존재하지 않거나 정보가 일치하지 않습니다.");
                                }
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "아이디와 이메일이 일치하지 않습니다.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "데이터베이스 연결 중 오류가 발생했습니다.");
            }
        }
    }

    private void login(String username, String password) {
        try {
            URL url = new URL("http://localhost:8080/api/users/login");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);

            String jsonInputString = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                JOptionPane.showMessageDialog(this, "로그인 성공!");
                updateLoginLabel();
            } else {
                JOptionPane.showMessageDialog(this, "로그인 실패! 아이디나 비밀번호를 확인하세요.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "로그인 중 오류 발생!");
        }
    }

    private void updateLoginLabel() {
        loginLabel.setText("로그아웃");
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLabel.removeMouseListener(loginLabel.getMouseListeners()[0]);
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logout();
            }
        });
    }

    private void logout() {
        JOptionPane.showMessageDialog(this, "로그아웃 되었습니다.");
        loginLabel.setText("로그인");
        loginLabel.removeMouseListener(loginLabel.getMouseListeners()[0]);
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openJoin();
            }
        });
    }

    private void openMyPlan() {
    }

    private void openJoin() {
        dispose();
        SwingUtilities.invokeLater(() -> new JoinFrame().setVisible(true));
    }

    private JLabel link(String text, int x, int y, Runnable action) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("돋움", Font.PLAIN, 15));
        label.setBounds(x, y, 100, 20);
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
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}
