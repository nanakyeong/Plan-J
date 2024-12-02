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

    private void initialize() {
        setTitle("Plan J");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        Container contentPane  = getContentPane();
        contentPane.setLayout(null);

//        contentPane.setLayout(new BorderLayout());


        contentPane.setBackground(Color.WHITE);


        JLabel logoP = new JLabel("P");
        logoP.setFont(JoinFrame.FontLoader.getFont("낭만있구미체", 35f, Font.BOLD));
        logoP.setForeground(new Color(0x89AEBF));
        logoP.setBounds(123, 135, 30, 40);
        contentPane.add(logoP);

        JLabel logoJ = new JLabel("J");
        logoJ.setFont(JoinFrame.FontLoader.getFont("낭만있구미체", 35f, Font.BOLD));
        logoJ.setForeground(new Color(0x436698));
        logoJ.setBounds(217, 135, 30, 40);
        contentPane.add(logoJ);

        JLabel logoText = new JLabel("lan");
        logoText.setFont(JoinFrame.FontLoader.getFont("낭만있구미체", 35f, Font.BOLD));
        logoText.setBounds(151, 135, 70, 40);
        contentPane.add(logoText);

        logoText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openMain();
            }
        });



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
        });


//        JLabel name = new JLabel("용강천사님");
//        name.setFont(JoinFrame.FontLoader.getFont("세종글꽃체",18f,Font.PLAIN));
//        name.setBounds(870,55,100,20);
//        name.setHorizontalAlignment(SwingConstants.RIGHT);
//        name.setBackground(Color.WHITE);
//        name.setForeground(Color.BLACK);
//        name.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        contentPane.add(name);


        JLabel name = new JLabel("yg1004");
        name.setFont(JoinFrame.FontLoader.getFont("세종글꽃체", 18f, Font.PLAIN));
        name.setForeground(Color.decode("#436698"));
        name.setBackground(Color.WHITE);
        name.setOpaque(true); // 배경 색이 보이도록 설정
        name.setBounds(711, 47, 80, 30); // 크기와 위치 설정
        name.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스를 올리면 커서 변경

        name.setHorizontalAlignment(SwingConstants.CENTER); // 수평 중앙 정렬
        name.setVerticalAlignment(SwingConstants.CENTER);   // 수직 중앙 정렬


        JLabel logout = new JLabel("로그아웃");
        logout.setFont(JoinFrame.FontLoader.getFont("세종글꽃체", 18f, Font.PLAIN));
        logout.setForeground(Color.WHITE);
        logout.setBackground(Color.BLACK);
        logout.setOpaque(true); // 배경 색이 보이도록 설정
        logout.setBounds(800, 47, 80, 30); // 크기와 위치 설정
        logout.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스를 올리면 커서 변경

        logout.setHorizontalAlignment(SwingConstants.CENTER);
        logout.setVerticalAlignment(SwingConstants.CENTER);

        contentPane.add(myplan);
        contentPane.add(name);
        contentPane.add(logout);


        List<String> plans = getPlanTitlesFromDatabase();

        DefaultListModel<String> planListModel = new DefaultListModel<>();
        plans.forEach(planListModel::addElement);
        RoundButton newPlanButton = new RoundButton("+ New Plan");
        newPlanButton.setBounds(123, 225, 150, 40); // 위치와 크기 설정
        newPlanButton.setFont(JoinFrame.FontLoader.getFont("세종글꽃체",18f,Font.BOLD)); // 글씨 크기 설정
        newPlanButton.setBackground(Color.decode("#e7e7e7")); // 배경색 설정
        newPlanButton.setForeground(Color.BLACK);
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
        list.setFont(JoinFrame.FontLoader.getFont("세종글꽃체", 16f, Font.PLAIN));  // JList 글꼴 설정
        list.setForeground(Color.BLACK);
        sp = new JScrollPane(list);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setBounds(123, 270, 738, 200); // 크기와 위치 설정
        contentPane.add(sp);

//        panel1 = new MyPanel();
//        panel1.setBounds(0, 0, 1000, 600);
//        contentPane.add(panel1);

        MyPanel panel1 = new MyPanel();
        panel1.setBounds(100, 60, 800, 50);
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
    private void openMain() {
        SwingUtilities.invokeLater(() -> {
            MainpageFrame mainpageFrame = ApplicationContextProvider.getContext().getBean(MainpageFrame.class);
            mainpageFrame.setVisible(true);
            dispose();
        });
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
            UIManager.put("OptionPane.background", Color.WHITE); // OptionPane 자체 배경색
            UIManager.put("Panel.background", Color.WHITE);

            ApplicationContextProvider.getContext().getBean(MainpageFrame.class).setVisible(true);
        });
    }
}