package com.example.planj;

import com.example.planj.db.PlanDTO;
import com.example.planj.db.PlanService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class UploadpageFrame extends JFrame {

    private Container contentPane;
    private JList<String> list;
    private JScrollPane sp;

    public UploadpageFrame() {
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
        newPlanButton.setBackground(new Color(255, 255, 255)); // 배경색 설정 (파란색 계열)
        contentPane.add(newPlanButton);

        newPlanButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new PlanwritepageFrame();
                dispose();
            });
        });

        list = new JList<>(planListModel);
        list.setVisibleRowCount(7); // 보여질 plan 개수
        sp = new JScrollPane(list);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setBounds(123, 270, 738, 200); // 크기와 위치 설정
        contentPane.add(sp);

        MyPanel panel1 = new MyPanel();
        panel1.setBounds(0, 0, 1000, 600);
        contentPane.add(panel1);

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
                            PlanwritepageFrame frame = new PlanwritepageFrame();
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");

        return plans.stream()
                .map(plan -> {
                    String dateString = plan.getDate() != null
                            ? plan.getDate().format(formatter)
                            : "날짜 없음";
                    return plan.getTitle() + " - " + dateString;
                })
                .collect(Collectors.toList());
    }

    class MyPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.black);
            g2.drawLine(123, 85, 740, 85);
        }
    }

    public static void main(String[] args) {
        new UploadpageFrame();
    }
}

