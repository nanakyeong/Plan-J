package com.example.planj;

import com.example.planj.db.PlanDTO;
import com.example.planj.db.PlanService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MainpageFrame extends JFrame {
    private final PlanService planService;
    private final JButton[] planButtons = new JButton[7];

    @Autowired private UploadpageFrame uploadFrame;

    private JTextField search_plan; // 검색 텍스트 필드

    // 지역 및 시군구 드롭박스 선언
    private JComboBox<String> areaCodeComboBox;
    private JComboBox<String> sigunguComboBox;
    private Map<String, Integer> areaCodeMap = new HashMap<>();
    private Map<String, Integer> sigunguCodeMap = new HashMap<>();
    private static final String SERVICE_KEY = "pRHMKrAJfJJZTC104XWkGvOIvKtKcO6zFysOGGDrH3Bo%2FktklWp6urJAiA5DoWSY3rf7LEKeb2NU5aDiAfDhlw%3D%3D";


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
        JLabel login = new JLabel("로그아웃");
        login.setBounds(762, 55, 100, 20);
        JLabel join = new JLabel("회원가입");
        join.setBounds(814, 55, 100, 20);
        contentPane.add(myplan);
        contentPane.add(login);
        contentPane.add(join);

        // 검색 패널
        JPanel searchPanel = new JPanel();
        searchPanel.setBounds(420, 142, 460, 22);
        searchPanel.setLayout(null);
        contentPane.add(searchPanel);

        // 지역 드롭박스
        areaCodeComboBox = new JComboBox<>();
        areaCodeComboBox.setBounds(0, 0, 120, 22);
        areaCodeComboBox.addActionListener(e -> updateSigunguComboBox());
        searchPanel.add(areaCodeComboBox);
        populateAreaCodeComboBox();

        // 시군구 드롭박스
        sigunguComboBox = new JComboBox<>();
        sigunguComboBox.setBounds(125, 0, 120, 22);
        searchPanel.add(sigunguComboBox);

        // 검색 텍스트 필드
        search_plan = new JTextField();
        search_plan.setBounds(250, 0, 160, 22);
        searchPanel.add(search_plan);

        // 검색 아이콘
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setBounds(415, 0, 30, 22);
        searchIcon.setHorizontalAlignment(SwingConstants.CENTER);
        searchIcon.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // 회색 1픽셀 테두리
        searchPanel.add(searchIcon);
        // 검색 아이콘 동작
        searchIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

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
            PlanwritepageFrame planFrame = ApplicationContextProvider.getContext().getBean(PlanwritepageFrame.class);
            planFrame.disableEditing(); // UI 비활성화
            planFrame.setPlanDTO(planDTO);
            planFrame.setVisible(true);
            dispose();
        });
    }

    private void populateAreaCodeComboBox() {
        String urlString = "https://apis.data.go.kr/B551011/KorService1/areaCode1?serviceKey=" + SERVICE_KEY +
                "&numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json";
        try {
            JSONObject areaData = fetchData(urlString);
            if (areaData != null) {
                JSONArray areaArray = areaData.getJSONArray("item");
                areaCodeComboBox.addItem("지역 선택"); // 기본 항목
                for (int i = 0; i < areaArray.length(); i++) {
                    JSONObject area = areaArray.getJSONObject(i);
                    String areaName = area.getString("name");
                    int areaCode = area.getInt("code");
                    areaCodeComboBox.addItem(areaName);
                    areaCodeMap.put(areaName, areaCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSigunguComboBox() {
        String selectedArea = (String) areaCodeComboBox.getSelectedItem();
        if (selectedArea == null || selectedArea.equals("지역 선택")) return;

        int selectedAreaCode = areaCodeMap.getOrDefault(selectedArea, 0);
        String urlString = "https://apis.data.go.kr/B551011/KorService1/areaCode1?serviceKey=" + SERVICE_KEY +
                "&numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=AppTest&areaCode=" + selectedAreaCode + "&_type=json";
        try {
            JSONObject sigunguData = fetchData(urlString);
            if (sigunguData != null) {
                JSONArray sigunguArray = sigunguData.getJSONArray("item");
                sigunguComboBox.removeAllItems();
                sigunguComboBox.addItem("시군구 선택");
                sigunguCodeMap.clear();
                for (int i = 0; i < sigunguArray.length(); i++) {
                    JSONObject sigungu = sigunguArray.getJSONObject(i);
                    String sigunguName = sigungu.getString("name");
                    int sigunguCode = sigungu.getInt("code");
                    sigunguComboBox.addItem(sigunguName);
                    sigunguCodeMap.put(sigunguName, sigunguCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject fetchData(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return new JSONObject(response.toString()).getJSONObject("response").getJSONObject("body").getJSONObject("items");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

