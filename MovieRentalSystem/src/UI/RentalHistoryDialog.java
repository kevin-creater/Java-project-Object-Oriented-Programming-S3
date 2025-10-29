package UI;

import dao.RentalDAO;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class RentalHistoryDialog extends JDialog {
    
    private RentalDAO rentalDAO;
    private User currentUser;
    private JTable historyTable; // Made table a class field
    
    public RentalHistoryDialog(JFrame parent, RentalDAO rentalDAO, User user) {
        super(parent, "Rental History", true); // 'true' for modal
        this.rentalDAO = rentalDAO;
        this.currentUser = user;
        
        setupUI();      // 1. Create the UI
        loadHistory();  // 2. Load the data
        setVisible(true); // 3. NOW, show the window
    }
    
    private void setupUI() {
        setSize(700, 400);
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String title = currentUser.isAdmin() ? "All Transactions" : "My Rental History for " + currentUser.getName();
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        historyTable = new JTable();
        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only one row
        JScrollPane scrollPane = new JScrollPane(historyTable);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Only add "Return" button if the user is a CUSTOMER
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
    
    /**
     * New method to load or reload the rental history from the database.
     */
    private void loadHistory() {
        DefaultTableModel model;
        if (currentUser.isAdmin()) {
            model = rentalDAO.getAllRentalHistory();
        } else {
            model = rentalDAO.getRentalHistory(currentUser.getUserId());
        }
        historyTable.setModel(model);
        
        // Hide the ID columns (rental_id and movie_id) from the user
        hideColumn(0); // Hide rental_id (index 0)
        hideColumn(1); // Hide movie_id (index 1)
    }
    
    /**
     * New method to handle the return logic.
     */
    private void returnSelectedMovie() {
        int selectedRow = historyTable.getSelectedRow();// select row rental history
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a movie to return.", "No Movie Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get data from the table model
        // We added rental_id (index 0) and movie_id (index 1) to the query
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
            boolean success = rentalDAO.returnMovie(rentalId, movieId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Movie returned successfully!");
                loadHistory(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to return movie.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * New helper method to hide columns by their index.
     */
    private void hideColumn(int columnIndex) {
        TableColumnModel tcm = historyTable.getColumnModel();
        tcm.getColumn(columnIndex).setMinWidth(0);
        tcm.getColumn(columnIndex).setMaxWidth(0);
        tcm.getColumn(columnIndex).setPreferredWidth(0);
        tcm.getColumn(columnIndex).setResizable(false);
    }
}