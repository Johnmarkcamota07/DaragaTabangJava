package com.UI;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class ViewTicketsUser extends JFrame {
    private String user;
    private JTable ticketTable;
    private DefaultTableModel tableModel;
    private final String FILE_PATH = "data/tickets.db";
    public ViewTicketsUser(String user)
    {
        this.user = user;
        setTitle("Vew Tickets");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800,600);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Date", "Title", "Status", "Priority", "Category"};

        tableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        ticketTable = new JTable(tableModel);

        add(new JScrollPane(ticketTable), BorderLayout.CENTER);
        loadUserTickets();
    }  

    private void loadUserTickets()  
    {
        File file = new File(FILE_PATH);
        if(!file.exists()){
            JOptionPane.showMessageDialog(this, "NO TICKETS DATABASE FOUND");
        }

        

    }
}
