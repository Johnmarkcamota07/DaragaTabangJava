package com.utils;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class haystackManager {
    private static final Logger LOGGER = Logger.getLogger(haystackManager.class.getName());
    private static final String FILE_PATH = "data/tickets.db";
    private final Map<String, Long> indexMap = new HashMap<>();

    public haystackManager() {
        rebuildIndex();
    }

    private void rebuildIndex() {
        File databaseFile = new File(FILE_PATH);
        if (!databaseFile.exists()) return;

        try (RandomAccessFile file = new RandomAccessFile(databaseFile, "r")) {
            String line;
            long currentPos = 0;
            while ((line = file.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length > 0) {
                    indexMap.put(data[0], currentPos);
                }
                currentPos = file.getFilePointer();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "fail to read data / file not found", e);
        }
    }

    public void saveOrUpdateTicket(String id, String date, String name, String status, String description) {
        try (RandomAccessFile file = new RandomAccessFile(FILE_PATH, "rw")) {
            long endPosition = file.length();
            file.seek(endPosition);
            
            String record = id + "|" + date + "|" + name + "|" + status + "|" + description;
            file.writeBytes(record + "\n");
            
            indexMap.put(id, endPosition);
            System.out.println("Saved Ticket " + id);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "ticket error",e);
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
