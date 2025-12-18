package com.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeleteTicket {
    private static final Logger LOGGER = Logger.getLogger(DeleteTicket.class.getName());
    private static final String TICKET_DB = "data/tickets.db";
    private static final String CHAT_DB = "data/chats.db";
    private static final String ARCHIVE_TICKET = "data/archiveticket.db";
    private static final String ARCHIVE_CHAT = "data/archivechat.db";

    public static void archiveAndDelete(int ticketId) {
        archiveTicketData(ticketId);
        archiveChatData(ticketId);
    }

    private static void archiveTicketData(int ticketId) {
        File inputFile = new File(TICKET_DB);
        File archiveFile = new File(ARCHIVE_TICKET);
        List<String> keptLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length > 0) {
                    int currentId = Integer.parseInt(parts[0]);
                    
                    if (currentId == ticketId) {
                        if (parts.length >= 4) {
                            parts[3] = Status.DELETED.name(); // Change Status
                        }
                        String archivedLine = String.join("|", parts);
                        appendToArchive(archiveFile, archivedLine);
                    } else {
                        keptLines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"file not found");
        }

        // Overwrite the main DB with the remaining lines
        writeLinesToFile(inputFile, keptLines);
    }

    private static void archiveChatData(int ticketId) {
        File inputFile = new File(CHAT_DB);
        File archiveFile = new File(ARCHIVE_CHAT);
        List<String> keptLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length > 0) {
                    try {
                        int currentId = Integer.parseInt(parts[0]);
                        if (currentId == ticketId) {
                            // Match found: Move to Archive
                            appendToArchive(archiveFile, line);
                        } else {
                            // Not a match: Keep in Tickets DB
                            keptLines.add(line);
                        }
                    } catch (NumberFormatException e) {
                        keptLines.add(line); // Keep error or broken lines just in case
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"file not found");
        }
        writeLinesToFile(inputFile, keptLines);
    }

    private static void appendToArchive(File file, String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            
        }
    }

    private static void writeLinesToFile(File file, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"file not found");
        }
    }
}