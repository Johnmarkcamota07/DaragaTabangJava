package com.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import com.utils.ChatManager;
import com.utils.haystackManager;

public class ChatWindow extends JDialog {

    private final int ticketID;
    private final String currentUser;
    private final ChatManager chatManager;
    
    private final JTextArea chatArea;
    private final JTextField inputField;
    private Timer refreshTimer;

    public ChatWindow(Frame owner, int ticketID, String userFullName) {
        super(owner, "Chat - Ticket #" + ticketID, false); 
        this.ticketID = ticketID;
        this.currentUser = userFullName;
        this.chatManager = new ChatManager();

        setSize(400, 500);
        setLocationRelativeTo(owner); 
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240)); // Light gray background
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Loading...");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        JTextArea descLabel = new JTextArea("Loading description...");
        descLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        descLabel.setWrapStyleWord(true);
        descLabel.setLineWrap(true);
        descLabel.setEditable(false);
        descLabel.setOpaque(false); // Make it blend with background
        descLabel.setBorder(new EmptyBorder(5, 0, 5, 0));

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(descLabel, BorderLayout.CENTER);
        
        // Add Header to the very top of the window
        add(headerPanel, BorderLayout.NORTH);

        loadTicketDetails(ticketID, titleLabel, descLabel);
        //CHAT DATA
        chatArea = new JTextArea();
        chatArea.setEditable(false); // Read only
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatArea.setMargin(new Insets(10, 10, 10, 10)); 
        
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        JButton sendButton = new JButton("Send");

        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMsg());
        inputField.addActionListener(e -> sendMsg()); // Allow pressing "Enter" key

        // ---  AUTO-REFRESH TIMER ---
        // Checks for new messages every 2 seconds
        refreshTimer = new Timer(2000, e -> loadChats());
        refreshTimer.start();

        loadChats();
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                refreshTimer.stop();
            }
        });
    }

    private void loadTicketDetails(int id, JLabel titleLbl, JTextArea descLbl) {
        haystackManager manager = new haystackManager();
        String rawData = manager.getLatestTicket(String.valueOf(id));

        if (rawData != null && !rawData.startsWith("Error")) {
            String[] parts = rawData.split("\\|");
            if (parts.length >= 8) {
                titleLbl.setText("Ticket #" + parts[0] + ": " + parts[2]); // Title
                descLbl.setText(parts[7]); 
            }
        } else {
            titleLbl.setText("Ticket #" + id);
            descLbl.setText("Could not load description.");
        }
    }
    private void sendMsg() {
        String msg = inputField.getText().trim();
        if (!msg.isEmpty()) {
            chatManager.sendMessage(ticketID, currentUser, msg);
            inputField.setText("");
            loadChats();
        }
    }

    private void loadChats() {
        String history = chatManager.getChatHistory(ticketID);
        
        if (!chatArea.getText().equals(history)) {
            chatArea.setText(history);

            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        }
    }
}