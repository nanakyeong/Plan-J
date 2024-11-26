package com.example.planj;

import java.awt.*;
import javax.swing.*;

import com.example.planj.db.PlanDTO;
import com.example.planj.db.PlanService;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.List;

@Component
public class PlanwritepageFrame extends JFrame {

    @Autowired
    private PlanService planService;

    private JPanel planPanel;
    private MyPanel myPanel;

    private static final String SERVICE_KEY = "pRHMKrAJfJJZTC104XWkGvOIvKtKcO6zFysOGGDrH3Bo%2FktklWp6urJAiA5DoWSY3rf7LEKeb2NU5aDiAfDhlw%3D%3D";

    private JTextField planTitle;
    private JComboBox<Integer> section1;
    private JComboBox<Integer> section2;
    private JComboBox<String> areaCodeComboBox;
    private JComboBox<String> sigunguComboBox;
    private Map<String, Integer> areaCodeMap = new HashMap<>();
    private Map<String, Integer> sigunguCodeMap = new HashMap<>();
    private Map<String, List<String>> dayToPlacesMap = new HashMap<>();
    private String accommodationName;
    private double accommodationLat;
    private double accommodationLon;
    private PlanDTO planDTO;
    private Map<String, String> accommodationsPerDay = new HashMap<>();
    private Map<String, List<String>> placesPerDay = new HashMap<>();

    public void setPlanDTO(PlanDTO planDTO) {
        this.planDTO = planDTO;

        if (planDTO != null) {
            // 제목, 박/일, 숙소 설정
            planTitle.setText(planDTO.getTitle());
            section1.setSelectedItem(planDTO.getNights());
            section2.setSelectedItem(planDTO.getDays());
            areaCodeComboBox.setSelectedItem(planDTO.getAccommodation());

            // 기존 데이터 초기화
            planPanel.removeAll();

            // 날짜별 장소 데이터를 처리
            Map<String, List<String>> placesPerDay = planDTO.getPlacesPerDay();
            if (placesPerDay != null) {
                placesPerDay.forEach((day, places) -> {
                    JLabel dayLabel = new JLabel(day);
                    planPanel.add(dayLabel);

                    if (places != null) {
                        for (String place : places) {
                            JLabel placeLabel = new JLabel("- " + place);
                            planPanel.add(placeLabel);
                        }
                    }
                });
            }

            planPanel.revalidate();
            planPanel.repaint();
        }
    }

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

        JButton saveButton = new JButton("저장하기");
        saveButton.setBounds(718, 160, 150, 30);
        saveButton.setFont(new Font("돋움", Font.BOLD, 18));
        saveButton.setBackground(new Color(255, 255, 255));
        contentPane.add(saveButton);

        JButton updateButton = new JButton("수정하기");
        updateButton.setBounds(600, 100, 130, 20);
        updateButton.setFont(new Font("돋움", Font.BOLD, 18));
        updateButton.setBackground(new Color(255, 255, 255));
        contentPane.add(updateButton);
        updateButton.addActionListener(e -> updatePlan());

        JButton deleteButton = new JButton("삭제하기");
        deleteButton.setBounds(735, 100, 130, 20);
        deleteButton.setFont(new Font("돋움", Font.BOLD, 18));
        deleteButton.setBackground(new Color(255, 255, 255));
        contentPane.add(deleteButton);
        deleteButton.addActionListener(e -> deletePlan());

        JLabel myplan = new JLabel("myplan");
        myplan.setBounds(700, 55, 100, 20);
        JLabel login = new JLabel("로그인");
        login.setBounds(762, 55, 100, 20);
        JLabel join = new JLabel("회원가입");
        join.setBounds(814, 55, 100, 20);
        contentPane.add(myplan);
        contentPane.add(login);
        contentPane.add(join);

        JLabel title = new JLabel("제목 : ");
        title.setBounds(138, 220, 210, 20);
        contentPane.add(title);

        this.planTitle = new JTextField();
        this.planTitle.setBounds(178, 220, 200, 23);
        contentPane.add(this.planTitle);

        Integer[] choices1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        this.section1 = new JComboBox<>(choices1);
        this.section1.setBounds(395, 220, 50, 23);
        this.section1.setBackground(new Color(255, 255, 255));
        contentPane.add(this.section1);

        JLabel day1 = new JLabel("박");
        day1.setBounds(450, 220, 30, 20);
        contentPane.add(day1);

        this.section2 = new JComboBox<>();
        this.section2.setBounds(475, 220, 50, 23);
        this.section2.setBackground(new Color(255, 255, 255));
        contentPane.add(this.section2);

        JLabel day2 = new JLabel("일");
        day2.setBounds(530, 220, 30, 20);
        contentPane.add(day2);

        areaCodeComboBox = new JComboBox<>();
        areaCodeComboBox.setBounds(138, 250, 120, 23);
        areaCodeComboBox.addActionListener(e -> updateSigunguComboBox());
        areaCodeComboBox.setBackground(new Color(255, 255, 255));
        contentPane.add(areaCodeComboBox);

        sigunguComboBox = new JComboBox<>();
        sigunguComboBox.setBounds(268, 250, 120, 23);
        sigunguComboBox.setBackground(new Color(255, 255, 255));
        contentPane.add(sigunguComboBox);

        populateAreaCodeComboBox();

        this.section1.addActionListener(e -> updateDays(section1, section2));
        this.section2.addActionListener(e -> updatePlanPanel(section2));

        JButton addAccommodationButton = new JButton("숙소 추가");
        addAccommodationButton.setBounds(425, 250, 100, 23);
        addAccommodationButton.setBackground(new Color(255, 255, 255));
        addAccommodationButton.addActionListener(e -> openAccommodationPopup());
        contentPane.add(addAccommodationButton);

        this.planPanel = new JPanel();
        this.planPanel.setLayout(new BoxLayout(planPanel, BoxLayout.Y_AXIS));
        this.planPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(planPanel);
        scrollPane.setBounds(138, 280, 408, 228);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scrollPane);

        this.section1.setSelectedIndex(0);

        myPanel = new MyPanel();
        myPanel.setBounds(0, 0, 1000, 600);
        contentPane.add(myPanel);

        saveButton.addActionListener(e -> {
            this.planService = ApplicationContextProvider.getContext().getBean(PlanService.class);
            PlanDTO planDTO = new PlanDTO();
            planDTO.setTitle(planTitle.getText());
            planDTO.setNights((Integer) section1.getSelectedItem());
            planDTO.setDays((Integer) section2.getSelectedItem());
            planDTO.setAccommodation((String) areaCodeComboBox.getSelectedItem());

            // 날짜별 데이터 설정
            planDTO.setAccommodationsPerDay(new HashMap<>(accommodationsPerDay));
            planDTO.setPlacesPerDay(new HashMap<>(placesPerDay));

            planService.createPlan(planDTO);
            JOptionPane.showMessageDialog(this, "계획이 성공적으로 저장되었습니다.");
            new UploadpageFrame();
            dispose();
        });



        setVisible(true);
    }

    private void updatePlan() {
        if (planDTO != null) {
            planDTO.setTitle(planTitle.getText());
            planDTO.setNights((Integer) section1.getSelectedItem());
            planDTO.setDays((Integer) section2.getSelectedItem());
            planDTO.setAccommodation((String) areaCodeComboBox.getSelectedItem());
            planDTO.setAccommodationsPerDay(new HashMap<>(accommodationsPerDay)); // 날짜별 숙소 업데이트
            planDTO.setPlacesPerDay(new HashMap<>(placesPerDay)); // 날짜별 장소 업데이트

            this.planService = ApplicationContextProvider.getContext().getBean(PlanService.class);
            planService.updatePlan(planDTO.getId(), planDTO);

            JOptionPane.showMessageDialog(this, "계획이 수정되었습니다.");
            new UploadpageFrame();
            dispose();
        }
    }


    private void deletePlan() {
        if (planDTO != null) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "정말 삭제하시겠습니까?",
                    "삭제 확인",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                this.planService = ApplicationContextProvider.getContext().getBean(PlanService.class);
                boolean isDeleted = planService.deletePlan(planDTO.getId());

                if (isDeleted) {
                    JOptionPane.showMessageDialog(this, "계획이 삭제되었습니다.");
                } else {
                    JOptionPane.showMessageDialog(this, "삭제할 계획을 찾을 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }

                new UploadpageFrame();
                dispose();
            }
        }
    }

    private void openAccommodationPopup() {
        AccommodationPopup accommodationPopup = new AccommodationPopup(this);
        accommodationPopup.setVisible(true);
    }

    public void addAccommodationToSchedule(String accommodationName, double latitude, double longitude) {
        this.accommodationName = accommodationName;
        this.accommodationLat = latitude;
        this.accommodationLon = longitude;

        for (java.awt.Component comp : planPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel dayPanel = (JPanel) comp;
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
        dayPanel.setName(dayText);

        dayPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(0, 10, 10, 0)
        ));

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
        optimizeButton.addActionListener(e -> openRouteDialog(dayText));

        labelPanel.add(optimizeButton);
        labelPanel.add(dayLabel);
        labelPanel.add(Box.createHorizontalGlue());
        labelPanel.add(optimizeButton);
        labelPanel.setAlignmentX(LEFT_ALIGNMENT);

        dayPanel.add(labelPanel);

        JLabel topAccommodationLabel = new JLabel(accommodationName != null ? accommodationName : "숙소 미지정");
        topAccommodationLabel.setFont(new Font("돋움", Font.PLAIN, 12));
        topAccommodationLabel.setForeground(Color.BLUE);
        topAccommodationLabel.setAlignmentX(LEFT_ALIGNMENT);
        topAccommodationLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 0));
        dayPanel.add(topAccommodationLabel);

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

        JButton addPlaceButton = new JButton("장소를 추가하려면 클릭하세요...");
        addPlaceButton.setFont(new Font("돋움", Font.PLAIN, 12));
        addPlaceButton.setForeground(Color.GRAY);
        addPlaceButton.setFocusPainted(false);
        addPlaceButton.setContentAreaFilled(false);
        addPlaceButton.setBorderPainted(false);
        addPlaceButton.setAlignmentX(LEFT_ALIGNMENT);
        addPlaceButton.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 0));
        addPlaceButton.addActionListener(e -> openAddPlaceDialog(dayPanel));
        dayPanel.add(addPlaceButton);

        dayPanel.putClientProperty("topAccommodationLabel", topAccommodationLabel);

        planPanel.add(dayPanel);
        planPanel.add(Box.createVerticalStrut(5));
    }

    private void openRouteDialog(String dayText) {
        Map<String, Object> dayData = getLocationsForDay(dayText);
        List<double[]> locationCoordinates = new ArrayList<>();
        List<String> placeNames = new ArrayList<>();

        List<double[]> dayCoordinates = (List<double[]>) dayData.get("locations");
        List<String> dayPlaceNames = (List<String>) dayData.get("placeNames");
        if (dayPlaceNames.isEmpty()) {
            JOptionPane.showMessageDialog(this, "경로에 추가된 장소가 없습니다. 장소를 추가해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (accommodationName != null && accommodationLat != 0 && accommodationLon != 0) {
            locationCoordinates.add(new double[]{accommodationLon, accommodationLat});
            placeNames.add(accommodationName);
        }

        locationCoordinates.addAll(dayCoordinates);
        placeNames.addAll(dayPlaceNames);

        if (accommodationName != null && accommodationLat != 0 && accommodationLon != 0) {
            locationCoordinates.add(new double[]{accommodationLon, accommodationLat});
            placeNames.add(accommodationName);
        }

        RouteDialog routeDialog = new RouteDialog(locationCoordinates, placeNames);
        routeDialog.setVisible(true);
    }

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
        result.put("locations", locations);
        result.put("placeNames", placeNames);
        return result;
    }

    private void openAddPlaceDialog(JPanel dayPanel) {
        PopupDialog dialog = new PopupDialog(this, dayPanel);
        dialog.setVisible(true);
    }

    public void addPlaceToSchedule(String placeName, JPanel dayPanel) {
        if (dayPanel.getComponentCount() > 0) {
            java.awt.Component lastComponent = dayPanel.getComponent(dayPanel.getComponentCount() - 1);
            if (lastComponent instanceof JButton && ((JButton) lastComponent).getText().equals("장소를 추가하려면 클릭하세요...")) {
                dayPanel.remove(lastComponent);
            }
        }
        String dayText = dayPanel.getName();

        placesPerDay.putIfAbsent(dayText, new ArrayList<>());
        placesPerDay.get(dayText).add(placeName);

        JPanel combinedPanel = new JPanel();
        combinedPanel.setLayout(new BoxLayout(combinedPanel, BoxLayout.Y_AXIS));
        combinedPanel.setAlignmentX(LEFT_ALIGNMENT);
        combinedPanel.setBackground(Color.WHITE);

        java.awt.Component verticalStrut = Box.createVerticalStrut(10);
        combinedPanel.add(verticalStrut);

        JPanel placePanel = new JPanel();
        placePanel.setLayout(new BoxLayout(placePanel, BoxLayout.X_AXIS));
        placePanel.setAlignmentX(LEFT_ALIGNMENT);
        placePanel.setBackground(Color.WHITE);
        placePanel.setMaximumSize(new Dimension(400, 30));

        JLabel placeLabel = new JLabel(placeName);
        placeLabel.setFont(new Font("돋움", Font.PLAIN, 12));
        placeLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        placeLabel.setAlignmentY(CENTER_ALIGNMENT);
        placeLabel.setPreferredSize(new Dimension(200, 20));
        placeLabel.setToolTipText(placeName);

        placeLabel.setText(placeName.length() > 15 ? placeName.substring(0, 15) + "..." : placeName);
        placePanel.add(placeLabel);
        placeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updateMapWithPlace(placeName);
            }
        });

        JButton upButton = new JButton("▲");
        upButton.setFont(new Font("돋움", Font.PLAIN, 13));
        upButton.setFocusPainted(false);
        upButton.setContentAreaFilled(false);
        upButton.setBorderPainted(false);
        upButton.setPreferredSize(new Dimension(50, 15));
        upButton.setAlignmentY(CENTER_ALIGNMENT);
        upButton.addActionListener(e -> movePlaceContainerUp(dayPanel, combinedPanel));

        JButton downButton = new JButton("▼");
        downButton.setFont(new Font("돋움", Font.PLAIN, 13));
        downButton.setFocusPainted(false);
        downButton.setContentAreaFilled(false);
        downButton.setBorderPainted(false);
        downButton.setPreferredSize(new Dimension(50, 15));
        downButton.setAlignmentY(CENTER_ALIGNMENT);
        downButton.addActionListener(e -> movePlaceContainerDown(dayPanel, combinedPanel));

        JButton deleteButton = new JButton("×");
        deleteButton.setFont(new Font("돋움", Font.BOLD, 14));
        deleteButton.setForeground(Color.RED);
        deleteButton.setFocusPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setBorderPainted(false);
        deleteButton.setPreferredSize(new Dimension(50, 15));
        deleteButton.setAlignmentY(CENTER_ALIGNMENT);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 15));
        deleteButton.addActionListener(e -> {
            dayPanel.remove(combinedPanel);
            dayToPlacesMap.get(dayText).remove(placeName);
            dayPanel.revalidate();
            dayPanel.repaint();
        });

        placePanel.add(Box.createHorizontalGlue());
        placePanel.add(upButton);
        placePanel.add(downButton);
        placePanel.add(deleteButton);

        combinedPanel.add(placePanel);
        dayPanel.add(combinedPanel);

        JButton addPlaceButton = new JButton("장소를 추가하려면 클릭하세요...");
        addPlaceButton.setFont(new Font("돋움", Font.PLAIN, 12));
        addPlaceButton.setForeground(Color.GRAY);
        addPlaceButton.setFocusPainted(false);
        addPlaceButton.setContentAreaFilled(false);
        addPlaceButton.setBorderPainted(false);
        addPlaceButton.setAlignmentX(LEFT_ALIGNMENT);
        addPlaceButton.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 0));
        addPlaceButton.addActionListener(e -> openAddPlaceDialog(dayPanel));
        dayPanel.add(addPlaceButton);

        dayPanel.revalidate();
        dayPanel.repaint();
    }


    private void movePlaceContainerUp(JPanel dayPanel, JPanel placeContainerPanel) {
        int index = dayPanel.getComponentZOrder(placeContainerPanel);
        if (index > 1) {
            dayPanel.remove(placeContainerPanel);
            dayPanel.add(placeContainerPanel, index - 1);
            dayPanel.revalidate();
            dayPanel.repaint();
        }
    }

    private void movePlaceContainerDown(JPanel dayPanel, JPanel placeContainerPanel) {
        int index = dayPanel.getComponentZOrder(placeContainerPanel);
        if (index < dayPanel.getComponentCount() - 2) {
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

            JSONObject jsonObject = new JSONObject(response.toString());
            JSONObject body = jsonObject.getJSONObject("response").getJSONObject("body");
            JSONArray items = body.getJSONObject("items").getJSONArray("item");

            if (items.length() > 0) {
                return items.getJSONObject(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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
                    int areaCode = area.getInt("code");
                    areaCodeComboBox.addItem(areaName);
                    areaCodeMap.put(areaName, areaCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getAreaCode(String areaName) {
        return areaCodeMap.getOrDefault(areaName, 1);
    }

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

    public int getSelectedAreaCode() {
        String selectedArea = (String) areaCodeComboBox.getSelectedItem();
        int areaCode = getAreaCode(selectedArea);
        return areaCode;
    }

    public String getSelectedSigunguCode() {
        String selectedSigungu = (String) sigunguComboBox.getSelectedItem();
        if (selectedSigungu == null || selectedSigungu.equals("시군구 선택")) {
            return "";
        }
        String sigunguCode = String.valueOf(sigunguCodeMap.getOrDefault(selectedSigungu, 0));
        return sigunguCode;
    }

    public static void main(String[] args) {
        new PlanwritepageFrame();
    }
}