package com.example.planj;

import javax.swing.*;
import java.awt.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.net.URL;

public class PlanwritepageFrame extends JFrame {

    private JPanel planPanel;

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
        section1.setBounds(385, 220, 50, 23);
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

        section1.addActionListener(e -> updateDays(section1, section2));
        section2.addActionListener(e -> updatePlanPanel(section2));

        // 일정 리스트 패널
        planPanel = new JPanel();
        planPanel.setLayout(new BoxLayout(planPanel, BoxLayout.Y_AXIS));
        planPanel.setBackground(Color.WHITE); // 흰색 배경 설정

        // JScrollPane에 planPanel 추가
        JScrollPane scrollPane = new JScrollPane(planPanel);
        scrollPane.setBounds(138, 280, 408, 228);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scrollPane);

        section1.setSelectedIndex(0);

        MyPanel panel1 = new MyPanel();
        panel1.setBounds(0, 0, 1000, 600);
        contentPane.add(panel1);

        setVisible(true);
    }

    private void updateDays(JComboBox<Integer> section1, JComboBox<Integer> section2) {
        int selectedNights = (Integer) section1.getSelectedItem();
        section2.removeAllItems();

        // 일 수는 박 수 + 1 (최소) ~ 박 수 + 2 (최대)
        int minDays = selectedNights + 1;
        int maxDays = selectedNights + 2;

        for (int i = minDays; i <= maxDays; i++) {
            section2.addItem(i);
        }
    }

    private void updatePlanPanel(JComboBox<Integer> section2) {
        planPanel.removeAll(); // 기존의 일차 패널 제거

        if (section2.getSelectedItem() != null) {
            int days = (Integer) section2.getSelectedItem();
            for (int i = 1; i <= days; i++) {
                addDayPanel(planPanel, i + "일차");
            }
        }

        planPanel.revalidate();
        planPanel.repaint();
    }

    // 각 일정 패널을 생성하는 메서드
    private void addDayPanel(JPanel planPanel, String dayText) {
        JPanel dayPanel = new JPanel();
        dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));
        dayPanel.setBackground(Color.WHITE); // 흰색 배경 설정

        // "1일차"와 같은 텍스트 라벨
        JLabel dayLabel = new JLabel(dayText);
        dayLabel.setFont(new Font("돋움", Font.BOLD, 17));
        dayLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // 위쪽에 5포인트 여백 추가
        dayLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dayPanel.add(dayLabel);

        // "장소를 추가하려면 클릭하세요..." 버튼
        JButton addPlaceButton = new JButton("장소를 추가하려면 클릭하세요...");
        addPlaceButton.setFont(new Font("돋움", Font.PLAIN, 12));
        addPlaceButton.setForeground(Color.GRAY);
        addPlaceButton.setFocusPainted(false);
        addPlaceButton.setContentAreaFilled(false);
        addPlaceButton.setBorderPainted(false);
        addPlaceButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addPlaceButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 0, 0));
        addPlaceButton.addActionListener(e -> openAddPlaceDialog(dayPanel));
        dayPanel.add(addPlaceButton);

        planPanel.add(dayPanel);
        planPanel.add(Box.createVerticalStrut(5)); // dayPanel 사이의 간격 추가
    }


    //
    //
    //
    private void openAddPlaceDialog(JPanel dayPanel) {
        // 팝업 창 생성
        JDialog dialog = new JDialog(this, "장소 추가", true);
        dialog.setSize(600, 400);
        dialog.setLayout(new BorderLayout());

        // 상단 버튼 패널 생성
        JPanel topPanel = new JPanel();
        JButton stayButton = new JButton("숙박");
        JButton sightseeingButton = new JButton("관광지");
        JButton foodButton = new JButton("음식점");

        topPanel.add(stayButton);
        topPanel.add(sightseeingButton);
        topPanel.add(foodButton);
        dialog.add(topPanel, BorderLayout.NORTH);

        // 메인 패널 (리스트와 정보 패널을 포함할 컨테이너)
        JPanel mainPanel = new JPanel(new CardLayout());

        // 각 카테고리에 대한 패널 생성
        JPanel stayPanel = createCategoryPanel("숙박 장소");
        JPanel sightseeingPanel = createCategoryPanel("관광지 장소");
        JPanel foodPanel = createCategoryPanel("음식점");

        // 메인 패널에 각 카테고리 패널 추가
        mainPanel.add(stayPanel, "숙박");
        mainPanel.add(sightseeingPanel, "관광지");
        mainPanel.add(foodPanel, "음식점");

        dialog.add(mainPanel, BorderLayout.CENTER);

        // 버튼 클릭 시 카테고리 전환
        stayButton.addActionListener(e -> showCategory(mainPanel, "숙박"));
        sightseeingButton.addActionListener(e -> showCategory(mainPanel, "관광지"));
        foodButton.addActionListener(e -> showCategory(mainPanel, "음식점"));

        // 추가 및 닫기 버튼 패널 추가
        JPanel bottomPanel = new JPanel();
        JButton addButton = new JButton("추가");
        JButton closeButton = new JButton("닫기");

        // 추가 버튼 클릭 시 선택한 장소 추가
        addButton.addActionListener(e -> {
            // 선택된 장소 이름을 가져옴
            String selectedPlace = getSelectedPlace(mainPanel);
            if (selectedPlace != null) {
                addPlaceToSchedule(selectedPlace, dayPanel); // 메인 창 일정에 추가
                dialog.dispose(); // 추가 후 팝업 닫기
            } else {
                JOptionPane.showMessageDialog(dialog, "추가할 장소를 선택하세요.");
            }
        });

        closeButton.addActionListener(e -> dialog.dispose());
        bottomPanel.add(addButton);
        bottomPanel.add(closeButton);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        // 팝업 창을 화면 중앙에 배치
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // 카테고리 패널 생성 메서드
    private JPanel createCategoryPanel(String categoryName) {
        JPanel panel = new JPanel(new BorderLayout());

        // 리스트 패널
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement(categoryName + " 1");
        listModel.addElement(categoryName + " 2");
        listModel.addElement(categoryName + " 3");
        JList<String> placeList = new JList<>(listModel);
        placeList.setName(categoryName); // 패널 이름 설정 (나중에 선택할 때 사용)

        // 리스트의 고정 크기 설정
        JScrollPane listScrollPane = new JScrollPane(placeList);
        listScrollPane.setPreferredSize(new Dimension(200, 300)); // 너비 200, 높이 300 설정
        panel.add(listScrollPane, BorderLayout.WEST);

        // 정보 패널
        JLabel infoLabel = new JLabel("선택한 " + categoryName + " 정보", SwingConstants.CENTER);
        infoLabel.setPreferredSize(new Dimension(400, 300)); // 정보 패널의 크기 고정
        panel.add(infoLabel, BorderLayout.CENTER);

        return panel;
    }

    // 선택한 카테고리 패널 표시 메서드
    private void showCategory(JPanel mainPanel, String category) {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, category);
    }

    // 선택한 장소 가져오기 메서드
    private String getSelectedPlace(JPanel mainPanel) {
        for (Component comp : mainPanel.getComponents()) {
            if (comp.isVisible() && comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component subComp : panel.getComponents()) {
                    if (subComp instanceof JScrollPane) {
                        JScrollPane scrollPane = (JScrollPane) subComp;
                        JList<?> list = (JList<?>) scrollPane.getViewport().getView();
                        return (String) list.getSelectedValue(); // 선택된 값 반환
                    }
                }
            }
        }
        return null; // 선택되지 않은 경우 null 반환
    }

    // 메인 창 일정에 장소 추가 메서드
    private void addPlaceToSchedule(String placeName, JPanel dayPanel) {
        // 기존의 "장소를 추가하려면 클릭하세요..." 버튼 제거
        if (dayPanel.getComponentCount() > 0) {
            Component lastComponent = dayPanel.getComponent(dayPanel.getComponentCount() - 1);
            if (lastComponent instanceof JButton && ((JButton) lastComponent).getText().equals("장소를 추가하려면 클릭하세요...")) {
                dayPanel.remove(lastComponent);
            }
        }

        // 장소 항목 패널 생성
        JPanel placePanel = new JPanel();
        placePanel.setLayout(new BoxLayout(placePanel, BoxLayout.X_AXIS));
        placePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        placePanel.setBackground(Color.WHITE);
        placePanel.setMaximumSize(new Dimension(400, 30));

        // 장소 이름 라벨
        JLabel placeLabel = new JLabel(placeName);
        placeLabel.setFont(new Font("돋움", Font.PLAIN, 12));
        placeLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
        placePanel.add(placeLabel);

        // 순서 변경 버튼 (위, 아래)
        JButton upButton = new JButton("▲");
        upButton.setFont(new Font("돋움", Font.PLAIN, 13));
        upButton.setFocusPainted(false);
        upButton.setContentAreaFilled(false);
        upButton.setBorderPainted(false);
        upButton.setPreferredSize(new Dimension(50, 20));
        upButton.addActionListener(e -> movePlacePanelUp(dayPanel, placePanel));

        JButton downButton = new JButton("▼");
        downButton.setFont(new Font("돋움", Font.PLAIN, 13));
        downButton.setFocusPainted(false);
        downButton.setContentAreaFilled(false);
        downButton.setBorderPainted(false);
        downButton.setPreferredSize(new Dimension(50, 20));
        downButton.addActionListener(e -> movePlacePanelDown(dayPanel, placePanel));

        // 삭제 버튼
        JButton deleteButton = new JButton("×");
        deleteButton.setFont(new Font("돋움", Font.BOLD, 14));
        deleteButton.setForeground(Color.RED);
        deleteButton.setFocusPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setBorderPainted(false);
        deleteButton.setPreferredSize(new Dimension(50, 20));
        deleteButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 15));
        deleteButton.addActionListener(e -> {
            dayPanel.remove(placePanel);
            dayPanel.revalidate();
            dayPanel.repaint();
        });

        // 버튼들을 placePanel에 추가
        placePanel.add(Box.createHorizontalGlue()); // 라벨과 버튼들 사이의 여백
        placePanel.add(upButton);
        placePanel.add(downButton);
        placePanel.add(deleteButton);

        // dayPanel에 placePanel 추가
        dayPanel.add(placePanel);

        // 새로운 "장소를 추가하려면 클릭하세요..." 버튼 추가
        JButton addPlaceButton = new JButton("장소를 추가하려면 클릭하세요...");
        addPlaceButton.setFont(new Font("돋움", Font.PLAIN, 12));
        addPlaceButton.setForeground(Color.GRAY);
        addPlaceButton.setFocusPainted(false);
        addPlaceButton.setContentAreaFilled(false);
        addPlaceButton.setBorderPainted(false);
        addPlaceButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addPlaceButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 0, 0));
        addPlaceButton.addActionListener(e -> openAddPlaceDialog(dayPanel));
        dayPanel.add(addPlaceButton);

        // 레이아웃 업데이트
        dayPanel.revalidate();
        dayPanel.repaint();
    }

    // 특정 일차(dayPanel) 내에서만 placePanel의 순서를 변경하는 메서드
    private void movePlacePanelUp(JPanel dayPanel, JPanel placePanel) {
        int index = dayPanel.getComponentZOrder(placePanel);
        // "일차" 라벨을 고정하고 장소 항목만 순서를 변경할 수 있도록, 첫 번째 위치로 제한
        if (index > 1) {
            dayPanel.remove(placePanel);
            dayPanel.add(placePanel, index - 1);
            dayPanel.revalidate();
            dayPanel.repaint();
        }
    }

    private void movePlacePanelDown(JPanel dayPanel, JPanel placePanel) {
        int index = dayPanel.getComponentZOrder(placePanel);
        // 마지막 항목으로 이동하지 않도록, 마지막 전 위치로 제한
        if (index < dayPanel.getComponentCount() - 2) {
            dayPanel.remove(placePanel);
            dayPanel.add(placePanel, index + 1);
            dayPanel.revalidate();
            dayPanel.repaint();
        }
    }



    //

    class MyPanel extends JPanel {
        private JFXPanel jfxPanel;

        public MyPanel() {
            setLayout(null);

            // JFXPanel을 생성하여 JavaFX WebView 포함
            jfxPanel = new JFXPanel();
            jfxPanel.setBounds(566, 221, 284, 288); // 지도 위치 지정
            add(jfxPanel);

            // JavaFX 스레드에서 WebView 초기화
            Platform.runLater(() -> {
                WebView webView = new WebView();
                WebEngine webEngine = webView.getEngine();

                // resources 폴더에 있는 map.html 파일을 로드
                URL url = getClass().getResource("/map.html");
                if (url != null) {
                    webEngine.load(url.toExternalForm());
                } else {
                    webEngine.loadContent("<html><body><h1>파일을 찾을 수 없습니다.</h1></body></html>");
                }

                // WebView를 JFXPanel에 설정
                jfxPanel.setScene(new Scene(webView));
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
            g2.drawRect(123, 200, 743, 330); // 전체
            g2.drawRect(565, 220, 285, 290); // 지도 위치
        }
    }

    public static void main(String[] args) {
        new PlanwritepageFrame();
    }
}
