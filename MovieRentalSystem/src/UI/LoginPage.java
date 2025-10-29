package UI;

import dao.UserDAO;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox; // New dropdown
    private JButton loginButton, registerButton;
    private UserDAO userDAO;
    
    public LoginPage() {
        userDAO = new UserDAO();
        setupUI();
    }
    
    private void setupUI() {
        setTitle("Movie Rental - Login");
        setSize(400, 350); // Adjusted size
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Movie Rental System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLUE);
        
        // Form
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 rows
        
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30)); // Set preferred size
        formPanel.add(usernameField);
        
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30)); // Set preferred size
        formPanel.add(passwordField);
        
        // Role Selector
        formPanel.add(new JLabel("Login as:"));
        String[] roles = {"CUSTOMER", "ADMIN"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setPreferredSize(new Dimension(200, 30)); // Set preferred size
        formPanel.add(roleComboBox);
        
        // --- THIS IS THE FIX ---
        // Create a wrapper panel to hold the form panel
        // This stops the form from stretching to fill the window
        JPanel formWrapper = new JPanel(new GridBagLayout());
        formWrapper.add(formPanel);
        // --- END OF FIX ---

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        loginButton = new JButton("Login");
        registerButton = new JButton("Create Account");
        
        loginButton.addActionListener(new LoginListener());
        registerButton.addActionListener(new RegisterListener());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formWrapper, BorderLayout.CENTER); // <-- Use the wrapper here
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private class LoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem(); // Get role
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginPage.this, "Enter username and password");
                return;
            }
            
            // Pass role to login method
            User user = userDAO.login(username, password, role);
            
            if (user != null) {
                JOptionPane.showMessageDialog(LoginPage.this, "Login successful!");
                dispose();
                new MainApp(user); // Open main app
            } else {
                JOptionPane.showMessageDialog(LoginPage.this, "Invalid login credentials or role");
            }
        }
    }
    
    private class RegisterListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new RegistrationDialog(LoginPage.this, userDAO);
        }
    }
    
    public static void main(String[] args) {
        // Ensure UI updates on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new LoginPage());
    }
} 