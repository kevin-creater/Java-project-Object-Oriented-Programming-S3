package view;

// Import the controller
import controller.RentalController;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class RentalHistoryDialog extends JDialog {
    
    // --- FIX 1: CHANGE DAO TO CONTROLLER ---
    private RentalController rentalController;
    private User currentUser;
    private JTable historyTable; 
    
    // --- FIX 2: UPDATE CONSTRUCTOR ---
    public RentalHistoryDialog(JFrame parent, RentalController rentalController, User user) {
        super(parent, "Rental History", true); 
        this.rentalController = rentalController;
        this.currentUser = user;
        
        setupUI();      
        loadHistory();  
        setVisible(true); 
    }
    
    private void setupUI() {
        // ... (This method is correct, no changes needed) ...
        setSize(700, 400);
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String title = currentUser.isAdmin() ? "All Transactions" : "My Rental History for " + currentUser.getName();
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        historyTable = new JTable();
        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        JScrollPane scrollPane = new JScrollPane(historyTable);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());

        if (!currentUser.isAdmin()) {
            JButton returnButton = new JButton("Return Selected Movie");
            returnButton.addActionListener(e -> returnSelectedMovie());
            buttonPanel.add(returnButton);
        }
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadHistory() {
        // --- FIX 3: USE CONTROLLER ---
        DefaultTableModel model = rentalController.getRentalHistory(currentUser);
        historyTable.setModel(model);
        
        hideColumn(0); 
        hideColumn(1); 
    }
    
    private void returnSelectedMovie() {
        int selectedRow = historyTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a movie to return.", "No Movie Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int rentalId = (int) historyTable.getModel().getValueAt(selectedRow, 0);
        int movieId = (int) historyTable.getModel().getValueAt(selectedRow, 1);
        String movieTitle = (String) historyTable.getModel().getValueAt(selectedRow, 2);
        String status = (String) historyTable.getModel().getValueAt(selectedRow, 6);
        
        if (!"ACTIVE".equals(status)) {
            JOptionPane.showMessageDialog(this, "This movie has already been returned.", "Already Returned", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to return '" + movieTitle + "'?", 
            "Confirm Return", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // --- FIX 4: USE CONTROLLER ---
            boolean success = rentalController.returnMovie(rentalId, movieId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Movie returned successfully!");
                loadHistory(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to return movie.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void hideColumn(int columnIndex) {
        // ... (This method is correct, no changes needed) ...
        TableColumnModel tcm = historyTable.getColumnModel();
        tcm.getColumn(columnIndex).setMinWidth(0);
        tcm.getColumn(columnIndex).setMaxWidth(0);
        tcm.getColumn(columnIndex).setPreferredWidth(0);
        tcm.getColumn(columnIndex).setResizable(false);
    }
}
