package com.UI;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.utils.haystackManager;


public class ViewTicketsUser extends JFrame {
    private String username;
    private JTable ticketTable;
    private DefaultTableModel tableModel;
    public ViewTicketsUser(String user)
    {
        this.username = user;
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
        loadUserTickets(user, tableModel);
    }  

    private void loadUserTickets(String user, DefaultTableModel model)  
    {
        haystackManager manager = new haystackManager();
        List<String[]> tickets = manager.getUserTickets(user);
        if(tickets.isEmpty()){
            JOptionPane.showMessageDialog(this, "No Tickets Found for user: " + user);
        }

        for (String[] t : tickets)
        {
            Object[] row = {
                t[0],t[1],t[2],t[3],t[4],t[5]
            };

            model.addRow(row);
        }
    }
}
