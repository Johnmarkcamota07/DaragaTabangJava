package com.utils;//utils

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class ticket {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1000);

    private final int ticketID;
    private String ticketTitle;
    private String ticketDescription;
    private String ticketCreatedBy;
    private final String ticketPriority;
    private String ticketCategory;
    private String ticketStatus;
    private String ticketAssignedTo;
    private LocalDateTime ticketCreatedAt;
    private LocalDateTime ticketClosedAt;

    public ticket(String title, String description, String createdBy, String priority, String category){
        this.ticketID = ID_GENERATOR.getAndIncrement();

        this.ticketTitle = title;
        this.ticketDescription = description;
        this.ticketCreatedBy = createdBy;
        this.ticketPriority = priority;
        this.ticketCategory = category;
        this.ticketStatus = "Open";
        this.ticketCreatedAt = LocalDateTime.now();
        this.ticketAssignedTo = null;
        this.ticketClosedAt = null;
    }

    public int getTicketId(){
        return ticketID;
    }

    public String getTitle(){
        return ticketTitle;
    }

    public void setTitle(String title)
    {
        this.ticketTitle = title;
    }

    public String getStatus()
    {
        return ticketStatus;
    }

    public void setStatus(String newStatus)
    {
        String oldStatus = this.ticketStatus;
        this.ticketStatus = newStatus;

        System.out.println("Ticket " + this.ticketID + "status changed from " + oldStatus + "to" + newStatus);

        if (newStatus.equalsIgnoreCase("Closed") || newStatus.equalsIgnoreCase("Resolved")){
            this.ticketClosedAt = LocalDateTime.now();
            System.out.println("Ticket closedd at:" + this.ticketClosedAt);
        }else if(oldStatus.equalsIgnoreCase("Closed") && !newStatus.equalsIgnoreCase("Closed"))
        {
            this.ticketClosedAt = null;
        }
    }

    public String getAssignedTo(){
        return ticketAssignedTo;
    }

    public void setAssignedTo(String assignedTo){
        this.ticketAssignedTo = assignedTo;
        if(this.ticketStatus.equalsIgnoreCase("Open")){
            this.ticketStatus = "Assigned";
        }
        System.out.println("Ticket " + this.ticketID + " assigned to: " + assignedTo);
    }

    @Override
    public String toString() {
        return "Ticket #" + ticketID + 
               " | Title: " + ticketTitle + 
               " | Status: " + ticketStatus + 
               " | Priority: " + ticketPriority + 
               " | Assigned To: " + (ticketAssignedTo != null ? ticketAssignedTo : "N/A");
    }
}
