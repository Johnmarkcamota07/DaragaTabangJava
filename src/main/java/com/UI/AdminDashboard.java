package com.UI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.daragatabang.DaragaTabangNet;

public class AdminDashboard extends JFrame {
    public AdminDashboard(String name , String user) {
        setTitle("Admin Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String uppername = name.toUpperCase();

         setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel greetings = new JLabel("Welcome " + uppername + "!");
        greetings.setFont(new java.awt.Font("SegoeUI", java.awt.Font.BOLD, 32));
        greetings.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        add(greetings,gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1; gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.CENTER;
        java.awt.Dimension buttonSize = new java.awt.Dimension(200, 40);

        gbc.gridx = 0; gbc.gridy = 1;
        JButton viewticket = new JButton("View Submitted Tickets");
        viewticket.setPreferredSize(buttonSize);
        add(viewticket,gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JButton logout = new JButton("Log Out");
        logout.setPreferredSize(buttonSize);
        add(logout,gbc);

        viewticket.addActionListener(e ->{
            ViewTicketAdmin viewTicket = new ViewTicketAdmin(name,user);
            viewTicket.setVisible(true);
            dispose();
        });

        logout.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(this,"Are you sure you want to log out?", "Confirm Action", JOptionPane.YES_NO_OPTION);
            if(response == JOptionPane.YES_OPTION)
            {
                DaragaTabangNet main = new DaragaTabangNet();
                main.setVisible(true);
                dispose();
            }

        });
    }
}