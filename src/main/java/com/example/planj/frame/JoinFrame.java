package com.example.planj.frame;

import com.example.planj.ApplicationContextProvider;
import com.example.planj.MainpageFrame;
import com.example.planj.UploadpageFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import javax.swing.border.AbstractBorder;
import java.awt.geom.RoundRectangle2D;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class JoinFrame extends JFrame {
    private BufferedImage backgroundImage;

    public class FontLoader {
        private static final Map<String, Font> fontRegistry = new HashMap<>();

        // 폰트를 로드하여 등록하는 메서드
        public static void loadCustomFont(InputStream fontStream, String fontName) {
            try {
                if (fontStream != null) {
                    Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    ge.registerFont(customFont); // 시스템에 등록
                    fontRegistry.put(fontName, customFont); // 폰트를 Map에 저장
                    System.out.println("폰트 로드 성공: " + fontName);
                } else {
                    throw new FileNotFoundException("폰트 파일을 찾을 수 없습니다: " + fontName);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("폰트 로드 실패: " + fontName);
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

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        contentPane.setBackground(Color.WHITE);

        setTitle("PLAN J");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, getWidth(), getHeight());
        setContentPane(layeredPane);

        // 배경 이미지 로드
        // 배경 이미지 로드
        try {
            // src/main/resources 기준으로 클래스패스에서 파일 로드
            InputStream is = getClass().getResourceAsStream("/배경.png");
            if (is != null) {
                backgroundImage = ImageIO.read(is);
            } else {
                throw new FileNotFoundException("이미지를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("배경 이미지를 로드할 수 없습니다.");
        }

        // 배경 이미지를 설정하는 커스텀 패널
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

        JLabel logoP = new JLabel("P");
        logoP.setFont(FontLoader.getFont("낭만있구미체", 35f, Font.BOLD));
        logoP.setForeground(new Color(0x89AEBF));
        logoP.setBounds(440, 120, 30, 40);
        contentPanel.add(logoP);

        JLabel logoJ = new JLabel("J");
        logoJ.setFont(FontLoader.getFont("낭만있구미체", 35f, Font.BOLD));
        logoJ.setForeground(new Color(0x436698));
        logoJ.setBounds(540, 120, 30, 40);
        contentPanel.add(logoJ);

        JLabel logoText = new JLabel("lan");
        logoText.setFont(FontLoader.getFont("낭만있구미체", 35f, Font.BOLD));
        logoText.setBounds(468, 120, 70, 40);
        contentPanel.add(logoText);

        JLabel logo2 = new JLabel("회원가입");
        logo2.setFont(FontLoader.getFont("낭만있구미체",17f,Font.PLAIN));
        logo2.setBounds(460, 180, 180, 40);
        contentPanel.add(logo2);

        MyPanel line = new MyPanel();
        line.setBounds(370, 200, 220, 20);
        contentPanel.add(line);

        RoundTextField phone = new RoundTextField("  000-0000-0000");
        phone.setFont(FontLoader.getFont("세종글꽃체",15f,Font.PLAIN));
        phone.setBounds(400, 250, 180, 20);
        contentPanel.add(phone);

        RoundTextField email = new RoundTextField("  email");
        email.setFont(FontLoader.getFont("세종글꽃체",15f,Font.PLAIN));
        email.setBounds(400, 280, 180, 20);
        contentPanel.add(email);

        RoundTextField username = new RoundTextField("  아이디");
        username.setFont(FontLoader.getFont("세종글꽃체",15f,Font.PLAIN));
        username.setBounds(400, 310, 180, 20);
        contentPanel.add(username);

        PasswordField password1 = new PasswordField("  비밀번호");
        password1.setFont(FontLoader.getFont("세종글꽃체",15f,Font.PLAIN));
        password1.setBounds(400, 340, 180, 20);
        contentPanel.add(password1);

        PasswordField password2 = new PasswordField("  비밀번호 확인");
        password2.setFont(FontLoader.getFont("세종글꽃체",15f,Font.PLAIN));
        password2.setBounds(400, 370, 180, 20);
        contentPanel.add(password2);


        RoundButton check = new RoundButton("회원가입");
        check.setFont(FontLoader.getFont("세종글꽃체",20f,Font.PLAIN));
        check.setBounds(430, 420, 120, 30);
        contentPanel.add(check);

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

                String phoneText = phone.getText().trim().equals("000-0000-0000") ? null : phone.getText().trim();
                String emailText = email.getText().trim().equals("email") ? null : email.getText().trim();
                String usernameText = username.getText().trim().equals("아이디") ? null : username.getText().trim();
                String passwordText1 = password1.getText().trim().equals("비밀번호") ? null : password1.getText().trim();
                String passwordText2 = password2.getText().trim().equals("비밀번호 확인") ? null : password2.getText().trim();

                if (phoneText == null) {
                    UIManager.put("OptionPane.messageFont", FontLoader.getFont("세종글꽃체", 14f, Font.BOLD));
                    JOptionPane.showMessageDialog(null, "전화번호는 필수 항목입니다.");
                    return;
                }
                if (emailText == null) {
                    JOptionPane.showMessageDialog(null, "이메일은 필수 항목입니다.");
                    UIManager.put("OptionPane.messageFont", FontLoader.getFont("세종글꽃체", 14f, Font.BOLD));
                    return;
                }
                if (usernameText == null) {
                    JOptionPane.showMessageDialog(null, "아이디는 필수 항목입니다.");
                    UIManager.put("OptionPane.messageFont", FontLoader.getFont("세종글꽃체", 14f, Font.BOLD));
                    return;
                }
                if (passwordText1 == null) {
                    JOptionPane.showMessageDialog(null, "비밀번호는 필수 항목입니다.");
                    UIManager.put("OptionPane.messageFont", FontLoader.getFont("세종글꽃체", 14f, Font.BOLD));
                    return;
                }
                if (passwordText2 == null) {
                    JOptionPane.showMessageDialog(null, "비밀번호 확인은 필수 항목입니다.");
                    UIManager.put("OptionPane.messageFont", FontLoader.getFont("세종글꽃체", 14f, Font.BOLD));
                    return;
                }
                if (!passwordText1.equals(passwordText2)) {
                    JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
                    UIManager.put("OptionPane.messageFont", FontLoader.getFont("세종글꽃체", 14f, Font.BOLD));
                    return;
                }

                String userData = String.format(
                        "{\"phone\":\"%s\", \"email\":\"%s\", \"username\":\"%s\", \"password\":\"%s\"}",
                        phoneText, emailText, usernameText, passwordText1
                );
                registerUser(userData);
            }
        });


        JLabel myplan = new JLabel("myplan");
        myplan.setFont(FontLoader.getFont("세종글꽃체", 18f, Font.PLAIN));
        myplan.setForeground(Color.BLACK);
        myplan.setBackground(Color.WHITE);
        myplan.setOpaque(true);
        myplan.setBounds(620, 47, 80, 30); // 크기와 위치 설정
        myplan.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스를 올리면 커서 변경

        myplan.setHorizontalAlignment(SwingConstants.CENTER); // 수평 중앙 정렬
        myplan.setVerticalAlignment(SwingConstants.CENTER);   // 수직 중앙 정렬

        myplan.setBorder(new RoundRectangleBorder(new Color(0, 0, 0, 0), 20, 20)); // 둥근 테두리 추가


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
        login.setFont(FontLoader.getFont("세종글꽃체", 18f, Font.PLAIN));
        login.setForeground(Color.BLACK);
        login.setBackground(Color.WHITE); // 초기 배경 색
        login.setOpaque(true); // 배경 색이 보이도록 설정
        login.setBounds(711, 47, 80, 30); // 크기와 위치 설정
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
        join.setFont(FontLoader.getFont("세종글꽃체", 18f, Font.PLAIN));
        join.setForeground(Color.BLACK);
        join.setBackground(Color.LIGHT_GRAY); // 초기 배경 색은 라이트 그레이
        join.setOpaque(true); // 배경 색이 보이도록 설정
        join.setBounds(800, 47, 80, 30); // 크기와 위치 설정
        join.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스를 올리면 커서 변경

        join.setHorizontalAlignment(SwingConstants.CENTER); // 수평 중앙 정렬
        join.setVerticalAlignment(SwingConstants.CENTER);   // 수직 중앙 정렬

        join.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 회원가입 클릭 시 아무 동작 안 함 (빈 액션)
            }
        });
        contentPanel.add(myplan);
        contentPanel.add(login);
        contentPanel.add(join);

        MyPanel panel1 = new MyPanel();
        panel1.setBounds(100, 60, 800, 50);
        contentPanel.add(panel1);

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
        SwingUtilities.invokeLater(() -> {
            UploadpageFrame uploadFrame = ApplicationContextProvider.getContext().getBean(UploadpageFrame.class);
            uploadFrame.setVisible(true);
            dispose();
        });
    }

    private void openLogin() {
        SwingUtilities.invokeLater(() -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    private JLabel link(String text, int x, int y, Runnable action) {
        JLabel label = new JLabel(text);
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
        // 클래스패스를 통해 폰트 로드
        FontLoader.loadCustomFont(
                JoinFrame.class.getResourceAsStream("/Gumi_Romance.otf"),
                "낭만있구미체"
        );
        FontLoader.loadCustomFont(
                JoinFrame.class.getResourceAsStream("/SejongGeulggot.otf"),
                "세종글꽃체"
        );
        // 그 후에 JoinFrame을 띄운다
        UIManager.put("OptionPane.background", Color.WHITE); // OptionPane 자체 배경색
        UIManager.put("Panel.background", Color.WHITE);
        SwingUtilities.invokeLater(() -> new JoinFrame());
    }
}


