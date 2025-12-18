package com.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatManager {
    private static final Logger LOGGER = Logger.getLogger(ChatManager.class.getName());
    private static final String CHAT_FILE = "data/chats.db";

    public ChatManager() {
        new File("data").mkdirs();
    }
    public void sendMessage(int ticketID, String sender, String message) {
        
        String safeMessage = message.replace("\n", " ");
        
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        String record = ticketID + "|" + sender + "|" + time + "|" + safeMessage;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CHAT_FILE, true))) {
            bw.write(record);
            bw.newLine();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"file not found");
        }
    }

    public String getChatHistory(int targetTicketID) {
        File file = new File(CHAT_FILE);
        if (!file.exists()) return "No messages yet.";

        StringBuilder history = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                
                if (parts.length >= 4) {
                    try {
                        int currentId = Integer.parseInt(parts[0]);
                        
                        if (currentId == targetTicketID) {
                            // Format: [Sender] (Time): Message
                            history.append("[").append(parts[1]).append("] ")
                                   .append("(").append(parts[2]).append("):\n")
                                   .append(parts[3]).append("\n\n");
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        } catch (IOException e) {
            return "Error loading chat.";
        }
        
        return history.length() > 0 ? history.toString() : "No messages start the conversation!";
    }
}
