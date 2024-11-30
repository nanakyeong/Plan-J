package com.example.planj;

import com.example.planj.db.PlanDTO;
import com.example.planj.db.PlanService;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Component
public class MainpageFrame extends JFrame {
    private final PlanService planService;
    private final JButton[] planButtons = new JButton[7];

    @Autowired
    public MainpageFrame(PlanService planService) {
        this.planService = planService;
        initialize();
    }

    private void initialize() {
        setTitle("Plan J");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

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

        JTextField search = new JTextField();
        search.setBounds(653, 142, 210, 23);
        contentPane.add(search);

        JButton btn_newplan = new JButton("+");
        btn_newplan.setBounds(123, 230, 120, 120);
        JLabel newplan = new JLabel("plan 업로드");
        newplan.setBounds(143, 350, 100, 20);
        contentPane.add(btn_newplan);
        contentPane.add(newplan);

        btn_newplan.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                UploadpageFrame uploadFrame = ApplicationContextProvider.getContext().getBean(UploadpageFrame.class);
                uploadFrame.setVisible(true);
                dispose();
            });
        });

        contentPane.add(btn_newplan);

        createPlanButtons(contentPane);
        updatePlanButtons();

        MyPanel panel1 = new MyPanel();
        panel1.setBounds(0, 0, 1000, 600);
        contentPane.add(panel1);

        setVisible(true);
    }

    private void createPlanButtons(Container contentPane) {
        for (int i = 0; i < planButtons.length; i++) {
            JButton planButton = new JButton();
            planButton.setBounds(123 + (((i+1) % 4) * 210), 230 + (((i+1) / 4) * 170), 120, 120);
            planButton.setVisible(false);
            contentPane.add(planButton);
            planButtons[i] = planButton;
        }
    }

    private void updatePlanButtons() {
        List<PlanDTO> plans = planService.getAllPlans();

        // 반복문에서 유효한 버튼 수를 초과하지 않도록 설정
        int maxButtons = Math.min(plans.size(), planButtons.length);
        for (int i = 0; i < maxButtons; i++) {
            PlanDTO plan = plans.get(i);
            JButton planButton = planButtons[i];
            planButton.setText(plan.getTitle());
            planButton.setVisible(true);

            // 버튼 클릭 시 계획 열기
            planButton.addActionListener(e -> openPlan(plan));
        }

        // 남아 있는 버튼은 숨기기
        for (int i = plans.size(); i < planButtons.length; i++) {
            planButtons[i].setVisible(false);
        }
    }



    private void openPlan(PlanDTO planDTO) {
        SwingUtilities.invokeLater(() -> {
            PlanwritepageFrame planFrame = ApplicationContextProvider.getContext().getBean(PlanwritepageFrame.class);
            planFrame.disableEditing(); // UI 비활성화
            planFrame.setPlanDTO(planDTO);
            planFrame.setVisible(true);
            dispose();
        });
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
        SwingUtilities.invokeLater(() -> {
            ApplicationContextProvider.getContext().getBean(MainpageFrame.class).setVisible(true);
        });
    }
}
