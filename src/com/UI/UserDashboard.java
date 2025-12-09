package com.UI;

import javax.swing.*;

public class UserDashboard extends JFrame {
    public UserDashboard(String name) {
        setTitle("User Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new JLabel("Welcome " + name + "! (File a complaint here)", SwingConstants.CENTER));
    }
}