package com.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class ticket {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1000);

    private final int ticketID;
    private String ticketTitle;
    private final String ticketDescription;
    private final String ticketCreatedBy;
    private final Priority ticketPriority;
    private final Category ticketCategory;
    private Status ticketStatus;
    private String ticketAssignedTo;
    private final String ticketCreatedAt;
    private LocalDateTime ticketClosedAt;

    public static void initiallizedIdCounter(int lastId){
        ID_GENERATOR.set(lastId + 1);
    }
    public ticket(String title, String description, String createdBy, String priority, String category){
        this.ticketID = ID_GENERATOR.getAndIncrement();

        this.ticketTitle = title;
        this.ticketDescription = description;
        this.ticketCreatedBy = createdBy;
        this.ticketPriority = Priority.valueOf(priority.toUpperCase());
        this.ticketCategory = Category.valueOf(category.toUpperCase());
        this.ticketStatus = Status.PENDING;
        this.ticketCreatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
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

    public Status getStatus()
    {
        return ticketStatus;
    }

    public String getDescription() {
    return ticketDescription;
    }

    public Priority getPriority() {
        return ticketPriority;
    }

    public Category getCategory() {
        return ticketCategory;
    }

    public String getTicketCreatedBy(){
        return ticketCreatedBy;
    }
    public String getTicketCreatedAt() {
        return ticketCreatedAt; 
    }

public void setStatus(Status newStatus)
    {
        Status oldStatus = this.ticketStatus;
        this.ticketStatus = newStatus;

        System.out.println("Ticket " + this.ticketID + "status changed from " + oldStatus + "to" + newStatus);

        if (newStatus == Status.COMPLETED || newStatus == Status.CANCELLED){
            this.ticketClosedAt = LocalDateTime.now();
            System.out.println("Ticket closed at:" + this.ticketClosedAt);
        }else if(oldStatus == Status.COMPLETED && newStatus != Status.COMPLETED)
        {
            this.ticketClosedAt = null;
        }
    }

    public String getAssignedTo(){
        return ticketAssignedTo;
    }

    public void setAssignedTo(String assignedTo){
        this.ticketAssignedTo = assignedTo;
        if(this.ticketStatus == Status.PENDING){
            this.ticketStatus = Status.IN_PROGRESS;
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
