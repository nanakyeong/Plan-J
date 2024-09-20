package com.example.planj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class PasswordField extends JPasswordField {

    private String placeholder;
    private boolean isPlaceholder;

    public PasswordField(String placeholder) {
        super();
        this.placeholder = placeholder;
        setFont(new Font("돋움", Font.PLAIN, 14));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder());
        setEchoChar('\0'); // 비밀번호 입력 전에는 마스킹 문자를 보이지 않게 설정

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isPlaceholder) {
                    setEchoChar('•'); // 입력 중에는 마스킹 문자 표시
                    setText(""); // 플레이스홀더 제거
                    setForeground(Color.BLACK);
                    isPlaceholder = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getPassword().length == 0) {
                    setEchoChar('\0'); // 비밀번호 입력 전에는 마스킹 문자를 보이지 않게 설정
                    setForeground(Color.GRAY);
                    setText(placeholder);
                    isPlaceholder = true;
                }
            }
        });

        // 초기화: 플레이스홀더 텍스트로 설정
        setForeground(Color.GRAY);
        setText(placeholder);
        isPlaceholder = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 둥근 모서리
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(Color.GRAY);
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20); // 둥근 테두리
    }
}
