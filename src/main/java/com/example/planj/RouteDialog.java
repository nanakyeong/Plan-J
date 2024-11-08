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
    private final List<double[]> locations = List.of(
            new double[]{126.2507039833, 33.3059197039},  // 시작점
            new double[]{126.5038611983, 33.4893700755},  // 중간점
            new double[]{126.2460707194, 33.3209235283},  // 중간점
            new double[]{126.3454537144, 33.3225554639},  // 중간점
            new double[]{126.5269445463, 33.2767800518},  // 중간점
            new double[]{126.6758204749, 33.4019047149}   // 도착점
    );

    public RouteDialog() {
        setTitle("Optimized Route Display");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
        List<double[]> optimizedOrder = optimizeRoute(locations);
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
                    JSONArray roads = jsonResponse.getJSONArray("routes").getJSONObject(0)
                            .getJSONArray("sections").getJSONObject(0).getJSONArray("roads");
                    pathSegments.put(roads);
                } else {
                    System.err.println("Error: No route found in response");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pathSegments;
    }

    private List<double[]> optimizeRoute(List<double[]> locations) {
        List<double[]> optimizedOrder = new ArrayList<>();
        boolean[] visited = new boolean[locations.size()];
        optimizedOrder.add(locations.get(0)); // 시작점을 먼저 추가
        visited[0] = true;

        for (int i = 0; i < locations.size() - 1; i++) {
            double[] currentLocation = optimizedOrder.get(i);
            double minDistance = Double.MAX_VALUE;
            int nextIndex = -1;

            for (int j = 0; j < locations.size(); j++) {
                if (!visited[j]) {
                    double distance = calculateDistance(currentLocation, locations.get(j));
                    if (distance < minDistance) {
                        minDistance = distance;
                        nextIndex = j;
                    }
                }
            }
            if (nextIndex != -1) {
                visited[nextIndex] = true;
                optimizedOrder.add(locations.get(nextIndex));
            }
        }
        return optimizedOrder;
    }

    private double calculateDistance(double[] loc1, double[] loc2) {
        return Math.sqrt(Math.pow(loc1[0] - loc2[0], 2) + Math.pow(loc1[1] - loc2[1], 2));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RouteDialog dialog = new RouteDialog();
            dialog.setVisible(true);
        });
    }
}
