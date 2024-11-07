package com.example.planj;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


public class PopupDialog extends JDialog {
    private static final String SERVICE_KEY = "pRHMKrAJfJJZTC104XWkGvOIvKtKcO6zFysOGGDrH3Bo%2FktklWp6urJAiA5DoWSY3rf7LEKeb2NU5aDiAfDhlw%3D%3D"; // TourAPI 서비스 키를 입력하세요
    private static final Map<String, String> titleToContentIdMap = new HashMap<>(); // 제목과 contentId 매핑
    private JPanel mainPanel;
    private String selectedPlace;
    private JPanel foodPanel = createCategoryPanel("음식점", "39");
    private double latitude;
    private double longitude;
    private WebView webView; // WebView를 인스턴스 변수로 설정하여 다른 메서드에서 접근 가능

    public PopupDialog(JFrame parent, JPanel dayPanel) {
        super(parent, "장소 추가", true);
        setSize(900, 400);
        setLayout(new BorderLayout());

        JFXPanel fxPanel = new JFXPanel();
        fxPanel.setPreferredSize(new Dimension(294, 400));

        Platform.runLater(() -> {
            // WebView 인스턴스를 초기화합니다.
            webView = new WebView();
            webView.getEngine().load(getClass().getResource("/map.html").toExternalForm());
            fxPanel.setScene(new Scene(webView));
        });

        // rightPanel에 fxPanel 추가
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(fxPanel);
        add(rightPanel, BorderLayout.EAST);


        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        JButton sightseeingButton = new JButton("관광지");
        JButton foodButton = new JButton("음식점");
        JButton cafeButton = new JButton("카페");

        topPanel.add(sightseeingButton);
        topPanel.add(foodButton);
        topPanel.add(cafeButton);
        add(topPanel, BorderLayout.NORTH);

        mainPanel = new JPanel(new CardLayout());
        JPanel sightseeingPanel = createCategoryPanel("관광지", "12");

        JPanel cafePanel = createCategoryPanel("카페", "39");

        mainPanel.add(sightseeingPanel, "관광지");
        mainPanel.add(foodPanel, "음식점");
        mainPanel.add(cafePanel, "카페");

        add(mainPanel, BorderLayout.CENTER);

        sightseeingButton.addActionListener(e -> loadCategoryData(mainPanel, "관광지", sightseeingPanel, "12", "A01", "A0101", ""));
        foodButton.addActionListener(e -> showFoodCategories()); // 음식점 버튼 클릭 시 카테고리 선택 창 표시
        cafeButton.addActionListener(e -> loadCategoryData(mainPanel, "카페", cafePanel, "39", "A05", "A0502", "A05020900"));

        JPanel bottomPanel = new JPanel();
        JButton addButton = new JButton("추가");
        JButton closeButton = new JButton("닫기");

        // PopupDialog 클래스에서 addButton 클릭 리스너
        addButton.addActionListener(e -> {
            selectedPlace = getSelectedPlace();
            if (selectedPlace != null) {
                ((PlanwritepageFrame) parent).addPlaceToSchedule(selectedPlace, dayPanel); // 메인 창 일정에 추가
                setVisible(false); // 팝업 창 닫기
            } else {
                JOptionPane.showMessageDialog(this, "추가할 장소를 선택하세요.");
            }
        });

        closeButton.addActionListener(e -> dispose());
        bottomPanel.add(addButton);
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(parent);
    }

    private void showFoodCategories() {
        JDialog foodDialog = new JDialog(this, "음식점 카테고리 선택", true);
        foodDialog.setSize(300, 200);
        foodDialog.setLayout(new GridLayout(4, 1));

        JButton koreanFoodButton = new JButton("한식");
        JButton westernFoodButton = new JButton("서양식");
        JButton japaneseFoodButton = new JButton("일식");
        JButton chineseFoodButton = new JButton("중식");

        koreanFoodButton.addActionListener(e -> {
            loadCategoryData(mainPanel, "음식점", foodPanel, "39", "A05", "A0502", "A05020100");
            foodDialog.dispose();
        });
        westernFoodButton.addActionListener(e -> {
            loadCategoryData(mainPanel, "음식점", foodPanel, "39", "A05", "A0502", "A05020200");
            foodDialog.dispose();
        });
        japaneseFoodButton.addActionListener(e -> {
            loadCategoryData(mainPanel, "음식점", foodPanel, "39", "A05", "A0502", "A05020300");
            foodDialog.dispose();
        });
        chineseFoodButton.addActionListener(e -> {
            loadCategoryData(mainPanel, "음식점", foodPanel, "39", "A05", "A0502", "A05020400");
            foodDialog.dispose();
        });

        foodDialog.add(koreanFoodButton);
        foodDialog.add(westernFoodButton);
        foodDialog.add(japaneseFoodButton);
        foodDialog.add(chineseFoodButton);

        foodDialog.setLocationRelativeTo(this);
        foodDialog.setVisible(true);
    }

    private JPanel createCategoryPanel(String categoryName, String contentTypeId) {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> placeList = new JList<>(listModel);

        JScrollPane listScrollPane = new JScrollPane(placeList);
        listScrollPane.setPreferredSize(new Dimension(200, 300));
        panel.add(listScrollPane, BorderLayout.WEST);

        // infoLabel을 JLabel로 생성하고 스크롤 가능하도록 JScrollPane으로 감싸기
        JLabel infoLabel = new JLabel("선택한 " + categoryName + " 정보", SwingConstants.CENTER);
        infoLabel.setVerticalAlignment(SwingConstants.TOP);

        // infoLabel을 감싼 JScrollPane 생성
        JScrollPane infoScrollPane = new JScrollPane(infoLabel);
        infoScrollPane.setPreferredSize(new Dimension(400, 300));
        infoScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        infoScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(infoScrollPane, BorderLayout.CENTER);

        // 선택된 장소 정보를 가져오는 코드에서 setCoordinatesAndDisplayMap 호출
        placeList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedItem = placeList.getSelectedValue();
                if (selectedItem != null) {
                    String contentId = titleToContentIdMap.get(selectedItem);
                    if (contentId != null) {
                        JSONObject item = fetchDetailItem(contentId, contentTypeId); // 장소 세부 정보 가져오기
                        infoLabel.setText(fetchDetailInfo(contentId, contentTypeId)); // 기존 정보 표시
                        setCoordinatesAndDisplayMap(item); // 지도 업데이트
                    }
                }
            }
        });

        return panel;
    }

    private void loadCategoryData(JPanel mainPanel, String category, JPanel targetPanel, String contentTypeId, String cat1, String cat2, String cat3) {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, category);

        PlanwritepageFrame parentFrame = (PlanwritepageFrame) getParent();
        int areaCode = parentFrame.getSelectedAreaCode(); // 선택한 areaCode 가져오기
        String sigunguCode = parentFrame.getSelectedSigunguCode(); // 선택한 sigunguCode 가져오기

        String url = generateAPIUrl(contentTypeId, cat1, cat2, cat3, areaCode, sigunguCode);
        String jsonResponse = requestAPIData(url);

        if (jsonResponse != null) {
            updateListWithAPIData(targetPanel, jsonResponse);
        }
    }

    private static String generateAPIUrl(String contentTypeId, String cat1, String cat2, String cat3, int areaCode, String sigunguCode) {
        String url = "http://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey=" + SERVICE_KEY
                + "&numOfRows=500&pageNo=1&MobileOS=ETC&MobileApp=TestApp&_type=json&contentTypeId=" + contentTypeId
                + "&areaCode=" + areaCode
                + "&cat1=" + cat1 + "&cat2=" + cat2 + "&cat3=" + cat3;

        if (!sigunguCode.isEmpty()) {
            url += "&sigunguCode=" + sigunguCode;
        }

        System.out.println("Generated URL: " + url); // URL 확인용 로그
        return url;
    }


    private static String requestAPIData(String urlString) {
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
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void updateListWithAPIData(JPanel targetPanel, String jsonResponse) {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject response = jsonObject.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONObject items = body.getJSONObject("items");
            JSONArray itemArray = items.getJSONArray("item");

            for (int i = 0; i < itemArray.length(); i++) {
                JSONObject item = itemArray.getJSONObject(i);
                String title = item.getString("title");
                String contentId = item.getString("contentid");
                listModel.addElement(title);
                titleToContentIdMap.put(title, contentId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane listScrollPane = (JScrollPane) targetPanel.getComponent(0);
        JList<String> placeListInScrollPane = (JList<String>) listScrollPane.getViewport().getView();
        placeListInScrollPane.setModel(listModel);
    }

    private static String fetchDetailInfo(String contentId, String contentTypeId) {
        String urlString = "http://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=" + SERVICE_KEY
                + "&MobileOS=ETC&MobileApp=AppTest&_type=json&contentId=" + contentId + "&contentTypeId=" + contentTypeId +
                "&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&numOfRows=10&pageNo=1";
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

            JSONObject jsonObject = new JSONObject(response.toString());
            JSONObject item = jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item").getJSONObject(0);

            return "<html><div style='padding:15px; width:250px; word-wrap:break-word;'>"
                    + (item.optString("firstimage", null) != null ?
                    "<img src='" + item.optString("firstimage") + "' width='280' height='210'><br><br>" : "")
                    + "<h2>" + item.optString("title", "제목 없음") + "</h2>"
                    + "<p>주소: " + item.optString("addr1", "주소 없음") + "</p>"
                    + "<p style='margin-top:10px;'>" + item.optString("overview", "정보 없음") + "</p>"
                    + "</div></html>";

        } catch (Exception e) {
            e.printStackTrace();
            return "정보를 불러오지 못했습니다.";
        }
    }

    private String getSelectedPlace() {
        for (Component comp : mainPanel.getComponents()) {
            if (comp.isVisible() && comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                JScrollPane scrollPane = (JScrollPane) panel.getComponent(0);
                JList<?> list = (JList<?>) scrollPane.getViewport().getView();
                return (String) list.getSelectedValue();
            }
        }
        return null;
    }

    // 선택된 장소의 좌표를 업데이트하고 지도에 마커를 표시
    // WebView에 지도 위치 설정 함수 호출
    private void updateMapMarker() {
        Platform.runLater(() -> {
            WebEngine webEngine = webView.getEngine();
            webEngine.executeScript("setMapPosition(" + latitude + ", " + longitude + ");");
        });
    }


    // 좌표를 설정하고 지도 업데이트
    private void setCoordinatesAndDisplayMap(JSONObject item) {
        try {
            longitude = item.getDouble("mapx");
            latitude = item.getDouble("mapy");
            updateMapMarker();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject fetchDetailItem(String contentId, String contentTypeId) {
        String urlString = "http://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=" + SERVICE_KEY
                + "&MobileOS=ETC&MobileApp=AppTest&_type=json&contentId=" + contentId + "&contentTypeId=" + contentTypeId +
                "&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&numOfRows=10&pageNo=1";
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

            JSONObject jsonObject = new JSONObject(response.toString());
            return jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item").getJSONObject(0);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
