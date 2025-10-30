package model;

import model.DatabaseConnection;
import model.User;
import java.sql.*;
import javax.swing.JOptionPane;

public class UserDAO {

    // Updated login method to check for userType
    public User login(String username, String password, String userType) {
        User user = null;
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND user_type = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, userType); // Check role
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("user_type")
                );
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return user;
    }
    
    public boolean register(User user) {
        String sql = "INSERT INTO users (username, password, name, email, phone, user_type) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, "CUSTOMER"); // All new users are CUSTOMER
            
            int result = stmt.executeUpdate();
            return result > 0;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean userExists(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}