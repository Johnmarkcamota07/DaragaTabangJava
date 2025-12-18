/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.daragatabang;

/**
 *
 * @author John Mark
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.UI.LoginWindow;

public class DaragaTabangNet extends JFrame{
    public DaragaTabangNet(){
        super("DaragaTabang");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.decode("#ffffff"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel title = new JLabel("DaragaTabang");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.decode("#0A192F")); // Navy Blue
        add(title, gbc);
        
        gbc.gridy = 1;
        JLabel subtitle = new JLabel("Official Ticketing & Support System");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        add(subtitle, gbc);

        gbc.gridy = 2;
        JButton btnEnter = new JButton("Login / Sign Up");
        btnEnter.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEnter.setPreferredSize(new Dimension(200, 45));
        btnEnter.setBackground(Color.decode("#0A192F"));
        btnEnter.setForeground(Color.WHITE);
        btnEnter.setFocusPainted(false);
        
        add(btnEnter, gbc);

        btnEnter.addActionListener(e -> {
            LoginWindow loginWindow = new LoginWindow(); 
            loginWindow.setVisible(true);
            dispose();
        });

        setVisible(true);
    }
    public static void main (String[] args) {
        SwingUtilities.invokeLater(() -> {
            new com.utils.haystackManager().rebuildIndex();
            DaragaTabangNet app = new DaragaTabangNet();
            app.setVisible(true);
        });
    }
}

