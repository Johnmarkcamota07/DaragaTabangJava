package com.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import com.utils.DeleteTicket;
import com.utils.Priority;
import com.utils.Status;
import com.utils.haystackManager;

public class ViewTicketsUser extends JFrame {
    private final String user;
    private final JTable ticketTable;
    private final DefaultTableModel tableModel;
    public ViewTicketsUser(String name,String user)
    {
        this.user = user;
        setTitle("Vew Tickets");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800,600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Date", "Title", "Status", "Priority", "Category","Chat","Delete"};

        tableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return column == 6 || column == 7;//para maclick yung button sa column 6 and 7:>
            }
        };

        ticketTable = new JTable(tableModel);
        ticketTable.setRowHeight(30);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        ticketTable.setRowSorter(sorter);

        sorter.setComparator(3, (String s1, String s2) -> {
            try {
                return Status.valueOf(s1).compareTo(Status.valueOf(s2));
            } catch (Exception e) { return 0; }
        });

        sorter.setComparator(4, (String s1,String s2) ->{
            try {
                return Priority.valueOf(s1).compareTo(Priority.valueOf(s2));
            } catch (Exception e) {
                return 0;
            }
        });

        ticketTable.getColumn("Chat").setCellRenderer(new ButtonRenderer("Open Chat"));
        ticketTable.getColumn("Chat").setCellEditor(new ButtonEditor(new JCheckBox()));
        ticketTable.getColumnModel().getColumn(6).setPreferredWidth(100);

        ticketTable.getColumn("Delete").setCellRenderer(new ButtonRenderer("Delete"));
        ticketTable.getColumn("Delete").setCellEditor(new DeleteButtonEditor(new JCheckBox()));
        ticketTable.getColumnModel().getColumn(7).setPreferredWidth(80);
        add(new JScrollPane(ticketTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh List");
        JButton closeButton = new JButton("Back");

        refreshButton.addActionListener(e -> {
            tableModel.setRowCount(0);
            loadUserTickets(user);
        });
        closeButton.addActionListener(e -> {
            UserDashboard openUserDashboard = new UserDashboard(name, user);
            openUserDashboard.setVisible(true);
            dispose();
        });
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadUserTickets(user);
    }  

    private void loadUserTickets(String user)  
    {
        haystackManager manager = new haystackManager();
        List<String[]> tickets = manager.getUserTickets(user);

        for (String[] t : tickets)
        {
            if(t.length >= 7)
            {
                Object[] row = {
                    t[0],t[1],t[2],t[3],t[4],t[5], "Open Chat","Delete"
                };
                tableModel.addRow(row);
            }
        }
    }
    
    class ButtonRenderer extends JButton implements TableCellRenderer{
        public ButtonRenderer(String label) {
            setOpaque(true);
            setText(label);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            if (column == 7) {
                setBackground(new Color(255, 100, 100)); // Red
                setForeground(Color.WHITE);
            } else {
                setBackground(UIManager.getColor("Button.background"));
                setForeground(Color.BLACK);
            }
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor{
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "Open Chat" : value.toString();
            button.setText(label);
            isPushed = true;
            selectedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                String idString = (String) ticketTable.getValueAt(selectedRow, 0);
                int ticketID = Integer.parseInt(idString);

                SwingUtilities.invokeLater(() -> 
                    new ChatWindow(ViewTicketsUser.this, ticketID, user).setVisible(true)
                );
            }
            isPushed = false;
            return label;
        }
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
    class DeleteButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private JTable tableRef;

        public DeleteButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(255, 100, 100)); // Make it look red when clicking
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.tableRef = table;
            label = (value == null) ? "Delete" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int viewRow = tableRef.getEditingRow();
                if (viewRow != -1) {
                    int modelRow = tableRef.convertRowIndexToModel(viewRow);
                    Object idObj = tableModel.getValueAt(modelRow, 0); 
                    int ticketId = Integer.parseInt(idObj.toString());
                    int confirm = JOptionPane.showConfirmDialog(null, 
                        "Are you sure you want to delete Ticket #" + ticketId + "?", 
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        DeleteTicket.archiveAndDelete(ticketId);
                        tableModel.setRowCount(0);
                        loadUserTickets(user);
                        JOptionPane.showMessageDialog(null, "Ticket Deleted and Archived.");
                    }
                }
            }
            isPushed = false;
            return label;
        }
        @Override
        public boolean stopCellEditing() { isPushed = false; return super.stopCellEditing(); }
    }  
}