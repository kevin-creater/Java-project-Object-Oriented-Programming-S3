package view;

// Import controller
import controller.UserController;
// We don't need model.User here anymore!
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
    
    // --- FIX 1: USE CONTROLLER ---
    private UserController userController;
    
    // --- FIX 2: UPDATE CONSTRUCTOR ---
    public RegistrationDialog(JFrame parent, UserController userController) {
        super(parent, "Register", true);
        this.userController = userController;
        setupUI();
    }
    
    private void setupUI() {
        // ... (This method is correct, no changes needed) ...
        setSize(400, 400);
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);
        
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);
        
        formPanel.add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        formPanel.add(confirmPasswordField);
        
        formPanel.add(new JLabel("Full Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);
        
        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        
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
            
            // --- FIX 3: SIMPLIFY VALIDATION ---
            // Most validation is now in the controller, but we check client-side
            // things here.
            
            if (username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(RegistrationDialog.this, "All fields are required");
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(RegistrationDialog.this, "Passwords don't match");
                return;
            }
            
            // You can keep extra UI validation here
            if (email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(RegistrationDialog.this, "Enter a valid Email address");
                return;
            }
            if (phone.length() != 10 || !phone.matches("[6-9][0-9]{9}")) {
                JOptionPane.showMessageDialog(RegistrationDialog.this, "Enter valid 10-digit phone number starting with 6-9");
                return;
            }
            
            // Check for existing user (optional, controller does this too)
            if (userController.userExists(username)) {
                 JOptionPane.showMessageDialog(RegistrationDialog.this, "Username already exists");
                 return;
            }

            // --- FIX 4: USE CONTROLLER ---
            // Pass the clean data to the controller
            boolean success = userController.register(username, password, name, email, phone);
            
            if (success) {
                JOptionPane.showMessageDialog(RegistrationDialog.this, "Registration successful!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(RegistrationDialog.this, "Registration failed (e.g., username exists or password < 6 chars)");
            }
        }
    }
}
