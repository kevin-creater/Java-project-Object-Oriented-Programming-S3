package controller;

import model.User;
import model.UserDAO;

public class UserController {

    private UserDAO userDAO;

    public UserController() {
        this.userDAO = new UserDAO();
    }

    public User login(String username, String password, String userType) {
        return userDAO.login(username, password, userType);
    }

    public boolean userExists(String username) {
        return userDAO.userExists(username);
    }

    public boolean register(String username, String password, String name, String email, String phone) {
        // Validation logic is moved from the View to the Controller
        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            return false; // Or throw exception
        }
        if (password.length() < 6) {
            return false;
        }
        if (userDAO.userExists(username)) {
            return false;
        }
        
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setUserType("CUSTOMER"); // All new users are CUSTOMER

        return userDAO.register(newUser);
    }
}