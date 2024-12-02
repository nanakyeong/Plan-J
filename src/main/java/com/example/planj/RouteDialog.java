
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
    private List<LocationData> locations;

    public RouteDialog(List<double[]> coords, List<String> names) {
        this.locations = createLocationDataList(coords, names);

        setTitle("Optimized Route Display");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel placeListPanel = new JPanel(new BorderLayout());
        placeListPanel.setPreferredSize(new Dimension(200, 600));
        placeListPanel.setBackground(Color.WHITE);

        JLabel placeListLabel = new JLabel("정렬 리스트", SwingConstants.CENTER);
        placeListPanel.add(placeListLabel, BorderLayout.NORTH);

        JTextArea placeListTextArea = new JTextArea();
        placeListTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(placeListTextArea);
        placeListPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel(new GridLayout(2, 1));
        summaryPanel.setPreferredSize(new Dimension(800, 50));
        summaryPanel.setBackground(Color.WHITE);

        JLabel distanceLabel = new JLabel("총 거리: 계산 중...");
        JLabel timeLabel = new JLabel("총 시간: 계산 중...");
        summaryPanel.add(distanceLabel);
        summaryPanel.add(timeLabel);
        add(summaryPanel, BorderLayout.SOUTH);

        List<LocationData> optimizedOrder = optimizeRoute(locations);

        placeListTextArea.setText("");
        for (LocationData location : optimizedOrder) {
            placeListTextArea.append(location.name + "\n");
        }

        add(placeListPanel, BorderLayout.WEST);

        JFXPanel fxPanel = new JFXPanel();
        add(fxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.load(getClass().getResource("/Routemap.html").toExternalForm());

            JSONArray optimizedPaths = getOptimizedRoute(optimizedOrder);
            JSONArray markers = new JSONArray();
            double[] centerCoordinates = calculateCenter(optimizedOrder);

            for (LocationData location : optimizedOrder) {
                JSONObject marker = new JSONObject();
                marker.put("x", location.coordinates[0]);
                marker.put("y", location.coordinates[1]);
                marker.put("title", location.name);
                markers.put(marker);
            }

            // 총 거리 및 시간 계산
            double[] totalDistanceAndTime = calculateTotalDistanceAndTime(optimizedPaths);

            // UI 업데이트
            distanceLabel.setText(String.format("총 거리: %.2f km", totalDistanceAndTime[0]));
            timeLabel.setText(String.format("총 시간: %.1f 분", totalDistanceAndTime[1]));

            webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                    webEngine.executeScript("displayRoute('" + optimizedPaths + "', '" + markers + "', "
                            + centerCoordinates[1] + ", " + centerCoordinates[0] + ");");
                }
            });

            fxPanel.setScene(new Scene(webView));
        });
    }


    private List<LocationData> createLocationDataList(List<double[]> coords, List<String> names) {
        List<LocationData> locationDataList = new ArrayList<>();
        for (int i = 0; i < coords.size(); i++) {
            locationDataList.add(new LocationData(coords.get(i), names.get(i)));
        }
        return locationDataList;
    }

    private double[] calculateCenter(List<LocationData> locations) {
        double sumX = 0;
        double sumY = 0;
        for (LocationData location : locations) {
            sumX += location.coordinates[0];
            sumY += location.coordinates[1];
        }
        double centerX = sumX / locations.size();
        double centerY = sumY / locations.size();
        return new double[]{centerX, centerY};
    }

    private JSONArray getOptimizedRoute(List<LocationData> optimizedOrder) {
        JSONArray pathSegments = new JSONArray();

        for (int i = 0; i < optimizedOrder.size() - 1; i++) {
            double[] start = optimizedOrder.get(i).coordinates;
            double[] end = optimizedOrder.get(i + 1).coordinates;

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
                    if (!routes.isEmpty()) {
                        JSONObject firstRoute = routes.getJSONObject(0);
                        if (firstRoute.has("sections")) {
                            JSONArray roads = firstRoute.getJSONArray("sections")
                                    .getJSONObject(0).getJSONArray("roads");

                            pathSegments.put(roads);
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

    private List<LocationData> optimizeRoute(List<LocationData> locations) {
        List<LocationData> optimizedOrder = new ArrayList<>();
        boolean[] visited = new boolean[locations.size()];
        LocationData startAccommodation = locations.get(0);
        LocationData endAccommodation = locations.get(locations.size() - 1);
        optimizedOrder.add(startAccommodation);
        visited[0] = true;
        LocationData currentLocation = startAccommodation;
        for (int i = 1; i < locations.size() - 1; i++) {
            double minDistance = Double.MAX_VALUE;
            int nextIndex = -1;
            for (int j = 1; j < locations.size() - 1; j++) {
                if (!visited[j]) {
                    double distance = calculateDistance(currentLocation.coordinates, locations.get(j).coordinates);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nextIndex = j;
                    }
                }
            }
            if (nextIndex != -1) {
                visited[nextIndex] = true;
                optimizedOrder.add(locations.get(nextIndex));
                currentLocation = locations.get(nextIndex);
            }
        }
        optimizedOrder.add(endAccommodation);
        return optimizedOrder;
    }

    private double calculateDistance(double[] loc1, double[] loc2) {
        return Math.sqrt(Math.pow(loc1[0] - loc2[0], 2) + Math.pow(loc1[1] - loc2[1], 2));
    }

    public static class LocationData {
        public double[] coordinates;
        public String name;

        public LocationData(double[] coordinates, String name) {
            this.coordinates = coordinates;
            this.name = name;
        }
    }

    private double[] calculateTotalDistanceAndTime(JSONArray pathSegments) {
        double totalDistance = 0; // 단위: m
        double totalTime = 0;     // 단위: 초

        for (int i = 0; i < pathSegments.length(); i++) {
            JSONArray segment = pathSegments.getJSONArray(i);

            for (int j = 0; j < segment.length(); j++) {
                JSONObject road = segment.getJSONObject(j);
                totalDistance += road.getDouble("distance"); // 도로의 거리(m)
                totalTime += road.getDouble("duration");    // 도로의 시간(초)
            }
        }

        // 반환값: {총 거리(km), 총 시간(분)}
        return new double[]{totalDistance / 1000.0, totalTime / 60.0};
    }

}