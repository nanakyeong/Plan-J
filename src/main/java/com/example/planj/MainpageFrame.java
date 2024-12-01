package com.example.planj;

import com.example.planj.db.PlanDTO;
import com.example.planj.db.PlanService;
import com.example.planj.frame.JoinFrame;
import com.example.planj.frame.LoginFrame;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MainpageFrame extends JFrame {
    private final PlanService planService;
    private final JButton[] planButtons = new JButton[7];
    private JLabel usernameLabel; // 사용자 이름을 표시할 라벨

    @Autowired private UploadpageFrame uploadFrame;

    private JTextField search_plan; // 검색 텍스트 필드

    // 지역 및 시군구 드롭박스 선언
    private JComboBox<String> areaCodeComboBoxMain;
    private JComboBox<String> sigunguComboBoxMain;
    private Map<String, Integer> areaCodeMapMain = new HashMap<>();
    private Map<String, Integer> sigunguCodeMapMain = new HashMap<>();
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
        logo1.setBounds(123, 135, 150, 30);
        contentPane.add(logo1);

        JLabel myplan = new JLabel("myplan");
        myplan.setBounds(700, 55, 100, 20);
        JLabel login = new JLabel("로그인");
        login.setBounds(762, 55, 100, 20);
        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openLoginPage();
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
        searchPanel.setBounds(500, 142, 380, 23);
        searchPanel.setBounds(420, 142, 460, 22);
        searchPanel.setLayout(null);
        contentPane.add(searchPanel);

        // 지역 드롭박스
        areaCodeComboBoxMain = new JComboBox<>();
        areaCodeComboBoxMain.setBounds(0, 0, 120, 22);
        areaCodeComboBoxMain.addActionListener(e -> updateSigunguComboBoxMain());
        searchPanel.add(areaCodeComboBoxMain);
        populateAreaCodeComboBoxMain();

        // 시군구 드롭박스
        sigunguComboBoxMain = new JComboBox<>();
        sigunguComboBoxMain.setBounds(125, 0, 120, 22);
        searchPanel.add(sigunguComboBoxMain);

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
                String region = (String) areaCodeComboBoxMain.getSelectedItem();
                String district = (String) sigunguComboBoxMain.getSelectedItem();
                String placeKeyword = search_plan.getText().trim();

                // 검색 조건에 따라 필터링
                updatePlanButtonsForSearch(region, district, placeKeyword);
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
            PlanwritepageFrame planFrame = ApplicationContextProvider.getContext().getBean(PlanwritepageFrame.class);
            planFrame.disableEditing(); // UI 비활성화
            planFrame.setPlanDTO(planDTO);
            planFrame.setVisible(true);
            dispose();
        });
    }

    private void populateAreaCodeComboBoxMain() {
        String urlString = "https://apis.data.go.kr/B551011/KorService1/areaCode1?serviceKey=" + SERVICE_KEY +
                "&numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json";
        try {
            JSONObject areaDataMain = fetchDataMain(urlString);
            if (areaDataMain != null) {
                JSONArray areaArrayMain = areaDataMain.getJSONArray("item");
                areaCodeComboBoxMain.addItem("지역 선택"); // 기본 항목
                for (int i = 0; i < areaArrayMain.length(); i++) {
                    JSONObject areaMain = areaArrayMain.getJSONObject(i);
                    String areaNameMain = areaMain.getString("name");
                    int areaCodeMain = areaMain.getInt("code");
                    areaCodeComboBoxMain.addItem(areaNameMain);
                    areaCodeMapMain.put(areaNameMain, areaCodeMain);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSigunguComboBoxMain() {
        String selectedAreaMain = (String) areaCodeComboBoxMain.getSelectedItem();
        if (selectedAreaMain == null || selectedAreaMain.equals("지역 선택")) return;

        int selectedAreaCodeMain = areaCodeMapMain.getOrDefault(selectedAreaMain, 0);
        String urlString = "https://apis.data.go.kr/B551011/KorService1/areaCode1?serviceKey=" + SERVICE_KEY +
                "&numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=AppTest&areaCode=" + selectedAreaCodeMain + "&_type=json";
        try {
            JSONObject sigunguDataMain = fetchDataMain(urlString);
            if (sigunguDataMain != null) {
                JSONArray sigunguArrayMain = sigunguDataMain.getJSONArray("item");
                sigunguComboBoxMain.removeAllItems();
                sigunguComboBoxMain.addItem("시군구 선택");
                sigunguCodeMapMain.clear();
                for (int i = 0; i < sigunguArrayMain.length(); i++) {
                    JSONObject sigunguMain = sigunguArrayMain.getJSONObject(i);
                    String sigunguNameMain = sigunguMain.getString("name");
                    int sigunguCodeMain = sigunguMain.getInt("code");
                    sigunguComboBoxMain.addItem(sigunguNameMain);
                    sigunguCodeMapMain.put(sigunguNameMain, sigunguCodeMain);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject fetchDataMain(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseMain = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseMain.append(line);
            }
            reader.close();

            return new JSONObject(responseMain.toString()).getJSONObject("response").getJSONObject("body").getJSONObject("items");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updatePlanButtonsForSearch(String region, String district, String placeKeyword) {
        List<PlanDTO> plans = planService.getIsRegisteredTrue();

        // 결과를 담을 리스트
        List<PlanDTO> searchResults = new ArrayList<>();

        // 1순위: 검색한 장소가 포함된 계획 (코사인 유사도 적용)
        if (placeKeyword != null) {
            for (PlanDTO plan : plans) {
                double maxSimilarity = plan.getPlacesPerDay().values().stream()
                        .flatMap(List::stream)
                        .mapToDouble(place -> calculateCosineSimilarity(placeKeyword, place))
                        .max().orElse(0.0);

                // 임계값(예: 0.5) 이상의 유사도를 가진 계획만 추가
                if (maxSimilarity >= 0.5) {
                    searchResults.add(plan);
                }
            }
        }

        // 2순위: 검색한 장소 근처에 있는 같은 지역 내 계획
        if (district != null) {
            for (PlanDTO plan : plans) {
                if (district.equals(plan.getDistrict()) && !searchResults.contains(plan)) {
                    searchResults.add(plan);
                }
            }
        }

        // 3순위: 같은 지역의 계획
        if (region != null) {
            for (PlanDTO plan : plans) {
                if (region.equals(plan.getRegion()) && !searchResults.contains(plan)) {
                    searchResults.add(plan);
                }
            }
        }

        // 1순위 결과를 유사도 기준으로 정렬
        if (placeKeyword != null) {
            searchResults.sort((plan1, plan2) -> {
                double maxSim1 = plan1.getPlacesPerDay().values().stream()
                        .flatMap(List::stream)
                        .mapToDouble(place -> calculateCosineSimilarity(placeKeyword, place))
                        .max().orElse(0.0);

                double maxSim2 = plan2.getPlacesPerDay().values().stream()
                        .flatMap(List::stream)
                        .mapToDouble(place -> calculateCosineSimilarity(placeKeyword, place))
                        .max().orElse(0.0);

                return Double.compare(maxSim2, maxSim1); // 높은 유사도 우선
            });
        }

        // 최대 8개만 버튼에 표시
        int maxButtons = Math.min(searchResults.size(), planButtons.length);
        for (int i = 0; i < maxButtons; i++) {
            PlanDTO plan = searchResults.get(i);
            JButton planButton = planButtons[i];
            planButton.setText(plan.getTitle());
            planButton.setVisible(true);

            // 버튼 클릭 시 계획 열기
            planButton.addActionListener(e -> openPlan(plan));
        }

        // 나머지 버튼 숨기기
        for (int i = searchResults.size(); i < planButtons.length; i++) {
            planButtons[i].setVisible(false);
        }
    }

    private double calculateCosineSimilarity(String text1, String text2) {
        Map<Character, Integer> freqMap1 = getFrequencyMap(text1);
        Map<Character, Integer> freqMap2 = getFrequencyMap(text2);

        // 벡터 내적
        double dotProduct = freqMap1.keySet().stream()
                .filter(freqMap2::containsKey)
                .mapToDouble(c -> freqMap1.get(c) * freqMap2.get(c))
                .sum();

        // 벡터 크기 계산
        double magnitude1 = Math.sqrt(freqMap1.values().stream().mapToDouble(v -> v * v).sum());
        double magnitude2 = Math.sqrt(freqMap2.values().stream().mapToDouble(v -> v * v).sum());

        // 코사인 유사도 계산
        return (magnitude1 > 0 && magnitude2 > 0) ? (dotProduct / (magnitude1 * magnitude2)) : 0.0;
    }

    private Map<Character, Integer> getFrequencyMap(String text) {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }
        return freqMap;
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

