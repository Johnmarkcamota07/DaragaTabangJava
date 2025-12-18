
package com.UI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.utils.UserManager;

public class LoginWindow extends JFrame {

    public LoginWindow() {
        setTitle("Login");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        JTextField txtUser = new JTextField(15);
        add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        JPasswordField txtPass = new JPasswordField(15);
        add(txtPass, gbc);

        JPanel btnPanel = new JPanel();
        JButton btnLogin = new JButton("Login");
        JButton btnSignUp = new JButton("Sign Up");
        btnPanel.add(btnLogin);
        btnPanel.add(btnSignUp);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        btnLogin.addActionListener(e -> {
            String user = txtUser.getText();
            String pass = new String(txtPass.getPassword());

            UserManager auth = new UserManager();
            String result = auth.authenticate(user, pass); 

            if (result != null) {
                String[] data = result.split("\\|");
                String role = data[0];
                String name = data[1];

                dispose(); 
                if (role.equals("ADMIN")) {
                     new AdminDashboard(name,user).setVisible(true);
                } else {
                     new UserDashboard(name,user).setVisible(true);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Invalid Login", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSignUp.addActionListener(e -> {
            new SignUpWindow().setVisible(true);
            dispose();
        });
        
    }
}