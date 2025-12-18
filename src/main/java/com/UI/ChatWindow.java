package com.UI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

import com.utils.ChatManager;

public class ChatWindow extends JDialog {

    private final int ticketID;
    private final String currentUser;
    private final ChatManager chatManager;
    
    private final JTextArea chatArea;
    private final JTextField inputField;
    private Timer refreshTimer;

    public ChatWindow(Frame owner, int ticketID, String userFullName) {
        super(owner, "Chat - Ticket #" + ticketID, false); // false to allow click back to viewticketsuser
        this.ticketID = ticketID;
        this.currentUser = userFullName;
        this.chatManager = new ChatManager();

        setSize(400, 500);
        setLocationRelativeTo(owner); // Center relative to the Ticket List
        setLayout(new BorderLayout());

        //CHAT DATA
        chatArea = new JTextArea();
        chatArea.setEditable(false); // Read only
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatArea.setMargin(new Insets(10, 10, 10, 10)); // Add some padding inside
        
        // Auto-scroll logic
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        // --- 2. INPUT AREA (Bottom) ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        JButton sendButton = new JButton("Send");

        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- 3. ACTIONS ---
        sendButton.addActionListener(e -> sendMsg());
        inputField.addActionListener(e -> sendMsg()); // Allow pressing "Enter" key

        // --- 4. AUTO-REFRESH TIMER ---
        // Checks for new messages every 2 seconds
        refreshTimer = new Timer(2000, e -> loadChats());
        refreshTimer.start();

        // Load messages immediately when opening
        loadChats();
        
        // Stop the timer when window is closed (Save memory)
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                refreshTimer.stop();
            }
        });
    }

    private void sendMsg() {
        String msg = inputField.getText().trim();
        if (!msg.isEmpty()) {
            chatManager.sendMessage(ticketID, currentUser, msg);
            inputField.setText(""); // Clear input box
            loadChats(); // Show the new message immediately
        }
    }

    private void loadChats() {
        String history = chatManager.getChatHistory(ticketID);
        
        // Only update if the text has actually changed (prevents flickering)
        if (!chatArea.getText().equals(history)) {
            chatArea.setText(history);
            
            // Auto-scroll to the bottom to see new message
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        }
    }
}