package com.UI;

import com.utils.UserManager;
import java.awt.*;
import javax.swing.*; 

public class SignUpWindow extends JFrame {

    public SignUpWindow() {
        setTitle("Sign Up");
        setSize(300, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("  Full Name:"));
        JTextField txtName = new JTextField();
        add(txtName);

        add(new JLabel("  Username:"));
        JTextField txtUser = new JTextField();
        add(txtUser);

        add(new JLabel("  Password:"));
        JPasswordField txtPass = new JPasswordField();
        add(txtPass);

        JButton btnRegister = new JButton("Register");
        add(btnRegister);
        
        JButton btnCancel = new JButton("Cancel");
        add(btnCancel);

        btnRegister.addActionListener(e -> {
            String name = txtName.getText();
            String user = txtUser.getText();
            String pass = new String(txtPass.getPassword());

            if (name.isEmpty() || user.isEmpty() || pass.isEmpty()) return;

            UserManager manager = new UserManager();
            if (manager.registerUser(user, pass, name)) {
                System.out.println("Registration Successful!");
                JOptionPane.showMessageDialog(this, "Success! Please Login.");
                dispose();
            } else {
                System.out.println("Username taken");
                JOptionPane.showMessageDialog(this, "Username taken.");
            }
        });

        btnCancel.addActionListener(e -> dispose());
        setVisible(true);
    }
}