package com.example.planj;

import javax.swing.*;
import java.awt.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


public class PlanwritepageFrame extends JFrame {

    private JPanel planPanel;
    private MyPanel myPanel; // MyPanel 참조 변수 추가

    private static final String SERVICE_KEY = "pRHMKrAJfJJZTC104XWkGvOIvKtKcO6zFysOGGDrH3Bo%2FktklWp6urJAiA5DoWSY3rf7LEKeb2NU5aDiAfDhlw%3D%3D"; // TourAPI 서비스 키를 입력하세요

    private JComboBox<String> areaCodeComboBox;
    private JComboBox<String> sigunguComboBox;
    private Map<String, Integer> areaCodeMap = new HashMap<>(); // 지역 이름과 코드 매핑을 위한 HashMap 추가
    private Map<String, Integer> sigunguCodeMap = new HashMap<>();
    // HashMap 선언
    private Map<String, List<String>> dayToPlacesMap = new HashMap<>();
    private String accommodationName; // 숙소 이름 저장
    private double accommodationLat;  // 숙소 위도
    private double accommodationLon;  // 숙소 경도

    public PlanwritepageFrame() {
        setTitle("Plan J");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        JLabel logo1 = new JLabel("Plan J");
        logo1.setFont(new Font("돋움", Font.BOLD, 35));
        logo1.setBounds(123, 135, 150, 30);
        contentPane.add(logo1);

        JLabel ai = new JLabel("AI");
        ai.setBounds(668, 55, 100, 20);
        JLabel myplan = new JLabel("myplan");
        myplan.setBounds(700, 55, 100, 20);
        JLabel login = new JLabel("로그인");
        login.setBounds(762, 55, 100, 20);
        JLabel join = new JLabel("회원가입");
        join.setBounds(814, 55, 100, 20);
        contentPane.add(ai);
        contentPane.add(myplan);
        contentPane.add(login);
        contentPane.add(join);

        JLabel title = new JLabel("제목 : ");
        title.setBounds(138, 220, 210, 20);
        contentPane.add(title);

        JTextField planTitle = new JTextField();
        planTitle.setBounds(178, 220, 200, 23);
        contentPane.add(planTitle);

        Integer[] choices1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        JComboBox<Integer> section1 = new JComboBox<>(choices1);
        section1.setBounds(395, 220, 50, 23);
        contentPane.add(section1);

        JLabel day1 = new JLabel("박");
        day1.setBounds(450, 220, 30, 20);
        contentPane.add(day1);

        JComboBox<Integer> section2 = new JComboBox<>();
        section2.setBounds(475, 220, 50, 23);
        contentPane.add(section2);

        JLabel day2 = new JLabel("일");
        day2.setBounds(530, 220, 30, 20);
        contentPane.add(day2);

        // 지역 선택 드롭다운
        areaCodeComboBox = new JComboBox<>();
        areaCodeComboBox.setBounds(138, 250, 120, 23);
        areaCodeComboBox.addActionListener(e -> updateSigunguComboBox());
        contentPane.add(areaCodeComboBox);

        // 시군구 선택 드롭다운
        sigunguComboBox = new JComboBox<>();
        sigunguComboBox.setBounds(268, 250, 120, 23);
        contentPane.add(sigunguComboBox);

        // 지역 데이터를 초기화
        populateAreaCodeComboBox();

        section1.addActionListener(e -> updateDays(section1, section2));
        section2.addActionListener(e -> updatePlanPanel(section2));

        // Add 숙소 추가 button next to area and sigungu dropdowns
        JButton addAccommodationButton = new JButton("숙소 추가");
        addAccommodationButton.setBounds(425, 250, 100, 23);
        addAccommodationButton.addActionListener(e -> openAccommodationPopup());
        contentPane.add(addAccommodationButton);

        // 일정 리스트 패널
        planPanel = new JPanel();
        planPanel.setLayout(new BoxLayout(planPanel, BoxLayout.Y_AXIS));
        planPanel.setBackground(Color.WHITE); // 흰색 배경 설정

        JScrollPane scrollPane = new JScrollPane(planPanel);
        scrollPane.setBounds(138, 280, 408, 228);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scrollPane);

        section1.setSelectedIndex(0);

        myPanel = new MyPanel();
        myPanel.setBounds(0, 0, 1000, 600);
        contentPane.add(myPanel);

        setVisible(true);

    }

    private void openAccommodationPopup() {
        AccommodationPopup accommodationPopup = new AccommodationPopup(this);
        accommodationPopup.setVisible(true);
    }

    // 숙소를 일정에 추가하는 메서드
    public void addAccommodationToSchedule(String accommodationName, double latitude, double longitude) {
        this.accommodationName = accommodationName;
        this.accommodationLat = latitude;
        this.accommodationLon = longitude;

        for (Component comp : planPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel dayPanel = (JPanel) comp;

                // 각 dayPanel에 저장된 라벨 가져와 업데이트
                JLabel topLabel = (JLabel) dayPanel.getClientProperty("topAccommodationLabel");

                if (topLabel != null) topLabel.setText(accommodationName);
            }
        }

        planPanel.revalidate();
        planPanel.repaint();
    }


    private void updateDays(JComboBox<Integer> section1, JComboBox<Integer> section2) {
        int selectedNights = (Integer) section1.getSelectedItem();
        section2.removeAllItems();

        int minDays = selectedNights + 1;
        int maxDays = selectedNights + 2;

        for (int i = minDays; i <= maxDays; i++) {
            section2.addItem(i);
        }
    }

    private void updatePlanPanel(JComboBox<Integer> section2) {
        planPanel.removeAll();

        if (section2.getSelectedItem() != null) {
            int days = (Integer) section2.getSelectedItem();
            for (int i = 1; i <= days; i++) {
                addDayPanel(planPanel, i + "일차");
            }
        }

        planPanel.revalidate();
        planPanel.repaint();
    }

    private void addDayPanel(JPanel planPanel, String dayText) {
        JPanel dayPanel = new JPanel();
        dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));
        dayPanel.setBackground(Color.WHITE);
        dayPanel.setName(dayText); // dayText를 이름으로 설정하여 식별

        dayPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(0, 10, 10, 0) // 내부 여백 설정 (위, 좌, 아래, 우)
        ));

        // "일차" 라벨과 "경로최적화" 라벨을 가로로 배치하기 위해 패널 생성
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
        labelPanel.setBackground(Color.WHITE);

        JLabel dayLabel = new JLabel(dayText);
        dayLabel.setFont(new Font("돋움", Font.BOLD, 17));
        dayLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton optimizeButton = new JButton("경로최적화");
        optimizeButton.setFont(new Font("돋움", Font.PLAIN, 12));
        optimizeButton.setForeground(Color.BLUE);
        optimizeButton.setFocusPainted(false);
        optimizeButton.setContentAreaFilled(false);
        optimizeButton.setBorderPainted(false);
        optimizeButton.addActionListener(e -> openRouteDialog(dayText)); // 액션 리스너 설정

        labelPanel.add(optimizeButton);
        labelPanel.add(dayLabel);
        labelPanel.add(Box.createHorizontalGlue());
        labelPanel.add(optimizeButton);
        labelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        dayPanel.add(labelPanel);

        // 상단 고정 숙소 라벨 추가
        JLabel topAccommodationLabel = new JLabel(accommodationName != null ? accommodationName : "숙소 미지정");
        topAccommodationLabel.setFont(new Font("돋움", Font.PLAIN, 12));
        topAccommodationLabel.setForeground(Color.BLUE);
        topAccommodationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topAccommodationLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 0));
        dayPanel.add(topAccommodationLabel);

        // 라벨 클릭 시 지도 위치 업데이트
        topAccommodationLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (accommodationLat != 0 && accommodationLon != 0) {
                    myPanel.updateMapMarker(accommodationLat, accommodationLon);
                } else {
                    JOptionPane.showMessageDialog(dayPanel, "숙소 위치가 지정되지 않았습니다.");
                }
            }
        });

        // 장소 추가 버튼을 아래쪽에 배치
        JButton addPlaceButton = new JButton("장소를 추가하려면 클릭하세요...");
        addPlaceButton.setFont(new Font("돋움", Font.PLAIN, 12));
        addPlaceButton.setForeground(Color.GRAY);
        addPlaceButton.setFocusPainted(false);
        addPlaceButton.setContentAreaFilled(false);
        addPlaceButton.setBorderPainted(false);
        addPlaceButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addPlaceButton.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 0));
        addPlaceButton.addActionListener(e -> openAddPlaceDialog(dayPanel));
        dayPanel.add(addPlaceButton); // dayPanel에 addPlaceButton을 추가하여 배치

        // 라벨을 업데이트하기 위해 저장
        dayPanel.putClientProperty("topAccommodationLabel", topAccommodationLabel);

        // 최종적으로 planPanel에 dayPanel 추가 및 여백 조정
        planPanel.add(dayPanel);
        planPanel.add(Box.createVerticalStrut(5)); // 위아래 간격을 위한 여백
    }

    private void openRouteDialog(String dayText) {
        Map<String, Object> dayData = getLocationsForDay(dayText);
        List<double[]> locationCoordinates = (List<double[]>) dayData.get("locations");
        List<String> placeNames = (List<String>) dayData.get("placeNames");

        // 숙소 좌표와 이름을 경로 시작과 끝에 추가
        if (accommodationName != null && accommodationLat != 0 && accommodationLon != 0) {
            // 경로 시작 위치로 숙소 추가
            locationCoordinates.add(0, new double[]{accommodationLat, accommodationLon});
            placeNames.add(accommodationName);

            // 경로 끝 위치로 숙소 추가
            locationCoordinates.add(new double[]{accommodationLat, accommodationLon});
            placeNames.add(accommodationName);
        }

        RouteDialog routeDialog = new RouteDialog(locationCoordinates, placeNames);
        routeDialog.setVisible(true);
    }


    // 특정 일차의 장소 좌표 목록을 가져오는 메서드
    private Map<String, Object> getLocationsForDay(String dayText) {
        List<double[]> locations = new ArrayList<>();
        List<String> placeNames = dayToPlacesMap.getOrDefault(dayText, new ArrayList<>());

        for (String placeName : placeNames) {
            JSONObject placeInfo = searchPlaceByKeyword(this, placeName);
            if (placeInfo != null) {
                double mapx = placeInfo.getDouble("mapx");
                double mapy = placeInfo.getDouble("mapy");
                locations.add(new double[]{mapx, mapy});
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("locations", locations);  // 좌표 리스트 저장
        result.put("placeNames", placeNames);  // 장소 이름 리스트 저장
        return result;
    }



    private void openAddPlaceDialog(JPanel dayPanel) {
        PopupDialog dialog = new PopupDialog(this, dayPanel);
        dialog.setVisible(true);
    }

    public void addPlaceToSchedule(String placeName, JPanel dayPanel) {
        // 기존의 "장소를 추가하려면 클릭하세요..." 버튼 제거
        if (dayPanel.getComponentCount() > 0) {
            Component lastComponent = dayPanel.getComponent(dayPanel.getComponentCount() - 1);
            if (lastComponent instanceof JButton && ((JButton) lastComponent).getText().equals("장소를 추가하려면 클릭하세요...")) {
                dayPanel.remove(lastComponent);
            }
        }

        String dayText = dayPanel.getName(); // dayPanel의 이름을 통해 dayText 추출

        // HashMap에 장소 목록을 업데이트
        dayToPlacesMap.putIfAbsent(dayText, new ArrayList<>()); // dayText가 없을 경우 리스트 초기화
        dayToPlacesMap.get(dayText).add(placeName); // dayText에 해당하는 장소 목록에 추가

        // 여백 블록과 장소 패널을 담는 패널 생성
        JPanel combinedPanel = new JPanel();
        combinedPanel.setLayout(new BoxLayout(combinedPanel, BoxLayout.Y_AXIS));
        combinedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        combinedPanel.setBackground(Color.WHITE);

        // 여백 블록 생성 (상단 여백)
        Component verticalStrut = Box.createVerticalStrut(10);
        combinedPanel.add(verticalStrut);

        // 장소 항목 패널 생성
        JPanel placePanel = new JPanel();
        placePanel.setLayout(new BoxLayout(placePanel, BoxLayout.X_AXIS));
        placePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        placePanel.setBackground(Color.WHITE);
        placePanel.setMaximumSize(new Dimension(400, 30));

        // 장소 이름 라벨
        JLabel placeLabel = new JLabel(placeName);
        placeLabel.setFont(new Font("돋움", Font.PLAIN, 12));
        placeLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), // 테두리 추가
                BorderFactory.createEmptyBorder(0, 15, 0, 0) // 내부 여백 설정
        ));
        placeLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        placeLabel.setPreferredSize(new Dimension(200, 20)); // 최대 너비 설정
        placeLabel.setToolTipText(placeName); // 툴팁에 전체 텍스트 표시

        // 텍스트가 길 때 줄임표(...)로 표시
        placeLabel.setText(placeName.length() > 15 ? placeName.substring(0, 15) + "..." : placeName);
        placePanel.add(placeLabel);
        // 클릭 시 지도의 위치를 업데이트하는 리스너 추가
        placeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updateMapWithPlace(placeName); // MyPanel 참조를 이용해 지도 업데이트
            }
        });

        // 위로 이동 버튼
        JButton upButton = new JButton("▲");
        upButton.setFont(new Font("돋움", Font.PLAIN, 13));
        upButton.setFocusPainted(false);
        upButton.setContentAreaFilled(false);
        upButton.setBorderPainted(false);
        upButton.setPreferredSize(new Dimension(50, 15));
        upButton.setAlignmentY(Component.CENTER_ALIGNMENT); // 세로 중앙 정렬
        upButton.addActionListener(e -> movePlaceContainerUp(dayPanel, combinedPanel));

        // 아래로 이동 버튼
        JButton downButton = new JButton("▼");
        downButton.setFont(new Font("돋움", Font.PLAIN, 13));
        downButton.setFocusPainted(false);
        downButton.setContentAreaFilled(false);
        downButton.setBorderPainted(false);
        downButton.setPreferredSize(new Dimension(50, 15));
        downButton.setAlignmentY(Component.CENTER_ALIGNMENT); // 세로 중앙 정렬
        downButton.addActionListener(e -> movePlaceContainerDown(dayPanel, combinedPanel));

        // 삭제 버튼
        JButton deleteButton = new JButton("×");
        deleteButton.setFont(new Font("돋움", Font.BOLD, 14));
        deleteButton.setForeground(Color.RED);
        deleteButton.setFocusPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setBorderPainted(false);
        deleteButton.setPreferredSize(new Dimension(50, 15));
        deleteButton.setAlignmentY(Component.CENTER_ALIGNMENT); // 세로 중앙 정렬
        deleteButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 15));
        deleteButton.addActionListener(e -> {
            dayPanel.remove(combinedPanel);
            dayToPlacesMap.get(dayText).remove(placeName); // HashMap에서 장소 삭제
            dayPanel.revalidate();
            dayPanel.repaint();
        });

        // 버튼들을 placePanel에 추가
        placePanel.add(Box.createHorizontalGlue()); // 라벨과 버튼들 사이의 여백
        placePanel.add(upButton);
        placePanel.add(downButton);
        placePanel.add(deleteButton);

        // combinedPanel에 placePanel 추가
        combinedPanel.add(placePanel);

        // dayPanel에 combinedPanel 추가
        dayPanel.add(combinedPanel);

        // 새로운 "장소를 추가하려면 클릭하세요..." 버튼 추가
        JButton addPlaceButton = new JButton("장소를 추가하려면 클릭하세요...");
        addPlaceButton.setFont(new Font("돋움", Font.PLAIN, 12));
        addPlaceButton.setForeground(Color.GRAY);
        addPlaceButton.setFocusPainted(false);
        addPlaceButton.setContentAreaFilled(false);
        addPlaceButton.setBorderPainted(false);
        addPlaceButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addPlaceButton.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 0));
        addPlaceButton.addActionListener(e -> openAddPlaceDialog(dayPanel));
        dayPanel.add(addPlaceButton);

        // 레이아웃 업데이트
        dayPanel.revalidate();
        dayPanel.repaint();
    }


    // placeContainerPanel 전체를 위로 이동하는 메서드
    private void movePlaceContainerUp(JPanel dayPanel, JPanel placeContainerPanel) {
        int index = dayPanel.getComponentZOrder(placeContainerPanel);
        if (index > 1) { // 첫 번째 위치로 이동하지 않도록 제한
            dayPanel.remove(placeContainerPanel);
            dayPanel.add(placeContainerPanel, index - 1);
            dayPanel.revalidate();
            dayPanel.repaint();
        }
    }

    // placeContainerPanel 전체를 아래로 이동하는 메서드
    private void movePlaceContainerDown(JPanel dayPanel, JPanel placeContainerPanel) {
        int index = dayPanel.getComponentZOrder(placeContainerPanel);
        if (index < dayPanel.getComponentCount() - 2) { // 마지막 위치로 이동하지 않도록 제한
            dayPanel.remove(placeContainerPanel);
            dayPanel.add(placeContainerPanel, index + 1);
            dayPanel.revalidate();
            dayPanel.repaint();
        }
    }


    class MyPanel extends JPanel {
        private JFXPanel jfxPanel;
        private WebEngine webEngine;

        public MyPanel() {
            setLayout(null);

            jfxPanel = new JFXPanel();
            jfxPanel.setBounds(566, 221, 284, 288);
            add(jfxPanel);

            Platform.runLater(() -> {
                WebView webView = new WebView();
                webEngine = webView.getEngine();

                URL url = getClass().getResource("/map.html");
                if (url != null) {
                    webEngine.load(url.toExternalForm());
                } else {
                    webEngine.loadContent("<html><body><h1>파일을 찾을 수 없습니다.</h1></body></html>");
                }

                jfxPanel.setScene(new Scene(webView));
            });
        }

        // 마커 위치 업데이트 메서드
        public void updateMapMarker(double latitude, double longitude) {
            Platform.runLater(() -> {
                if (webEngine != null) {
                    webEngine.executeScript("setMapPosition(" + latitude + ", " + longitude + ");");
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // 테두리 설정
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.black);
            g2.drawLine(123, 85, 866, 85);

            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.gray);
            g2.drawRect(123, 200, 743, 330);
            g2.drawRect(565, 220, 285, 290);
        }

    }

    private JSONObject searchPlaceByKeyword(PlanwritepageFrame parentFrame, String keyword) {
        int areaCode = parentFrame.getSelectedAreaCode();
        String sigunguCode = parentFrame.getSelectedSigunguCode();

        String urlString = "http://apis.data.go.kr/B551011/KorService1/searchKeyword1?serviceKey=" + SERVICE_KEY
                + "&numOfRows=1&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A&keyword="
                + URLEncoder.encode(keyword, StandardCharsets.UTF_8)
                + "&areaCode=" + areaCode;

        if (!sigunguCode.isEmpty()) {
            urlString += "&sigunguCode=" + sigunguCode;
        }

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.out.println("HTTP 응답 코드: " + responseCode);
                System.out.println("오류: API 요청이 실패했습니다.");
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            System.out.println("API 응답 내용: " + response.toString());

            JSONObject jsonObject = new JSONObject(response.toString());
            JSONObject body = jsonObject.getJSONObject("response").getJSONObject("body");
            JSONArray items = body.getJSONObject("items").getJSONArray("item");

            if (items.length() > 0) {
                return items.getJSONObject(0); // 첫 번째 장소 정보를 반환
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 지도에서 위치를 업데이트하는 메서드
    private void updateMapWithPlace(String placeName) {
        JSONObject placeInfo = searchPlaceByKeyword(this, placeName);
        if (placeInfo != null) {
            double latitude = placeInfo.getDouble("mapy");
            double longitude = placeInfo.getDouble("mapx");
            myPanel.updateMapMarker(latitude, longitude);
        } else {
            JOptionPane.showMessageDialog(this, "장소 정보를 찾을 수 없습니다.");
        }
    }


    private void populateAreaCodeComboBox() {
        String urlString = "https://apis.data.go.kr/B551011/KorService1/areaCode1?serviceKey=" + SERVICE_KEY +
                "&numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json";
        try {
            JSONObject areaData = fetchData(urlString);
            if (areaData != null) {
                JSONArray areaArray = areaData.getJSONArray("item");
                areaCodeComboBox.addItem("지역 선택");
                for (int i = 0; i < areaArray.length(); i++) {
                    JSONObject area = areaArray.getJSONObject(i);
                    String areaName = area.getString("name");
                    int areaCode = area.getInt("code"); // 코드 값을 가져옴
                    areaCodeComboBox.addItem(areaName);
                    areaCodeMap.put(areaName, areaCode); // 지역 이름과 코드 매핑
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 지역 이름을 코드와 매핑하여 반환하는 메서드
    private int getAreaCode(String areaName) {
        return areaCodeMap.getOrDefault(areaName, 1); // 존재하지 않으면 기본값 1 반환
    }

    // 선택한 지역에 따라 시군구 코드를 조회하여 드롭다운에 추가하는 메서드
    private void updateSigunguComboBox() {
        String selectedArea = (String) areaCodeComboBox.getSelectedItem();
        if (selectedArea == null || selectedArea.equals("지역 선택")) return;

        int selectedAreaCode = getAreaCode(selectedArea);
        String urlString = "https://apis.data.go.kr/B551011/KorService1/areaCode1?serviceKey=" + SERVICE_KEY +
                "&numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=AppTest&areaCode=" + selectedAreaCode + "&_type=json";
        try {
            JSONObject sigunguData = fetchData(urlString);
            if (sigunguData != null) {
                JSONArray sigunguArray = sigunguData.getJSONArray("item");
                sigunguComboBox.removeAllItems();
                sigunguComboBox.addItem("시군구 선택");
                sigunguCodeMap.clear();  // 기존 시군구 코드 매핑을 초기화
                for (int i = 0; i < sigunguArray.length(); i++) {
                    JSONObject sigungu = sigunguArray.getJSONObject(i);
                    String sigunguName = sigungu.getString("name");
                    int sigunguCode = sigungu.getInt("code"); // 시군구 코드 값을 가져옴
                    sigunguComboBox.addItem(sigunguName);
                    sigunguCodeMap.put(sigunguName, sigunguCode); // 시군구 이름과 코드 매핑
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

    public int getSelectedAreaCode() {
        String selectedArea = (String) areaCodeComboBox.getSelectedItem();
        int areaCode = getAreaCode(selectedArea);
        System.out.println("Selected Area: " + selectedArea + " | Area Code: " + areaCode); // 테스트 출력
        return areaCode; // 선택된 지역의 코드 반환
    }

    // 선택한 시군구의 코드를 반환하는 메서드
    public String getSelectedSigunguCode() {
        String selectedSigungu = (String) sigunguComboBox.getSelectedItem();
        if (selectedSigungu == null || selectedSigungu.equals("시군구 선택")) {
            System.out.println("Selected Sigungu: None (default)"); // 테스트 출력
            return ""; // 시군구가 선택되지 않았으면 빈 문자열 반환
        }
        String sigunguCode = String.valueOf(sigunguCodeMap.getOrDefault(selectedSigungu, 0)); // 시군구 코드를 정확히 반환
        System.out.println("Selected Sigungu: " + selectedSigungu + " | Sigungu Code: " + sigunguCode); // 테스트 출력
        return sigunguCode;
    }



    public static void main(String[] args) {
        new PlanwritepageFrame();
    }
}
