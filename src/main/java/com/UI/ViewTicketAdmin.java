package com.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import com.utils.Category;
import com.utils.Priority;
import com.utils.Status;
import com.utils.haystackManager;

public class ViewTicketAdmin extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(haystackManager.class.getName());
    private final JTable ticketTable;
    private final DefaultTableModel tableModel;
    private final JComboBox<String> categoryFilter;
    private final String currentAdmin; 

    public ViewTicketAdmin(String name, String user) {
        this.currentAdmin = name; 
        
        setTitle("Admin - View Tickets");
        setSize(1200, 600); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // TOP PANEL (Filter)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        topPanel.add(new JLabel("Filter by Category: "));

        categoryFilter = new JComboBox<>();
        
        categoryFilter.addItem("All");
        for (Category c : Category.values()) {
            categoryFilter.addItem(c.toString()); 
        }

        categoryFilter.addActionListener(e -> loadAllTickets()); 
        topPanel.add(categoryFilter);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.addActionListener(e -> loadAllTickets());
        topPanel.add(refreshButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            AdminDashboard back = new AdminDashboard(name,user);
            back.setVisible(true);
            dispose();
        });
        topPanel.add(backButton);
        
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "User", "Date", "Title", "Category", "Status", "Priority", "Assigned", "Chat", "Action"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Allow clicking Chat (8) and Claim (9) buttons and (5) Status
                return column == 8 || column == 9 || column == 5; 
            }
        };

        ticketTable = new JTable(tableModel);
        ticketTable.setRowHeight(35);
        ticketTable.getTableHeader().setReorderingAllowed(false);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        ticketTable.setRowSorter(sorter);
        
        sorter.setComparator(6, (String s1, String s2) -> { 
             try { return Priority.valueOf(s1).compareTo(Priority.valueOf(s2)); } catch (Exception e) { return 0; }
        });
        
        sorter.setComparator(5, (Status s1, Status s2) -> s1.compareTo(s2));

        sorter.setSortable(8, false);
        sorter.setSortable(9, false);

        TableColumn statusColumn = ticketTable.getColumnModel().getColumn(5);
        java.util.List<Status> validOptions = new java.util.ArrayList<>();
        for (Status s : Status.values()) {
            if (s != Status.DELETED) {
                validOptions.add(s);
            }
        }   
        Status[] displayStatuses = validOptions.toArray(new Status[0]);    
        JComboBox<Status> statusComboBox = new JComboBox<>(displayStatuses);
        statusColumn.setCellEditor(new DefaultCellEditor(statusComboBox));

        tableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                
                if (col == 5 && row >= 0) {
                    try {
                        Object idObj = tableModel.getValueAt(row, 0);
                        Object statusObj = tableModel.getValueAt(row, 5);

                        if (idObj != null && statusObj instanceof Status) {
                            int ticketId = Integer.parseInt(idObj.toString());
                            Status newStatus = (Status) statusObj;

                            haystackManager manager = new haystackManager();
                            manager.updateTicketStatus(ticketId, newStatus);
                            
                            System.out.println("Auto-Saved Ticket #" + ticketId + " to " + newStatus);
                        }
                    } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE,"out of bounds");
                    }
                }
            }
        });
        
        // Chat Button (Gray)
        ticketTable.getColumn("Chat").setCellRenderer(new ButtonRenderer("Chat", Color.LIGHT_GRAY));
        ticketTable.getColumn("Chat").setCellEditor(new ChatButtonEditor(new JCheckBox()));
        
        // Claim Button (Sky Blue)
        ticketTable.getColumn("Action").setCellRenderer(new ButtonRenderer("Claim", new Color(135, 206, 250))); 
        ticketTable.getColumn("Action").setCellEditor(new ClaimButtonEditor(new JCheckBox()));

        // Adjust Column Widths
        ticketTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        ticketTable.getColumnModel().getColumn(1).setPreferredWidth(100); // User
        ticketTable.getColumnModel().getColumn(3).setPreferredWidth(200); // Title
        ticketTable.getColumnModel().getColumn(7).setPreferredWidth(100); // Assigned To

        add(new JScrollPane(ticketTable), BorderLayout.CENTER);

        loadAllTickets();

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadAllTickets() {
        tableModel.setRowCount(0);
        haystackManager manager = new haystackManager();
        List<String[]> tickets = manager.getAllTickets();
        
        String selectedCat = (String) categoryFilter.getSelectedItem();

        for (String[] t : tickets) {
            if (t.length >= 7) { 
                // DB Structure: 0=ID, 1=Date, 2=Title, 3=Status, 4=Priority, 5=Category, 6=CreatedBy, 7=Desc, 8=AssignedTo
                
                String category = t[5];
                
                if ("All".equals(selectedCat) || category.equalsIgnoreCase(selectedCat)) {
                    
                    String assignedTo = (t.length >= 9) ? t[8] : "Unassigned";
                    
                    String actionLabel = assignedTo.equalsIgnoreCase(currentAdmin) ? "Assigned" : "Claim";

                    Status status = Status.valueOf(t[3]);
                    Object[] row = {
                        t[0], // ID
                        t[6], // User
                        t[1], // Date
                        t[2], // Title
                        t[5], // Category
                        status, // Status
                        t[4], // Priority
                        assignedTo, // Assigned Admin
                        "Chat",
                        actionLabel // Button Text
                    };
                    tableModel.addRow(row);
                }
            }
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        private final Color baseColor;
        public ButtonRenderer(String label, Color c) { 
            setOpaque(true); 
            setText(label); 
            this.baseColor = c;
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            setText((value == null) ? "" : value.toString());
            setBackground(baseColor);
            
            if ("Assigned".equals(getText())) {
                setBackground(Color.GREEN); // Show green if already claimed
            }
            return this;
        }
    }

    class ChatButtonEditor extends DefaultCellEditor {
        JButton button = new JButton();
        boolean isPushed;
        JTable tableRef;
        public ChatButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
            tableRef = table;
            button.setText("Chat");
            isPushed = true;
            return button;
        }
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int modelRow = tableRef.convertRowIndexToModel(tableRef.getEditingRow());
                String idStr = tableRef.getModel().getValueAt(modelRow, 0).toString();
                
                SwingUtilities.invokeLater(() -> 
                    new ChatWindow(ViewTicketAdmin.this, Integer.parseInt(idStr), currentAdmin).setVisible(true)
                );
            }
            isPushed = false;
            return "Chat";
        }
        @Override
        public boolean stopCellEditing() { isPushed = false; return super.stopCellEditing(); }
    }
    class ClaimButtonEditor extends DefaultCellEditor {
        JButton button = new JButton();
        boolean isPushed;
        JTable tableRef;
        String label;
        
        public ClaimButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
            tableRef = table;
            label = (value == null) ? "Claim" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }
        @Override
        public Object getCellEditorValue() {
            if (isPushed && "Claim".equals(label)) { // allow action if not yet Assigned
                int modelRow = tableRef.convertRowIndexToModel(tableRef.getEditingRow());
                String idStr = tableRef.getModel().getValueAt(modelRow, 0).toString();
                int ticketId = Integer.parseInt(idStr);
                
                int response = JOptionPane.showConfirmDialog(null, 
                    "Assign Ticket #" + ticketId + " to yourself (" + currentAdmin + ")?", 
                    "Claim Ticket", JOptionPane.YES_NO_OPTION);
                
                if (response == JOptionPane.YES_OPTION) {
                    SwingUtilities.invokeLater(() -> {                        
                    // Update database
                    haystackManager manager = new haystackManager();
                    manager.assignTicket(ticketId, currentAdmin);
                    
                    //Refresh tickets
                    loadAllTickets();
                    
                    JOptionPane.showMessageDialog(null, "Ticket successfully assigned to you!");
                        
                    });
                }
            }
            isPushed = false;
            return label;
        }
        @Override
        public boolean stopCellEditing() { isPushed = false; return super.stopCellEditing(); }
    }
}