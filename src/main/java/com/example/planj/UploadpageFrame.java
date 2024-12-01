package com.example.planj;

import com.example.planj.db.PlanDTO;
import com.example.planj.db.PlanService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import com.example.planj.frame.JoinFrame;
import com.example.planj.frame.LoginFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UploadpageFrame extends JFrame {

    private final PlanService planService;
    private Container contentPane;
    private JList<String> list;
    private JScrollPane sp;
    private MyPanel panel1;

    @Autowired
    public UploadpageFrame(PlanService planService) {
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

        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(Color.WHITE);

        JLabel logo1 = new JLabel("<html><span style='color:#89AEBF;'>P</span>lan<span style='color:#436698;'> J</span></html>");
        logo1.setFont(JoinFrame.FontLoader.getFont("낭만있구미체", 35f, Font.BOLD));
        logo1.setBounds(123, 135, 150, 30);
        contentPane.add(logo1);

//        JLabel myplan = new JLabel("myplan");
//        myplan.setBounds(700, 55, 100, 20);
//        JLabel login = new JLabel("로그아웃");
//        login.setBounds(762, 55, 100, 20);
//        login.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                openLoginPage();
//            }
//        });
//        JLabel join = new JLabel("회원가입");
//        join.setBounds(814, 55, 100, 20);
//        join.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                openJoinPage();
//            }
//        });
//        contentPane.add(myplan);
//        contentPane.add(login);
//        contentPane.add(join);
        JLabel name = new JLabel("용강천사님");
        name.setFont(new Font("돋움", Font.BOLD, 14));
        name.setBounds(870,55,100,20);
        name.setHorizontalAlignment(SwingConstants.RIGHT);
        name.setForeground(Color.BLACK);
        name.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(name);

        JLabel myplan = new JLabel("myplan");
        myplan.setFont(JoinFrame.FontLoader.getFont("세종글꽃체", 18f, Font.PLAIN));
        myplan.setForeground(Color.BLACK);
        myplan.setBackground(Color.LIGHT_GRAY);
        myplan.setOpaque(true);
        myplan.setBounds(620, 47, 80, 30); // 크기와 위치 설정
        myplan.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스를 올리면 커서 변경

        myplan.setHorizontalAlignment(SwingConstants.CENTER); // 수평 중앙 정렬
        myplan.setVerticalAlignment(SwingConstants.CENTER);   // 수직 중앙 정렬

//        myplan.setBorder(new JoinFrame.RoundRectangleBorder(new Color(0, 0, 0, 0), 20, 20)); // 둥근 테두리 추가


        JLabel login = new JLabel("로그인");
        login.setFont(JoinFrame.FontLoader.getFont("세종글꽃체", 18f, Font.PLAIN));
        login.setForeground(Color.BLACK);
        login.setBackground(Color.WHITE);
        login.setOpaque(true); // 배경 색이 보이도록 설정
        login.setBounds(711, 47, 80, 30); // 크기와 위치 설정
        login.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스를 올리면 커서 변경

        login.setHorizontalAlignment(SwingConstants.CENTER); // 수평 중앙 정렬
        login.setVerticalAlignment(SwingConstants.CENTER);   // 수직 중앙 정렬

        myplan.setBounds(700, 55, 100, 20);
        myplan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openMyplanPage();
            }
        });
        JLabel logout = new JLabel("로그아웃");
        logout.setBounds(762, 55, 100, 20);
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


        List<String> plans = getPlanTitlesFromDatabase();

        DefaultListModel<String> planListModel = new DefaultListModel<>();
        plans.forEach(planListModel::addElement);

        JButton newPlanButton = new JButton("+ New Plan");
        newPlanButton.setBounds(123, 225, 150, 40); // 위치와 크기 설정
        newPlanButton.setFont(new Font("돋움", Font.BOLD, 17)); // 글씨 크기 설정
        newPlanButton.setBackground(new Color(255, 255, 255)); // 배경색 설정
        contentPane.add(newPlanButton);

        newPlanButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                PlanwritepageFrame planFrame = new PlanwritepageFrame(planService);
                planFrame.setVisible(true);
                dispose();
            });
        });

        list = new JList<>(planListModel);
        list.setVisibleRowCount(7); // 보여질 plan 개수
        sp = new JScrollPane(list);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setBounds(123, 270, 738, 200); // 크기와 위치 설정
        contentPane.add(sp);

        panel1 = new MyPanel();
        panel1.setBounds(0, 0, 1000, 600);
        contentPane.add(panel1);

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // 더블 클릭 이벤트
                    int selectedIndex = list.getSelectedIndex();
                    if (selectedIndex != -1) {
                        PlanService planService = ApplicationContextProvider.getContext().getBean(PlanService.class);
                        List<PlanDTO> plans = planService.getAllPlans();
                        PlanDTO selectedPlan = plans.get(selectedIndex);

                        SwingUtilities.invokeLater(() -> {
                            PlanwritepageFrame frame = new PlanwritepageFrame(planService);
                            frame.disableEditing(); // UI 비활성화

                            // PlacePerDay에서 숙소 정보 추출
                            String accommodation = selectedPlan.getPlacesPerDay().entrySet().stream()
                                    .flatMap(entry -> entry.getValue().stream())
                                    .filter(place -> place.startsWith("[숙소]"))
                                    .findFirst()
                                    .map(place -> place.replace("[숙소] ", ""))
                                    .orElse(null);

                            frame.setPlanDTO(selectedPlan);
                            frame.setCurrentPlan(selectedPlan.toPlan());
                            frame.setVisible(true);
                            dispose();
                        });
                    }
                }
            }
        });
        //setVisible(true);
    }

    private void openMyplanPage() {
        SwingUtilities.invokeLater(() -> {
            // Spring 컨텍스트에서 LoginFrame 가져오기
            MainpageFrame mainpageFrame = ApplicationContextProvider.getContext().getBean(MainpageFrame.class);
            mainpageFrame.setVisible(true);
            dispose(); // 현재 프레임 닫기
        });
    }

    private void openLoginPage() {
        SwingUtilities.invokeLater(() -> {
            // Spring 컨텍스트에서 LoginFrame 가져오기
            LoginFrame loginFrame = ApplicationContextProvider.getContext().getBean(LoginFrame.class);
            loginFrame.setVisible(true);
            dispose(); // 현재 프레임 닫기
        });
    }

    private void openJoinPage() {
        SwingUtilities.invokeLater(() -> {
            // Spring 컨텍스트에서 LoginFrame 가져오기
            JoinFrame joinFrame = ApplicationContextProvider.getContext().getBean(JoinFrame.class);
            joinFrame.setVisible(true);
            dispose(); // 현재 프레임 닫기
        });
    }

    private List<String> getPlanTitlesFromDatabase() {
        PlanService planService = ApplicationContextProvider.getContext().getBean(PlanService.class);
        List<PlanDTO> plans = planService.getAllPlans();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년MM월dd일");

        return plans.stream()
                .map(plan -> {
                    String dateString = plan.getDate() != null
                            ? plan.getDate().format(formatter)
                            : "날짜 없음";
                    return plan.getTitle() + " - " + dateString; // 형식: title - yyyy년MM월dd일
                })
                .collect(Collectors.toList());
    }

    class MyPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.black);
            g2.drawLine(123, 85, 866, 85);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            MainpageFrame.FontLoader.loadCustomFont(
                    "C:\\Users\\Owner\\Desktop\\workspace\\java\\Plan-J\\src\\main\\java\\com\\example\\planj\\font\\Gumi Romance.ttf",
                    "낭만있구미체"
            );
            MainpageFrame.FontLoader.loadCustomFont(
                    "C:\\Users\\Owner\\Desktop\\workspace\\java\\Plan-J\\src\\main\\java\\com\\example\\planj\\font\\SejongGeulggot.otf",
                    "세종글꽃체"
            );

            UIManager.put("OptionPane.background", Color.WHITE); // OptionPane 자체 배경색
            UIManager.put("Panel.background", Color.WHITE);

            ApplicationContextProvider.getContext().getBean(MainpageFrame.class).setVisible(true);
        });
    }
}