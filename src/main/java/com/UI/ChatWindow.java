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
import com.utils.haystackManager;

public class ChatWindow extends JDialog {

    private final int ticketID;
    private final String currentUser;
    private final ChatManager chatManager;
    
    private final JTextArea chatArea;
    private final JTextField inputField;
    private Timer refreshTimer;

    private String ticketTitle = "Loading...";
    private String ticketDesc = "";

    public ChatWindow(Frame owner, int ticketID, String userFullName) {
        super(owner, "Chat - Ticket #" + ticketID, true); 
        this.ticketID = ticketID;
        this.currentUser = userFullName;
        this.chatManager = new ChatManager();

        setSize(400, 500);
        setLocationRelativeTo(owner); 
        setLayout(new BorderLayout());

        loadTicketDetails(ticketID);
        
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

        //Auto refresh
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

    private void loadTicketDetails(int id) {
        haystackManager manager = new haystackManager();
        String rawData = manager.getLatestTicket(String.valueOf(id));

        if (rawData != null && !rawData.startsWith("Error")) {
            String[] parts = rawData.split("\\|");
            // 0=ID, 1=Date, 2=Title, 3=Status, 4=Priority, 5=Category, 6=CreatedBy, 7=Description
            if (parts.length >= 8) {
                this.ticketTitle = parts[2]; // Title
                this.ticketDesc = parts[7];  // Description
            }
        } 
        else 
        {
            this.ticketDesc = "Could not load ticket details.";
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
        String headerText = """
                            === TICKET DETAILS ===
                            TITLE: """ + ticketTitle + "\n" +
                            "DESC: " + ticketDesc + "\n" +
                            "======================\n\n";

        //Get Actual Chat History
        String history = chatManager.getChatHistory(ticketID);
        
        String fullText = headerText + history;

        // Only update if text changed
        if (!chatArea.getText().equals(fullText)) {
            chatArea.setText(fullText);
            chatArea.setCaretPosition(chatArea.getDocument().getLength()); // Scroll to bottom
        }
    }
}