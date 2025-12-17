package com.UI;

import com.daragatabang.DaragaTabangNet;
import com.utils.UserManager;
import java.awt.*;
import javax.swing.*; 

public class SignUpWindow extends JFrame {

    public SignUpWindow() {
        setTitle("Sign Up");
        setSize(300, 250);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("  Full Name:"),gbc);
        gbc.gridx = 1;
        JTextField txtName = new JTextField(15);
        add(txtName,gbc);

        gbc.gridx = 0;gbc.gridy = 1;
        add(new JLabel("  Username:"),gbc);
        gbc.gridx = 1;
        JTextField txtUser = new JTextField(15);
        add(txtUser,gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("  Password:"),gbc);
        gbc.gridx = 1;
        JPasswordField txtPass = new JPasswordField();
        add(txtPass,gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        JButton btnRegister =  new JButton("Register");
        add(btnRegister,gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.ipadx = 10; gbc.ipady = 5;
        JButton btnCancel = new JButton("Cancel");
        add(btnCancel,gbc);

        btnRegister.addActionListener(e -> {
            String name = txtName.getText();
            String user = txtUser.getText();
            String pass = new String(txtPass.getPassword());

            if (name.isEmpty() || user.isEmpty() || pass.isEmpty()) return;

            UserManager manager = new UserManager();
            if (manager.registerUser(user, pass, name)) {
                System.out.println("Registration Successful!");
                JOptionPane.showMessageDialog(this, "Success! Please Login.");
                new DaragaTabangNet().setVisible(true);
                dispose();
            } else {
                System.out.println("Username taken");
                JOptionPane.showMessageDialog(this, "Username taken.");
            }
        });

        btnCancel.addActionListener(e -> {
            new DaragaTabangNet().setVisible(true);
            dispose();
        });
        setVisible(true);
    }
}