package UI;

import dao.UserDAO;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RegistrationDialog extends JDialog {
    private JTextField usernameField, nameField, emailField, phoneField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton registerButton, cancelButton;
    private UserDAO userDAO;
    
    public RegistrationDialog(JFrame parent, UserDAO userDAO) {
        super(parent, "Register", true);
        this.userDAO = userDAO;
        setupUI();
    }
    
    private void setupUI() {
        setSize(400, 400);
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        
        // Username
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);
        
        // Password
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);
        
        // Confirm Password
        formPanel.add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        formPanel.add(confirmPasswordField);
        
        // Name
        formPanel.add(new JLabel("Full Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        
        // Email
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);
        
        // Phone
        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        
        // Phone validation - only numbers
        phoneField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }
                if (phoneField.getText().length() >= 10 && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }
            }
        });
        formPanel.add(phoneField);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        registerButton = new JButton("Register");
        cancelButton = new JButton("Cancel");
        
        registerButton.addActionListener(new RegisterListener());
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        setVisible(true);
    }
    
    private class RegisterListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            
            // Validation
            if (username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(RegistrationDialog.this, "All fields are required");
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(RegistrationDialog.this, "Passwords don't match");
                return;
            }
            
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(RegistrationDialog.this, "Password must be 6+ characters");
                return;
            }
            
            if (email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(RegistrationDialog.this, "Enter a valid Email address");
                return;
            }
            if (phone.length() != 10 || !phone.matches("[6-9][0-9]{9}")) {
                JOptionPane.showMessageDialog(RegistrationDialog.this, "Enter valid 10-digit phone number starting with 6-9");
                return;
            }
            
            if (userDAO.userExists(username)) {
                JOptionPane.showMessageDialog(RegistrationDialog.this, "Username already exists");
                return;
            }
            
            // Create user and register
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPhone(phone);
            newUser.setUserType("CUSTOMER");
            
            if (userDAO.register(newUser)) {
                JOptionPane.showMessageDialog(RegistrationDialog.this, "Registration successful!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(RegistrationDialog.this, "Registration failed");
            }
        }
    }
}