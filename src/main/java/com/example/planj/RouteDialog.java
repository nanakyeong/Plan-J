package com.example.planj;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RouteDialog extends JFrame {
    private static final String API_KEY = "74c57db65fbd1377d25b3f8093772aaa";
    private List<double[]> locations;
    private List<String> placeNames; // 장소 이름 리스트 추가

    public RouteDialog(List<double[]> locations, List<String> placeNames) {
        this.locations = locations;
        this.placeNames = placeNames;
        setTitle("Optimized Route Display");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 장소 리스트 패널 설정
        JPanel placeListPanel = new JPanel(new BorderLayout());
        placeListPanel.setPreferredSize(new Dimension(200, 600));
        placeListPanel.setBackground(Color.WHITE);

        JLabel placeListLabel = new JLabel("장소 리스트", SwingConstants.CENTER);
        placeListPanel.add(placeListLabel, BorderLayout.NORTH);

        // 장소 리스트 표시용 JTextArea
        JTextArea placeListTextArea = new JTextArea();
        placeListTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(placeListTextArea);
        placeListPanel.add(scrollPane, BorderLayout.CENTER);

        // JTextArea에 장소 이름을 추가하여 표시
        for (int i = 0; i < locations.size(); i++) {
            String placeName = placeNames.get(i);
            placeListTextArea.append(placeName + "\n");
        }

        add(placeListPanel, BorderLayout.WEST); // 왼쪽에 장소 리스트 패널 추가

        // 지도 패널 설정
        JFXPanel fxPanel = new JFXPanel();
        add(fxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.load(getClass().getResource("/Routemap.html").toExternalForm());

            JSONArray optimizedPaths = getOptimizedRoute(locations);
            JSONArray markers = new JSONArray();
            double[] centerCoordinates = calculateCenter(locations);
            locations.forEach(loc -> {
                JSONObject marker = new JSONObject();
                marker.put("x", loc[0]);
                marker.put("y", loc[1]);
                markers.put(marker);
            });

            webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                    webEngine.executeScript("displayRoute('" + optimizedPaths.toString() + "', '" + markers.toString() + "', "
                            + centerCoordinates[1] + ", " + centerCoordinates[0] + ");");
                }
            });

            fxPanel.setScene(new Scene(webView));
        });
    }

    private double[] calculateCenter(List<double[]> locations) {
        double sumX = 0;
        double sumY = 0;
        for (double[] loc : locations) {
            sumX += loc[0];
            sumY += loc[1];
        }
        double centerX = sumX / locations.size();
        double centerY = sumY / locations.size();
        return new double[]{centerX, centerY};
    }

    private JSONArray getOptimizedRoute(List<double[]> locations) {
        List<double[]> optimizedOrder = optimizeRoute(locations); // 최적화된 순서로 경로 계산
        JSONArray pathSegments = new JSONArray();

        for (int i = 0; i < optimizedOrder.size() - 1; i++) {
            double[] start = optimizedOrder.get(i);
            double[] end = optimizedOrder.get(i + 1);

            try {
                String urlString = String.format(
                        "https://apis-navi.kakaomobility.com/v1/directions?origin=%f,%f&destination=%f,%f&priority=RECOMMEND",
                        start[0], start[1], end[0], end[1]
                );

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "KakaoAK " + API_KEY);

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.has("routes")) {
                    JSONArray routes = jsonResponse.getJSONArray("routes");
                    if (routes.length() > 0) {
                        JSONObject firstRoute = routes.getJSONObject(0);
                        if (firstRoute.has("sections")) {
                            JSONArray roads = firstRoute.getJSONArray("sections")
                                    .getJSONObject(0).getJSONArray("roads");

                            pathSegments.put(roads); // 각 도로 세그먼트를 경로에 추가
                        } else {
                            System.out.println("Error: 'sections' not found in the route data.");
                        }
                    }
                } else {
                    System.out.println("Error: 'routes' not found in the API response.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pathSegments;
    }



    private List<double[]> optimizeRoute(List<double[]> locations) {
        List<double[]> optimizedOrder = new ArrayList<>();

        // 시작과 끝은 숙소로 고정
        double[] startAccommodation = locations.get(0);
        double[] endAccommodation = locations.get(locations.size() - 1);

        optimizedOrder.add(startAccommodation); // 시작 숙소 추가

        // 중간 지점들만 추출하여 최적화
        List<double[]> intermediateLocations = new ArrayList<>(locations.subList(1, locations.size() - 1));
        boolean[] visited = new boolean[intermediateLocations.size()];

        // 중간 지점들 최적화하여 추가
        for (int i = 0; i < intermediateLocations.size(); i++) {
            double[] currentLocation = optimizedOrder.get(i);
            double minDistance = Double.MAX_VALUE;
            int nextIndex = -1;

            for (int j = 0; j < intermediateLocations.size(); j++) {
                if (!visited[j]) {
                    double distance = calculateDistance(currentLocation, intermediateLocations.get(j));
                    if (distance < minDistance) {
                        minDistance = distance;
                        nextIndex = j;
                    }
                }
            }

            if (nextIndex != -1) {
                visited[nextIndex] = true;
                optimizedOrder.add(intermediateLocations.get(nextIndex));
            }
        }

        optimizedOrder.add(endAccommodation); // 끝 숙소 추가

        return optimizedOrder;
    }



    private double calculateDistance(double[] loc1, double[] loc2) {
        return Math.sqrt(Math.pow(loc1[0] - loc2[0], 2) + Math.pow(loc1[1] - loc2[1], 2));
    }

}
