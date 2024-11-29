package com.example.planj;

import com.example.planj.db.PlanDTO;
import com.example.planj.db.PlanService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UploadpageFrame extends JFrame {

    private final PlanService planService;
    private Container contentPane;
    private JList<String> list;
    private JScrollPane sp;
    private MyPanel panel1; // 지도 패널

    @Autowired
    public UploadpageFrame(PlanService planService) {
        this.planService = planService;
        initialize();
    }

    private void initialize() {
        setTitle("Plan J");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JLabel logo1 = new JLabel("Plan J");
        logo1.setFont(new Font("돋움", Font.BOLD, 35));
        logo1.setBounds(123, 135, 150, 30);
        contentPane.add(logo1);

        JLabel myplan = new JLabel("myplan");
        myplan.setBounds(700, 55, 100, 20);
        JLabel login = new JLabel("로그인");
        login.setBounds(762, 55, 100, 20);
        JLabel join = new JLabel("회원가입");
        join.setBounds(814, 55, 100, 20);
        contentPane.add(myplan);
        contentPane.add(login);
        contentPane.add(join);

        List<String> plans = getPlanTitlesFromDatabase();

        DefaultListModel<String> planListModel = new DefaultListModel<>();
        plans.forEach(planListModel::addElement);

        JButton newPlanButton = new JButton("+ New Plan");
        newPlanButton.setBounds(123, 225, 150, 40); // 위치와 크기 설정
        newPlanButton.setFont(new Font("돋움", Font.BOLD, 17)); // 글씨 크기 설정
        newPlanButton.setBackground(new Color(255, 255, 255)); // 배경색 설정
        contentPane.add(newPlanButton);

        newPlanButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                panel1.setMapVisibility(false); // 지도 숨김
                PlanwritepageFrame planFrame = new PlanwritepageFrame(planService);
                dispose();
            });
        });

        list = new JList<>(planListModel);
        list.setVisibleRowCount(7); // 보여질 plan 개수
        sp = new JScrollPane(list);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setBounds(123, 270, 738, 200); // 크기와 위치 설정
        contentPane.add(sp);

        panel1 = new MyPanel(); // 지도 패널 초기화
        panel1.setBounds(0, 0, 1000, 600);
        contentPane.add(panel1);
        panel1.setMapVisibility(false); // 초기 상태에서 지도 숨김

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // 더블 클릭 이벤트
                    int selectedIndex = list.getSelectedIndex();
                    if (selectedIndex != -1) {
                        PlanService planService = ApplicationContextProvider.getContext().getBean(PlanService.class);
                        List<PlanDTO> plans = planService.getAllPlans();
                        PlanDTO selectedPlan = plans.get(selectedIndex);

                        SwingUtilities.invokeLater(() -> {
                            panel1.setMapVisibility(true); // 지도 표시
                            PlanwritepageFrame frame = new PlanwritepageFrame(planService);

                            // PlacePerDay에서 숙소 정보 추출
                            String accommodation = selectedPlan.getPlacesPerDay().entrySet().stream()
                                    .flatMap(entry -> entry.getValue().stream())
                                    .filter(place -> place.startsWith("[숙소]"))
                                    .findFirst()
                                    .map(place -> place.replace("[숙소] ", ""))
                                    .orElse(null);

                            frame.setPlanDTO(selectedPlan);
                            frame.setVisible(true);
                            dispose();
                        });
                    }
                }
            }
        });

        setVisible(true);
    }

    private List<String> getPlanTitlesFromDatabase() {
        PlanService planService = ApplicationContextProvider.getContext().getBean(PlanService.class);
        List<PlanDTO> plans = planService.getAllPlans();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년MM월dd일");

        return plans.stream()
                .map(plan -> {
                    String dateString = plan.getDate() != null
                            ? plan.getDate().format(formatter)
                            : "날짜 없음";
                    return plan.getTitle() + " - " + dateString; // 형식: title - yyyy년MM월dd일
                })
                .collect(Collectors.toList());
    }

    class MyPanel extends JPanel {
        private JFXPanel jfxPanel;
        private WebEngine webEngine;

        public MyPanel() {
            setLayout(null);

            jfxPanel = new JFXPanel();
            jfxPanel.setBounds(565, 220, 285, 290); // 지도 위치 설정
            add(jfxPanel);

            // JavaFX 초기화
            Platform.runLater(() -> {
                WebView webView = new WebView();
                webEngine = webView.getEngine();

                URL url = getClass().getResource("/map.html");
                if (url != null) {
                    webEngine.load(url.toExternalForm());
                } else {
                    webEngine.loadContent("<html><body><h1>지도 파일을 찾을 수 없습니다.</h1></body></html>");
                }

                jfxPanel.setScene(new Scene(webView));
            });
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.black);
            g2.drawLine(123, 85, 866, 85);

        }

        public void setMapVisibility(boolean isVisible) {
            jfxPanel.setVisible(isVisible); // 지도 표시/숨김
            revalidate();
            repaint();
        }
    }
}
