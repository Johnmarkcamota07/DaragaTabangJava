package com.UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

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

        String[] columnNames = {"ID", "Date", "Title", "Status", "Priority", "Category","Action"};

        tableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return column == 6; //para maclick yung button sa column 6 :>
            }
        };

        ticketTable = new JTable(tableModel);
        ticketTable.setRowHeight(30);
        ticketTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        ticketTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));
        
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
            if(t.length >= 6)
            {
                Object[] row = {
                    t[0],t[1],t[2],t[3],t[4],t[5], "Open Chat"
                };
                tableModel.addRow(row);
            }
        }
    }
    
    class ButtonRenderer extends JButton implements TableCellRenderer{
        public ButtonRenderer() {
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Open Chat" : value.toString());
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
            ticketTable.getColumnModel().getColumn(6).setPreferredWidth(100);
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
}

