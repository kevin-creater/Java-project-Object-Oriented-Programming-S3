package model;

import model.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class RentalDAO {

    /**
     * Rents a movie. This is a transaction that:
     * 1. Inserts a new rental record.
     * 2. Updates the movie's status to 'RENTED'.
     */
    public boolean rentMovie(int userId, int movieId, double price) {
        String insertRentalSql = "INSERT INTO rentals (movie_id, user_id, rental_date, due_date, total_amount, status) " +
                                 "VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), ?, 'ACTIVE')";
        String updateMovieSql = "UPDATE movies SET status = 'RENTED' WHERE movie_id = ?";
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Insert rental record
            try (PreparedStatement rentalStmt = conn.prepareStatement(insertRentalSql)) {
                rentalStmt.setInt(1, movieId);
                rentalStmt.setInt(2, userId);
                rentalStmt.setDouble(3, price);
                rentalStmt.executeUpdate();
            }
            
            // 2. Update movie status
            try (PreparedStatement movieStmt = conn.prepareStatement(updateMovieSql)) {
                movieStmt.setInt(1, movieId);
                movieStmt.executeUpdate();
            }
            
            conn.commit(); // Commit transaction
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Rollback on error
            } catch (Exception re) {
                re.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ce) {
                ce.printStackTrace();
            }
        }
    }
    
    /**
     * NEW METHOD: Returns a movie. This is a transaction that:
     * 1. Updates the rental record to 'RETURNED' and sets the return_date.
     * 2. Updates the movie's status to 'AVAILABLE'.
     */
    public boolean returnMovie(int rentalId, int movieId) {
        String updateRentalSql = "UPDATE rentals SET status = 'RETURNED', return_date = NOW() WHERE rental_id = ?";
        String updateMovieSql = "UPDATE movies SET status = 'AVAILABLE' WHERE movie_id = ?";
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Update rental record
            try (PreparedStatement rentalStmt = conn.prepareStatement(updateRentalSql)) {
                rentalStmt.setInt(1, rentalId);
                rentalStmt.executeUpdate();
            }
            
            // 2. Update movie status
            try (PreparedStatement movieStmt = conn.prepareStatement(updateMovieSql)) {
                movieStmt.setInt(1, movieId);
                movieStmt.executeUpdate();
            }
            
            conn.commit(); // Commit transaction
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Rollback on error
            } catch (Exception re) {
                re.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ce) {
                ce.printStackTrace();
            }
        }
    }


    /**
     * Fetches rental history for a specific user.
     * UPDATED: Added r.rental_id and m.movie_id for the 'return' logic.
     */
    public DefaultTableModel getRentalHistory(int userId) {
        String sql = "SELECT r.rental_id, m.movie_id, m.title AS 'Movie Title', r.rental_date AS 'Rented On', " +
                     "r.due_date AS 'Due Date', r.return_date AS 'Returned On', r.status AS 'Status' " +
                     "FROM rentals r " +
                     "JOIN movies m ON r.movie_id = m.movie_id " +
                     "WHERE r.user_id = ? " +
                     "ORDER BY r.rental_date DESC";
        
        return buildTableModel(sql, userId);
    }
    
    /**
     * Fetches ALL rental history (for Admin).
     * UPDATED: Added r.rental_id and m.movie_id.
     */
    public DefaultTableModel getAllRentalHistory() {
        String sql = "SELECT r.rental_id, m.movie_id, u.name AS 'Customer', m.title AS 'Movie Title', r.rental_date AS 'Rented On', " +
                     "r.due_date AS 'Due Date', r.return_date AS 'Returned On', r.status AS 'Status', r.total_amount AS 'Amount' " +
                     "FROM rentals r " +
                     "JOIN movies m ON r.movie_id = m.movie_id " +
                     "JOIN users u ON r.user_id = u.user_id " +
                     "ORDER BY r.rental_date DESC";
        
        return buildTableModel(sql, null);
    }
    
    // Helper method to build a JTable model from a SQL query
    private DefaultTableModel buildTableModel(String sql, Integer userId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (userId != null) {
                stmt.setInt(1, userId);
            }
            
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            // Column names
            Vector<String> columnNames = new Vector<>();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            // Data
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                data.add(row);
            }
            
            // Create a non-editable table model
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            return model;
            
        } catch (Exception e) {
            e.printStackTrace();
            // Return empty model on error
            return new DefaultTableModel(); 
        }
    }
}