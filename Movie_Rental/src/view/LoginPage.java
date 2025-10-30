package view;

// Import controller and model
import controller.UserController;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton loginButton, registerButton;
    
    // --- FIX 1: USE CONTROLLER ---
    private UserController userController;
    
    public LoginPage() {
        // --- FIX 2: INITIALIZE CONTROLLER ---
        userController = new UserController();
        setupUI();
    }
    
    private void setupUI() {
        // ... (This method is correct, no changes needed) ...
        setTitle("Movie Rental - Login");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Movie Rental System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLUE);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10)); 
        
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(usernameField);
        
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(passwordField);
        
        formPanel.add(new JLabel("Login as:"));
        String[] roles = {"CUSTOMER", "ADMIN"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setPreferredSize(new Dimension(200, 30));
        formPanel.add(roleComboBox);
        
        JPanel formWrapper = new JPanel(new GridBagLayout());
        formWrapper.add(formPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        loginButton = new JButton("Login");
        registerButton = new JButton("Create Account");
        
        loginButton.addActionListener(new LoginListener());
        registerButton.addActionListener(new RegisterListener());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formWrapper, BorderLayout.CENTER); 
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private class LoginListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem(); 
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginPage.this, "Enter username and password");
                return;
            }
            
            // --- FIX 3: USE CONTROLLER ---
            User user = userController.login(username, password, role);
            
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
            // --- FIX 4: PASS CONTROLLER ---
            new RegistrationDialog(LoginPage.this, userController);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage());
    }
}