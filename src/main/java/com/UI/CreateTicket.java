package com.UI;

import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.utils.Category;
import com.utils.Priority;
import com.utils.haystackManager;
import com.utils.ticket;

public class CreateTicket extends JDialog{

    private final String currentUser; // Stores user

    private final JTextField titleField;
    private final JTextArea descriptionArea;
    private final JComboBox<Priority> priorityBox;
    private final JComboBox<Category> categoryBox;

    public CreateTicket(Frame owner,String userFullName) {
        super(owner, "Create New Ticket", true);
        this.currentUser = userFullName;

        setTitle("Create New Ticket");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- HEADER ---
        JLabel headerLabel = new JLabel("New Ticket Details");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(headerLabel, gbc);

        gbc.gridwidth = 1; gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Ticket Title:"), gbc);

        gbc.gridx = 1;
        titleField = new JTextField(20);
        add(titleField, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        Category[] categories = Category.values(); 
        categoryBox = new JComboBox<>(categories);
        add(categoryBox, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Priority:"), gbc);

        gbc.gridx = 1;
        Priority[] priorities = Priority.values();
        priorityBox = new JComboBox<>(priorities);
        add(priorityBox, gbc);

        gbc.gridy++; gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST; // Align label to top-left
        add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        descriptionArea = new JTextArea(8, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        add(scrollPane, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Submit Ticket");
        JButton cancelButton = new JButton("Cancel");

        submitButton.addActionListener(e -> saveTicketAction());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, gbc);
    }

    private void saveTicketAction(){
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        
        if (title.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and Description cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String category = ((Category)categoryBox.getSelectedItem()).toString();
        String priority = ((Priority) priorityBox.getSelectedItem()).toString();

        ticket newTicket = new ticket(title, description, this.currentUser, priority, category);

        haystackManager manager = new haystackManager();
        

        boolean success = manager.saveOrUpdateTicket(newTicket);

        if (success) {
            JOptionPane.showMessageDialog(this, "Ticket Created Successfully!\nID: " + newTicket.getTicketId());
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save ticket to database.", "System Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}