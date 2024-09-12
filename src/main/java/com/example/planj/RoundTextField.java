package com.example.planj;

import javax.swing.*;
import java.awt.*;

public class RoundTextField extends JTextField {
    public RoundTextField(String text) {
        super(text);
        setFont(new Font("돋움", Font.PLAIN, 14));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Rounded corners
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(Color.GRAY);
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20); // Rounded border
    }
}
