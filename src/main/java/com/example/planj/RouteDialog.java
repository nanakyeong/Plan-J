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
            new double[]{126.2460707194, 33.3209235283},  // 시작점
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

            JSONObject routeData = generateRouteJson(optimizeRoute(locations));
            webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                    webEngine.executeScript("displayRoute('" + routeData.toString() + "');");
                }
            });

            fxPanel.setScene(new Scene(webView));
        });
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

    private JSONObject generateRouteJson(List<double[]> locations) {
        JSONObject jsonRequest = new JSONObject();
        JSONArray waypoints = new JSONArray();

        for (int i = 1; i < locations.size() - 1; i++) {
            JSONObject waypoint = new JSONObject();
            waypoint.put("x", locations.get(i)[0]);
            waypoint.put("y", locations.get(i)[1]);
            waypoints.put(waypoint);
        }

        jsonRequest.put("origin", new JSONObject().put("x", locations.get(0)[0]).put("y", locations.get(0)[1]));
        jsonRequest.put("destination", new JSONObject().put("x", locations.get(locations.size() - 1)[0]).put("y", locations.get(locations.size() - 1)[1]));
        jsonRequest.put("waypoints", waypoints);

        return jsonRequest;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RouteDialog dialog = new RouteDialog();
            dialog.setVisible(true);
        });
    }
}
