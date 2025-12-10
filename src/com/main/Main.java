package com.main;

import com.UI.LoginWindow;
import java.awt.*;
import javax.swing.*;

public class Main extends JFrame{
    public Main(){
        JFrame frame = new JFrame("DaragaTabang");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); 
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.decode("#ffffff")); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        
        gbc.gridy = 0;
        JLabel title = new JLabel("DaragaTabang");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.decode("#0A192F")); // Navy Blue
        panel.add(title, gbc);
        
        gbc.gridy = 1;
        JLabel subtitle = new JLabel("Official Ticketing & Support System");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        panel.add(subtitle, gbc);

        gbc.gridy = 2;
        JButton btnEnter = new JButton("Login / Sign Up");
        btnEnter.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEnter.setPreferredSize(new Dimension(200, 45));
        btnEnter.setBackground(Color.decode("#0A192F"));
        btnEnter.setForeground(Color.WHITE);
        btnEnter.setFocusPainted(false);
        
        panel.add(btnEnter, gbc);

        btnEnter.addActionListener(e -> {
            LoginWindow loginWindow = new LoginWindow(); 
            loginWindow.setVisible(true);
            frame.dispose();
        });

        frame.add(panel);
        frame.setVisible(true);
    }
    public static void main (String[] args) {
        new Main().setVisible(true);
    }
}

