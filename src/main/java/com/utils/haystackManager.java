package com.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class haystackManager {
    private static final Logger LOGGER = Logger.getLogger(haystackManager.class.getName());
    private static final String FILE_PATH = "data/tickets.db";
    private final Map<String, Long> indexMap = new HashMap<>();
    private static boolean isPrinted = false;
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
            if(!isPrinted)
            {            
                ticket.initiallizedIdCounter(maxId);
                System.out.println("Ticket ID counter initialized to: " + (maxId + 1));
                isPrinted = true;
            }
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

    public List<String[]> getUserTickets(String Username){
        List<String[]> userTickets = new ArrayList<>();

        try(RandomAccessFile file = new RandomAccessFile(FILE_PATH, "r")){
            for(Long position : indexMap.values())
            {
                file.seek(position);
                String line = file.readLine();

                if(line != null)
                {
                    String[] parts = line.split("\\|");
                    if(parts.length > 6 && parts[6].equals(Username)){
                        userTickets.add(parts);
                    }
                }
            }
        }catch(IOException e)
        {
            LOGGER.log(Level.SEVERE, "Error fetching tickets");
        }
        return userTickets;
    }
}
