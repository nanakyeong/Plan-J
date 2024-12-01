package com.example.planj.frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.awt.Font;

import com.example.planj.ApplicationContextProvider;
import com.example.planj.MainpageFrame;
import com.example.planj.UploadpageFrame;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class LoginFrame extends JFrame {
    private JLabel loginLabel;
    private BufferedImage backgroundImage;

    public class FontLoader {
        private static final Map<String, Font> fontRegistry = new HashMap<>();

        // 폰트를 로드하여 등록하는 메서드
        public static void loadCustomFont(String fontPath, String fontName) {
            try {
                File fontFile = new File(fontPath);
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont); // 시스템에 등록
                fontRegistry.put(fontName, customFont); // 폰트를 Map에 저장
                System.out.println("폰트 로드 성공: " + fontName + " (" + fontPath + ")");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("폰트 로드 실패: " + fontName + " (" + fontPath + ")");
            }
        }

        // 저장된 폰트를 가져오는 메서드
        public static Font getFont(String fontName, float size, int style) {
            Font font = fontRegistry.get(fontName);
            if (font != null) {
                return font.deriveFont(style, size);
            }
            System.out.println("등록되지 않은 폰트: " + fontName);
            return new Font("Default", style, Math.round(size)); // 대체 폰트 반환
        }
    }

    public LoginFrame() {

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        contentPane.setBackground(Color.WHITE);

        setTitle("PLAN J");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, getWidth(), getHeight());
        setContentPane(layeredPane);

        try {
            backgroundImage = ImageIO.read(new File("C:\\Users\\Owner\\Desktop\\workspace\\java\\Plan-J\\src\\main\\java\\com\\example\\planj\\img\\배경.png"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("배경 이미지를 로드할 수 없습니다.");
        }

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE); // 기본 배경색 설정
                g.fillRect(0, 0, getWidth(), getHeight()); // 흰색으로 전체 배경 채우기

                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        backgroundPanel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(backgroundPanel, Integer.valueOf(0)); // 배경 패널을 가장 아래로 추가

        // 콘텐츠 패널 생성
        JPanel contentPanel = new JPanel(null);
        contentPanel.setBounds(0, 0, getWidth(), getHeight());
        contentPanel.setOpaque(false); // 투명하게 설정
        layeredPane.add(contentPanel, Integer.valueOf(1));

        contentPanel.setBackground(Color.WHITE);

        JLabel logo1 = new JLabel("<html><span style='color:#89AEBF;'>P</span>lan<span style='color:#436698;'> J</span></html>");
        logo1.setFont(JoinFrame.FontLoader.getFont("낭만있구미체", 35f, Font.BOLD));
        contentPanel.add(logo1);
        logo1.setBounds(440, 120, 150, 40);

        JLabel logo2 = new JLabel("로그인");
        logo2.setFont(JoinFrame.FontLoader.getFont("낭만있구미체", 17f, Font.PLAIN));
        logo2.setBounds(460, 180, 180, 30);
        contentPanel.add(logo2);

        MyPanel line = new MyPanel();
        line.setBounds(370, 200, 220, 20);
        contentPanel.add(line);

        RoundTextField username = new RoundTextField("  아이디");
        username.setFont(JoinFrame.FontLoader.getFont("세종글꽃체", 15f, Font.PLAIN));
        username.setBounds(400, 250, 180, 20);
        contentPanel.add(username);

        PasswordField password = new PasswordField("  비밀번호");
        password.setFont(JoinFrame.FontLoader.getFont("세종글꽃체", 15f, Font.PLAIN));
        password.setBounds(400, 290, 180, 20);
        contentPanel.add(password);

        RoundButton check = new RoundButton("로그인");
        check.setFont(JoinFrame.FontLoader.getFont("세종글꽃체", 20f, Font.PLAIN));
        check.setBounds(430, 350, 120, 30);
        contentPanel.add(check);

        JLabel findPassword = link("비밀번호 변경", 500, 320, () -> openFindPassword());
        findPassword.setFont(JoinFrame.FontLoader.getFont("세종글꽃체", 15f, Font.PLAIN));
        contentPanel.add(findPassword);

        JLabel findUsername = link("아이디 찾기", 400, 320, () -> openFindUsername());
        findUsername.setFont(JoinFrame.FontLoader.getFont("세종글꽃체", 15f, Font.PLAIN));
        contentPanel.add(findUsername);

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

        JLabel myplan = new JLabel("myplan");
        myplan.setFont(JoinFrame.FontLoader.getFont("세종글꽃체", 18f, Font.PLAIN));
        myplan.setForeground(Color.BLACK);
        myplan.setBackground(Color.WHITE);
        myplan.setOpaque(true);
        myplan.setBounds(620, 47, 80, 30); // 크기와 위치 설정
        myplan.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스를 올리면 커서 변경

        myplan.setHorizontalAlignment(SwingConstants.CENTER); // 수평 중앙 정렬
        myplan.setVerticalAlignment(SwingConstants.CENTER);   // 수직 중앙 정렬

//        myplan.setBorder(new JoinFrame.RoundRectangleBorder(new Color(0, 0, 0, 0), 20, 20)); // 둥근 테두리 추가


        myplan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                myplan.setBackground(Color.LIGHT_GRAY); // 마우스 오버 시 배경색 변경
            }

            @Override
            public void mouseExited(MouseEvent e) {
                myplan.setBackground(Color.WHITE); // 마우스 나가면 배경색 원래대로
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                openMyPlan();
            }
        });

        JLabel login = new JLabel("로그인");
        login.setFont(JoinFrame.FontLoader.getFont("세종글꽃체", 18f, Font.PLAIN));
        login.setForeground(Color.BLACK);
        login.setBackground(Color.lightGray); // 초기 배경 색
        login.setOpaque(true); // 배경 색이 보이도록 설정
        login.setBounds(711, 47, 80, 30); // 크기와 위치 설정
        login.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스를 올리면 커서 변경

        login.setHorizontalAlignment(SwingConstants.CENTER); // 수평 중앙 정렬
        login.setVerticalAlignment(SwingConstants.CENTER);   // 수직 중앙 정렬


        JLabel join = new JLabel("회원가입");
        join.setFont(JoinFrame.FontLoader.getFont("세종글꽃체", 18f, Font.PLAIN));
        join.setForeground(Color.BLACK);
        join.setBackground(Color.WHITE);
        join.setOpaque(true); // 배경 색이 보이도록 설정
        join.setBounds(800, 47, 80, 30); // 크기와 위치 설정
        join.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스를 올리면 커서 변경

        join.setHorizontalAlignment(SwingConstants.CENTER);
        join.setVerticalAlignment(SwingConstants.CENTER);

        join.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                join.setBackground(Color.LIGHT_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                join.setBackground(Color.lightGray);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                openJoin();
            }
        });
        contentPanel.add(myplan);
        contentPanel.add(login);
        contentPanel.add(join);

        LoginFrame.MyPanel panel1 = new LoginFrame.MyPanel();
        panel1.setBounds(100, 60, 800, 50);
        contentPanel.add(panel1);

        setVisible(true);
    }

    private void openMyPlan() {
        SwingUtilities.invokeLater(() -> {
            MainpageFrame mainpageFrame = ApplicationContextProvider.getContext().getBean(MainpageFrame.class);
            mainpageFrame.setVisible(true);
            dispose();
        });
    }

    private void openFindUsername() {
        String email = JOptionPane.showInputDialog(this, "등록된 이메일을 입력하세요:", "아이디 찾기", JOptionPane.PLAIN_MESSAGE);
        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "이메일을 입력하세요.");
            UIManager.put("OptionPane.messageFont", JoinFrame.FontLoader.getFont("세종글꽃체", 14f, Font.BOLD));
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
            URL url = new URL("http://localhost:8080/api/users/login"); // API 엔드포인트
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

                // 사용자 이름 가져오기 (API 응답 처리)
                String loggedInUsername = "용강천사"; // 실제 API 응답에 따라 수정

                SwingUtilities.invokeLater(() -> {
                    MainpageFrame mainpageFrame = ApplicationContextProvider.getContext().getBean(MainpageFrame.class);
                    mainpageFrame.updateUsername(loggedInUsername); // 사용자 이름 업데이트
                    mainpageFrame.setVisible(true);
                    dispose(); // 로그인 프레임 닫기
                });
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

        SwingUtilities.invokeLater(() -> {
            // 폰트 로드
            JoinFrame.FontLoader.loadCustomFont(
                    "C:\\Users\\Owner\\Desktop\\workspace\\java\\Plan-J\\src\\main\\java\\com\\example\\planj\\font\\Gumi Romance.ttf",
                    "낭만있구미체"
            );
            JoinFrame.FontLoader.loadCustomFont(
                    "C:\\Users\\Owner\\Desktop\\workspace\\java\\Plan-J\\src\\main\\java\\com\\example\\planj\\font\\Rix X ladywatermelon OTF Regular.otf",
                    "Rix X Lady Watermelon"
            );
            JoinFrame.FontLoader.loadCustomFont(
                    "C:\\Users\\Owner\\Desktop\\workspace\\java\\Plan-J\\src\\main\\java\\com\\example\\planj\\font\\SejongGeulggot.otf",
                    "세종글꽃체"
            );

            UIManager.put("OptionPane.background", Color.WHITE); // OptionPane 자체 배경색
            UIManager.put("Panel.background", Color.WHITE);
            new LoginFrame();
        });
    }
}