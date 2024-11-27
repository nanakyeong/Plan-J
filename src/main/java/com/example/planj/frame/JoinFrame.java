package com.example.planj.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import javax.swing.border.AbstractBorder;
import java.awt.geom.RoundRectangle2D;

public class JoinFrame extends JFrame {

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


    public JoinFrame() {

        setBackground(Color.white);

        setTitle("PLAN J");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        contentPane.setBackground(Color.WHITE);

        JLabel logo1 = new JLabel("<html><span style='color:#89AEBF;'>P</span>lan<span style='color:#436698;'> J</span></html>");
        logo1.setFont(FontLoader.getFont("낭만있구미체",35f, Font.BOLD));
        contentPane.add(logo1);
        logo1.setBounds(440, 120, 150, 40);

        JLabel logo2 = new JLabel("회원가입");
        logo2.setFont(FontLoader.getFont("낭만있구미체",17f,Font.PLAIN));
        logo2.setBounds(460, 180, 180, 40);
        contentPane.add(logo2);

        MyPanel line = new MyPanel();
        line.setBounds(370, 200, 220, 20);
        contentPane.add(line);

        RoundTextField phone = new RoundTextField("  000-0000-0000");
        phone.setFont(FontLoader.getFont("Rix X Lady Watermelon",15f,Font.PLAIN));
        phone.setBounds(400, 250, 180, 20);
        contentPane.add(phone);

        RoundTextField email = new RoundTextField("  email");
        email.setFont(FontLoader.getFont("Rix X Lady Watermelon",15f,Font.PLAIN));
        email.setBounds(400, 280, 180, 20);
        contentPane.add(email);

        RoundTextField username = new RoundTextField("  아이디");
        username.setFont(FontLoader.getFont("Rix X Lady Watermelon",15f,Font.PLAIN));
        username.setBounds(400, 310, 180, 20);
        contentPane.add(username);

        PasswordField password1 = new PasswordField("  비밀번호");
        password1.setFont(FontLoader.getFont("Rix X Lady Watermelon",15f,Font.PLAIN));
        password1.setBounds(400, 340, 180, 20);
        contentPane.add(password1);

        PasswordField password2 = new PasswordField("  비밀번호 확인");
        password2.setFont(FontLoader.getFont("Rix X Lady Watermelon",15f,Font.PLAIN));
        password2.setBounds(400, 370, 180, 20);
        contentPane.add(password2);


        RoundButton check = new RoundButton("회원가입");
        check.setFont(FontLoader.getFont("Rix X Lady Watermelon",20f,Font.PLAIN));
        check.setBounds(430, 420, 120, 30);
        contentPane.add(check);

        phone.setMargin(new Insets(5, 10, 5, 10)); // 위, 왼쪽, 아래, 오른쪽 여백 설정
        email.setMargin(new Insets(5, 10, 5, 10));
        username.setMargin(new Insets(5, 10, 5, 10));
        password1.setMargin(new Insets(5, 10, 5, 10));
        password2.setMargin(new Insets(5, 10, 5, 10));


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


        JLabel myplan = new JLabel("myplan");
        myplan.setFont(FontLoader.getFont("Rix X Lady Watermelon", 18f, Font.PLAIN));
        myplan.setForeground(Color.BLACK);
        myplan.setBackground(Color.WHITE); // 초기 배경 색
        myplan.setOpaque(true); // 배경 색이 보이도록 설정
        myplan.setBounds(620, 50, 80, 30); // 크기와 위치 설정
        myplan.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스를 올리면 커서 변경

        myplan.setHorizontalAlignment(SwingConstants.CENTER); // 수평 중앙 정렬
        myplan.setVerticalAlignment(SwingConstants.CENTER);   // 수직 중앙 정렬

        myplan.setBorder(new RoundRectangleBorder(new Color(0, 0, 0, 0), 2, 60)); // 둥근 테두리 추가


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
                openMyPlan(); // 클릭 시 실행할 액션
            }
        });

        JLabel login = new JLabel("로그인");
        login.setFont(FontLoader.getFont("Rix X Lady Watermelon", 18f, Font.PLAIN));
        login.setForeground(Color.BLACK);
        login.setBackground(Color.WHITE); // 초기 배경 색
        login.setOpaque(true); // 배경 색이 보이도록 설정
        login.setBounds(711, 50, 80, 30); // 크기와 위치 설정
        login.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스를 올리면 커서 변경

        login.setHorizontalAlignment(SwingConstants.CENTER); // 수평 중앙 정렬
        login.setVerticalAlignment(SwingConstants.CENTER);   // 수직 중앙 정렬


        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                login.setBackground(Color.LIGHT_GRAY); // 마우스 오버 시 배경색 변경
            }

            @Override
            public void mouseExited(MouseEvent e) {
                login.setBackground(Color.WHITE); // 마우스 나가면 배경색 원래대로
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                openLogin(); // 클릭 시 실행할 액션
            }
        });

        JLabel join = new JLabel("회원가입");
        join.setFont(FontLoader.getFont("Rix X Lady Watermelon", 18f, Font.PLAIN));
        join.setForeground(Color.BLACK);
        join.setBackground(Color.LIGHT_GRAY); // 초기 배경 색은 라이트 그레이
        join.setOpaque(true); // 배경 색이 보이도록 설정
        join.setBounds(800, 50, 80, 30); // 크기와 위치 설정
        join.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스를 올리면 커서 변경

        join.setHorizontalAlignment(SwingConstants.CENTER); // 수평 중앙 정렬
        join.setVerticalAlignment(SwingConstants.CENTER);   // 수직 중앙 정렬

        join.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 회원가입 클릭 시 아무 동작 안 함 (빈 액션)
            }
        });
        contentPane.add(myplan);
        contentPane.add(login);
        contentPane.add(join);

        MyPanel panel1 = new MyPanel();
        panel1.setBounds(100, 60, 800, 50);
        contentPane.add(panel1);

        setVisible(true);
    }

    class RoundRectangleBorder extends AbstractBorder {
        private final Color color;
        private final int thickness;
        private final int arcWidth;

        public RoundRectangleBorder(Color color, int thickness, int arcWidth) {
            this.color = color;
            this.thickness = thickness;
            this.arcWidth = arcWidth;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, width - 1, height - 1, arcWidth, arcWidth); // 둥근 사각형 그리기
        }
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
        label.setFont(FontLoader.getFont("Rix X Lady Watermelon", 18f, Font.PLAIN));

        // 텍스트 길이에 따라 너비를 자동으로 계산
        FontMetrics metrics = label.getFontMetrics(label.getFont());
        int textWidth = metrics.stringWidth(text); // 텍스트의 너비 계산
        label.setBounds(x, y, textWidth + 20, 30); // 텍스트 길이에 여유값 추가

        label.setForeground(Color.BLACK);
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

        public MyPanel() {
            setOpaque(true); // 패널을 불투명하게 설정
            setBackground(Color.WHITE); // 배경색을 명시적으로 설정
        }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(4));
            g2.drawLine(20, 20, 780, 20);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 폰트 로드
            FontLoader.loadCustomFont(
                    "C:\\Users\\Owner\\Desktop\\workspace\\java\\Plan-J\\src\\main\\java\\com\\example\\planj\\font\\Gumi Romance.ttf",
                    "낭만있구미체"
            );
            FontLoader.loadCustomFont(
                    "C:\\Users\\Owner\\Desktop\\workspace\\java\\Plan-J\\src\\main\\java\\com\\example\\planj\\font\\Rix X ladywatermelon OTF Regular.otf",
                    "Rix X Lady Watermelon"
            );

            // JoinFrame 실행
            new JoinFrame();
        });
    }

}


