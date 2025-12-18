package com.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class haystackManager {
    private static final Logger LOGGER = Logger.getLogger(haystackManager.class.getName());
    private static final String FILE_PATH = "data/tickets.db";
    private final Map<String, Long> indexMap = new HashMap<>();

    public haystackManager() {
        rebuildIndex();
    }

    public final void rebuildIndex() {
        File databaseFile = new File(FILE_PATH);
        if (!databaseFile.exists()) return;
        int maxId = 1000;
        try (RandomAccessFile file = new RandomAccessFile(databaseFile, "r")) {
            String line;
            long currentPos = 0;
            while ((line = file.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length > 0) {
                    indexMap.put(data[0], currentPos);
                    try {
                        int currentId = Integer.parseInt(data[0]);
                        if(currentId > maxId)
                        {
                            maxId = currentId;
                        }

                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.FINE,"improper format");
                    }
                }
                currentPos = file.getFilePointer();
            }

            ticket.initiallizedIdCounter(maxId);
            System.out.println("Ticket ID counter initialized to: " + (maxId + 1));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "fail to read data / file not found", e);
        }
    }

    public boolean saveOrUpdateTicket(ticket t) {
        try (RandomAccessFile file = new RandomAccessFile(FILE_PATH, "rw")) {
            long endPosition = file.length();
            file.seek(endPosition);
            
            String safeDesc = t.getDescription().replace("\n", " ");
            String record = t.getTicketId() + "|" + 
                            t.getTicketCreatedAt() + "|" + 
                            t.getTitle() + "|" + 
                            t.getStatus() + "|" + 
                            t.getPriority() + "|" +
                            t.getCategory() + "|" + 
                            t.getTicketCreatedBy() + "|" +
                            safeDesc;
            file.writeBytes(record + "\n");
            
            indexMap.put(String.valueOf(t.getTicketId()), endPosition);
            System.out.println("Saved Ticket " + t.getTicketId() + " at pos: " + endPosition);
            return true;

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "ticket error",e);
            return false;
        }
    }

    public String getLatestTicket(String id) {
        if (!indexMap.containsKey(id)) return "Error: Ticket ID not found.";

        try (RandomAccessFile file = new RandomAccessFile(FILE_PATH, "r")) {
            file.seek(indexMap.get(id));
            return file.readLine();
        } catch (IOException e) {
            return "Error reading file.";
        }
    }
}
