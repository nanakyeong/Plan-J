package com.example.planj;

import com.example.planj.db.PlanDTO;
import com.example.planj.db.PlanService;
import com.example.planj.frame.LoginFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

@Component
public class MainpageFrame extends JFrame {
    private final PlanService planService;
    private final JButton[] planButtons = new JButton[7];
    private final JLabel[] planLabels = new JLabel[7];

    @Autowired private UploadpageFrame uploadFrame;

    private JTextField search_plan; // 검색 텍스트 필드

    @Autowired
    public MainpageFrame(PlanService planService) {
        this.planService = planService;
        initialize();
    }

    private void initialize() {
        setTitle("Plan J");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        JLabel logo1 = new JLabel("Plan J");
        logo1.setFont(new Font("돋움", Font.BOLD, 35));
        logo1.setBounds(123, 135, 150, 30);
        contentPane.add(logo1);

        JLabel myplan = new JLabel("myplan");
        myplan.setBounds(700, 55, 100, 20);
        JLabel login = new JLabel("로그인");
        login.setBounds(762, 55, 100, 20);
        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openLoginPage(); // LoginFrame으로 이동
            }
        });
        contentPane.add(login);

        JLabel join = new JLabel("회원가입");
        join.setBounds(814, 55, 100, 20);
        join.addMouseListener(new MouseAdapter() {
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
        searchPanel.setBounds(500, 142, 380, 23); // 크기를 늘려서 라디오 버튼 추가 가능
        searchPanel.setLayout(null);
        contentPane.add(searchPanel);

        // 라디오 버튼 그룹 생성
        ButtonGroup radioGroup = new ButtonGroup();
        JRadioButton regionRadioButton = new JRadioButton("지역");
        regionRadioButton.setBounds(0, 0, 60, 23);
        regionRadioButton.setSelected(true); // 기본 선택
        searchPanel.add(regionRadioButton);

        JRadioButton placeRadioButton = new JRadioButton("장소");
        placeRadioButton.setBounds(60, 0, 60, 23);
        searchPanel.add(placeRadioButton);

        // 라디오 버튼 그룹에 추가
        radioGroup.add(regionRadioButton);
        radioGroup.add(placeRadioButton);

        // 검색 텍스트 필드
        search_plan = new JTextField();
        search_plan.setBounds(120, 0, 210, 23);
        searchPanel.add(search_plan);

        // 검색 아이콘
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setBounds(330, 0, 30, 22);
        searchIcon.setHorizontalAlignment(SwingConstants.CENTER);
        searchPanel.add(searchIcon);

        // 검색 결과 라벨
        JLabel searchResultLabel = new JLabel(); // 검색 결과 라벨 초기화
        searchResultLabel.setFont(new Font("돋움", Font.BOLD, 18));
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

        JButton btn_newplan = new JButton("+");
        btn_newplan.setBounds(123, 230, 120, 120);
        JLabel newplan = new JLabel("plan 업로드");
        newplan.setBounds(143, 350, 100, 20);
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
            LoginFrame loginFrame = ApplicationContextProvider.getContext().getBean(LoginFrame.class);
            loginFrame.setVisible(true);
            dispose(); // 현재 프레임 닫기
        });
    }

    private void createPlanButtons(Container contentPane) {
        for (int i = 0; i < planButtons.length; i++) {
            JButton planButton = new JButton();
            planButton.setBounds(123 + (((i+1) % 4) * 210), 230 + (((i+1) / 4) * 170), 120, 120);
            planButton.setVisible(false);
            contentPane.add(planButton);
            planButtons[i] = planButton;

            JLabel planLabel = new JLabel();
            planLabel.setBounds(143 + (((i+1) % 4) * 210), 350 + (((i+1) / 4) * 170), 100, 20);
            planLabel.setVisible(false);
            contentPane.add(planLabel);
            planLabels[i] = planLabel;
        }
    }

    private void updatePlanButtons() {
        List<PlanDTO> plans = planService.getIsRegisteredTrue();

        // 반복문에서 유효한 버튼 수를 초과하지 않도록 설정
        int maxButtons = Math.min(plans.size(), planButtons.length);
        for (int i = 0; i < maxButtons; i++) {
            PlanDTO plan = plans.get(i);
            JButton planButton = planButtons[i];
            JLabel planLabel = planLabels[i];
            //planLabel.setText("[" + plan.getRegion() + "] " + plan.getTitle());
            planButton.setText(plan.getRegion());
            planLabel.setText(plan.getTitle());
            planButton.setVisible(true);
            planLabel.setVisible(true);

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
            PlanwritepageFrame planFrame = ApplicationContextProvider.getContext().getBean(PlanwritepageFrame.class);
            planFrame.disableEditing(); // UI 비활성화
            planFrame.setPlanDTO(planDTO);
            planFrame.setVisible(true);
            dispose();
        });
    }

    class MyPanel extends JPanel {
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
            ApplicationContextProvider.getContext().getBean(MainpageFrame.class).setVisible(true);
        });
    }
}

