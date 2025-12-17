package com.UI;

import java.awt.*;
import javax.swing.*;

public class UserDashboard extends JFrame {
    public UserDashboard(String name) {
        setTitle("User Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 3; gbc.gridy = 1;
        JButton createAddTicket = new JButton("Create Ticket");
        add(createAddTicket,gbc);


    }
}
