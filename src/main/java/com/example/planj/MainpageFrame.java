package com.example.planj;

import com.example.planj.db.PlanDTO;
import com.example.planj.db.PlanService;
import com.example.planj.frame.JoinFrame;
import com.example.planj.frame.LoginFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MainpageFrame extends JFrame {
    private final PlanService planService;
    private final JButton[] planButtons = new JButton[7];
    private JLabel usernameLabel; // 사용자 이름을 표시할 라벨

    @Autowired private UploadpageFrame uploadFrame;

    private JTextField search_plan; // 검색 텍스트 필드

    @Autowired
    public MainpageFrame(PlanService planService) {
        this.planService = planService;
        initialize();

    }

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

    private void initialize() {

        setTitle("Plan J");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.setBackground(Color.WHITE);

        usernameLabel = new JLabel("로그인 해주세요.");
        usernameLabel.setFont(new Font("돋움", Font.BOLD, 14));
        usernameLabel.setBounds(870, 55, 100, 20);
        usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        usernameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openLoginPage();
            }
        });
        getContentPane().add(usernameLabel);

        JLabel logo1 = new JLabel("Plan J");
        logo1.setFont(new Font("돋움", Font.BOLD, 35));
        JLabel logo1 = new JLabel("<html><span style='color:#89AEBF;'>P</span>lan<span style='color:#436698;'> J</span></html>");
        logo1.setFont(JoinFrame.FontLoader.getFont("낭만있구미체",35f, Font.BOLD));
        logo1.setBounds(123, 135, 150, 30);
        contentPane.add(logo1);

//        JLabel myplan = new JLabel("myplan");
//        myplan.setBounds(700, 55, 100, 20);
//        JLabel login = new JLabel("로그인");
//        login.setBounds(762, 55, 100, 20);
//        login.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                openLoginPage(); // LoginFrame으로 이동
//            }
//        });
//        contentPane.add(login);
//
//        JLabel join = new JLabel("회원가입");
//        join.setBounds(814, 55, 100, 20);
//        join.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                openJoinPage();
//            }
//        });
//
//        contentPane.add(myplan);
//        contentPane.add(login);
//        contentPane.add(join);

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
                openLoginPage();
            }
        });

        JLabel login = new JLabel("로그인");
        login.setFont(JoinFrame.FontLoader.getFont("세종글꽃체", 18f, Font.PLAIN));
        login.setForeground(Color.BLACK);
        login.setBackground(Color.WHITE);
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
                openLoginPage();
            }
        });

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
                join.setBackground(Color.WHITE);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                openJoinPage();
            }
        });
        contentPane.add(myplan);
        contentPane.add(login);
        contentPane.add(join);

        // 검색 패널
        JPanel searchPanel = new JPanel();
        searchPanel.setBounds(500, 142, 380, 23);
        searchPanel.setLayout(null);
        contentPane.add(searchPanel);

        // 라디오 버튼 그룹 생성
        ButtonGroup radioGroup = new ButtonGroup();
        JRadioButton regionRadioButton = new JRadioButton("지역");
        regionRadioButton.setFont(JoinFrame.FontLoader.getFont("세종글꽃체",15f,Font.PLAIN));
        regionRadioButton.setBounds(0, 0, 60, 23);
        regionRadioButton.setSelected(true); // 기본 선택
        regionRadioButton.setBackground(Color.WHITE);
        searchPanel.add(regionRadioButton);

        JRadioButton placeRadioButton = new JRadioButton("장소");
        placeRadioButton.setFont(JoinFrame.FontLoader.getFont("세종글꽃체",15f,Font.PLAIN));
        placeRadioButton.setBounds(60, 0, 60, 23);
        placeRadioButton.setBackground(Color.WHITE);
        searchPanel.add(placeRadioButton);

        // 라디오 버튼 그룹에 추가
        radioGroup.add(regionRadioButton);
        radioGroup.add(placeRadioButton);

        // 검색 텍스트 필드
        search_plan = new RoundTextField("");
        search_plan.setBounds(120, 0, 210, 23);
        searchPanel.add(search_plan);
        searchPanel.setBackground(Color.WHITE);

        // 검색 아이콘
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setBackground(Color.WHITE);
        searchIcon.setBounds(330, 0, 30, 22);
        searchIcon.setHorizontalAlignment(SwingConstants.CENTER);
        searchPanel.add(searchIcon);

        // 검색 결과 라벨
        JLabel searchResultLabel = new JLabel(); // 검색 결과 라벨 초기화
        searchResultLabel.setFont(JoinFrame.FontLoader.getFont("세종글꽃체",15f,Font.PLAIN));
        searchResultLabel.setBounds(123, 190, 500, 30); // 위치 지정
        contentPane.add(searchResultLabel);

            // 검색 아이콘 동작
        searchIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String searchText = search_plan.getText().trim(); // 입력된 검색어 가져오기
                if (searchText.isEmpty()) {
                    JOptionPane.showMessageDialog(contentPane, "검색어를 입력하세요!", "오류", JOptionPane.WARNING_MESSAGE);
                } else {
                    // 라벨에 검색 결과 업데이트
                    searchResultLabel.setText("\"" + searchText + "\"에 대한 검색 결과입니다.");
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                searchIcon.setOpaque(true);
                searchIcon.setBackground(Color.LIGHT_GRAY);
                searchIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                searchIcon.setOpaque(false);
                searchIcon.setBackground(null);
            }
        });

        RoundButton btn_newplan = new RoundButton("+");
        btn_newplan.setBounds(123, 230, 120, 120);
        JLabel newplan = new JLabel("plan 업로드");
        newplan.setFont(JoinFrame.FontLoader.getFont("세종글꽃체",15f,Font.PLAIN));
        newplan.setBounds(143, 350, 100, 20);
        btn_newplan.setBackground(Color.lightGray);
        btn_newplan.setForeground(Color.BLACK);
        contentPane.add(btn_newplan);
        contentPane.add(newplan);

        btn_newplan.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                //UploadpageFrame uploadFrame = ApplicationContextProvider.getContext().getBean(UploadpageFrame.class);
                this.uploadFrame.setVisible(true);
                dispose();
            });
        });

        contentPane.add(btn_newplan);

        createPlanButtons(contentPane);
        updatePlanButtons();

        MyPanel panel1 = new MyPanel();
        panel1.setBounds(0, 0, 1000, 600);
        contentPane.add(panel1);

        setVisible(true);
    }

    public void updateUsername(String username) {
        if (username == null || username.isEmpty()) {
            usernameLabel.setText("로그인 해주세요.");
            usernameLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            usernameLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    openLoginPage();
                }
            });
        } else {
            usernameLabel.setText(username + "님");
            usernameLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            for (MouseListener listener : usernameLabel.getMouseListeners()) {
                usernameLabel.removeMouseListener(listener);
            }
        }
    }


    private void openLoginPage() {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = ApplicationContextProvider.getContext().getBean(LoginFrame.class);
            loginFrame.setVisible(true);
            dispose();
        });
    }

    private void openJoinPage() {
        SwingUtilities.invokeLater(() -> {
            JoinFrame joinFrame = ApplicationContextProvider.getContext().getBean(JoinFrame.class);
            joinFrame.setVisible(true);
            dispose();
        });
    }

    private void createPlanButtons(Container contentPane) {
        for (int i = 0; i < planButtons.length; i++) {
            JButton planButton = new JButton();
            planButton.setBounds(123 + (((i+1) % 4) * 210), 230 + (((i+1) / 4) * 170), 120, 120);
            planButton.setVisible(false);
            contentPane.add(planButton);
            planButtons[i] = planButton;
        }
    }

    private void updatePlanButtons() {
        List<PlanDTO> plans = planService.getIsRegisteredTrue();

        // 반복문에서 유효한 버튼 수를 초과하지 않도록 설정
        int maxButtons = Math.min(plans.size(), planButtons.length);
        for (int i = 0; i < maxButtons; i++) {
            PlanDTO plan = plans.get(i);
            JButton planButton = planButtons[i];
            planButton.setText(plan.getTitle());
            planButton.setVisible(true);

            // 버튼 클릭 시 계획 열기
            planButton.addActionListener(e -> openPlan(plan));
        }

        // 남아 있는 버튼은 숨기기
        for (int i = plans.size(); i < planButtons.length; i++) {
            planButtons[i].setVisible(false);
        }
    }

    private void openPlan(PlanDTO planDTO) {
        SwingUtilities.invokeLater(() -> {
            setBackground(Color.WHITE);
            PlanwritepageFrame planFrame = ApplicationContextProvider.getContext().getBean(PlanwritepageFrame.class);
            planFrame.disableEditing(); // UI 비활성화
            planFrame.setPlanDTO(planDTO);
            planFrame.setVisible(true);
            dispose();
        });
    }

    class MyPanel extends JPanel {
        public MyPanel() {
            setOpaque(true); // 패널을 불투명하게 설정
            setBackground(Color.WHITE); // 배경색을 명시적으로 설정
        }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.black);
            g2.drawLine(123, 85, 740, 85);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UIManager.put("OptionPane.background", Color.WHITE); // OptionPane 자체 배경색
            UIManager.put("Panel.background", Color.WHITE);
            ApplicationContextProvider.getContext().getBean(MainpageFrame.class).setVisible(true);

        });
    }
}

