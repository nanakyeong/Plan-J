package com.example.planj;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.util.HashMap;
import java.util.Map;

public class AccommodationPopup extends JDialog {
    private static final String SERVICE_KEY = "pRHMKrAJfJJZTC104XWkGvOIvKtKcO6zFysOGGDrH3Bo%2FktklWp6urJAiA5DoWSY3rf7LEKeb2NU5aDiAfDhlw%3D%3D";
    private static final Map<String, String> titleToContentIdMap = new HashMap<>();
    private static final Map<String, double[]> titleToCoordinatesMap = new HashMap<>();
    private JPanel mainPanel;
    private String selectedAccommodation;
    private double latitude;
    private double longitude;
    private WebView webView;
    private PlanwritepageFrame parentFrame;

    public AccommodationPopup(PlanwritepageFrame parent) {
        super(parent, "숙소 추가", true);
        this.parentFrame = parent;
        setSize(900, 400);
        setLayout(new BorderLayout());

        JFXPanel fxPanel = new JFXPanel();
        fxPanel.setPreferredSize(new Dimension(294, 400));

        Platform.runLater(() -> {
            webView = new WebView();
            webView.getEngine().load(getClass().getResource("/map.html").toExternalForm());
            fxPanel.setScene(new Scene(webView));
        });

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(fxPanel);
        add(rightPanel, BorderLayout.EAST);

        mainPanel = new JPanel(new BorderLayout());
        JPanel accommodationPanel = createAccommodationPanel();
        mainPanel.add(accommodationPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton addButton = new JButton("추가");
        JButton closeButton = new JButton("닫기");

        addButton.addActionListener(e -> {
            selectedAccommodation = getSelectedAccommodation();
            if (selectedAccommodation != null) {
                double[] coordinates = titleToCoordinatesMap.getOrDefault(selectedAccommodation, new double[]{0.0, 0.0});
                parentFrame.addAccommodationToSchedule(selectedAccommodation, coordinates[0], coordinates[1]);
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "추가할 숙소를 선택하세요.");
            }
        });

        closeButton.addActionListener(e -> dispose());
        bottomPanel.add(addButton);
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(parent);

        loadAccommodationData((DefaultListModel<String>) ((JList<String>) ((JScrollPane) accommodationPanel.getComponent(0)).getViewport().getView()).getModel());
    }

    private JPanel createAccommodationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> placeList = new JList<>(listModel);

        JScrollPane listScrollPane = new JScrollPane(placeList);
        listScrollPane.setPreferredSize(new Dimension(200, 300));
        panel.add(listScrollPane, BorderLayout.WEST);

        JLabel infoLabel = new JLabel("숙소 정보", SwingConstants.CENTER);
        infoLabel.setVerticalAlignment(SwingConstants.TOP);

        JScrollPane infoScrollPane = new JScrollPane(infoLabel);
        infoScrollPane.setPreferredSize(new Dimension(400, 300));
        infoScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        infoScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(infoScrollPane, BorderLayout.CENTER);

        placeList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedItem = placeList.getSelectedValue();
                if (selectedItem != null) {
                    String contentId = titleToContentIdMap.get(selectedItem);
                    if (contentId != null) {
                        JSONObject item = fetchDetailItem(contentId, "32");
                        infoLabel.setText(fetchDetailInfo(contentId, "32"));
                        setCoordinatesAndDisplayMap(item);
                    }
                }
            }
        });

        return panel;
    }

    private void loadAccommodationData(DefaultListModel<String> listModel) {
        int areaCode = parentFrame.getSelectedAreaCode();
        String sigunguCode = parentFrame.getSelectedSigunguCode();
        String url = generateAPIUrl(areaCode, sigunguCode);
        String jsonResponse = requestAPIData(url);

        if (jsonResponse != null) {
            updateListWithAPIData(listModel, jsonResponse);
        }
    }

    private String generateAPIUrl(int areaCode, String sigunguCode) {
        String url = "http://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey=" + SERVICE_KEY
                + "&numOfRows=500&pageNo=1&MobileOS=ETC&MobileApp=TestApp&_type=json&contentTypeId=32&areaCode=" + areaCode;

        if (!sigunguCode.isEmpty()) {
            url += "&sigunguCode=" + sigunguCode;
        }

        return url;
    }

    private String requestAPIData(String urlString) {
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

    private void updateListWithAPIData(DefaultListModel<String> listModel, String jsonResponse) {
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

                double latitude = item.optDouble("mapy", 0.0);
                double longitude = item.optDouble("mapx", 0.0);

                listModel.addElement(title);
                titleToContentIdMap.put(title, contentId);
                titleToCoordinatesMap.put(title, new double[]{latitude, longitude});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String fetchDetailInfo(String contentId, String contentTypeId) {
        String urlString = "http://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=" + SERVICE_KEY
                + "&MobileOS=ETC&MobileApp=AppTest&_type=json&contentId=" + contentId + "&contentTypeId=" + contentTypeId
                + "&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&numOfRows=10&pageNo=1";
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

    private String getSelectedAccommodation() {
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

    private void updateMapMarker() {
        Platform.runLater(() -> {
            WebEngine webEngine = webView.getEngine();
            webEngine.executeScript("setMapPosition(" + latitude + ", " + longitude + ");");
        });
    }

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
                + "&MobileOS=ETC&MobileApp=AppTest&_type=json&contentId=" + contentId + "&contentTypeId=" + contentTypeId
                + "&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&numOfRows=10&pageNo=1";
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
