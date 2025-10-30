package view;

// Import controllers
import controller.MovieController;
import controller.RentalController;

import model.Movie;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

public class MainApp extends JFrame {
    private User currentUser;
    
    // --- FIX 1: DECLARE CONTROLLERS ---
    private MovieController movieController;
    private RentalController rentalController;
    
    private JPanel movieGridPanel; 
    private JTextField searchField;
    
    public MainApp(User user) {
        this.currentUser = user;
        
        // --- FIX 2: INITIALIZE CONTROLLERS ---
        this.movieController = new MovieController();
        this.rentalController = new RentalController();
        
        setupUI();
        loadMovies();
    }
    
    private void setupUI() {
        setTitle("Movie Rental - Welcome " + currentUser.getName());
        setSize(1000, 700); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Top Panel: Search and Title
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Available Movies", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(25);
        searchPanel.add(searchField);
        
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchMovies());
        searchPanel.add(searchButton);
        
        JButton showAllButton = new JButton("Show All");
        showAllButton.addActionListener(e -> loadMovies());
        searchPanel.add(showAllButton);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Center Panel: Movie Grid
        movieGridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        JScrollPane scrollPane = new JScrollPane(movieGridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom Panel: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton historyButton;
        if (currentUser.isAdmin()) {
            historyButton = new JButton("View All Rentals");
        } else {
            historyButton = new JButton("View My Rentals");
        }
        historyButton.addActionListener(e -> viewRentalHistory());
        buttonPanel.add(historyButton);

        if (currentUser.isAdmin()) {
            JButton addButton = new JButton("Add Movie");
            addButton.addActionListener(e -> addMovie());
            buttonPanel.add(addButton);
        }
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginPage(); // This should also be updated to use controller
        });
        buttonPanel.add(logoutButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        setVisible(true);
    }

    private JPanel createMovieCard(Movie movie) {
        // ... (This method is correct, no changes needed)
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setPreferredSize(new Dimension(280, 380)); 
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        Dimension imageSize = new Dimension(150, 200); 
        
        String imagePath = movie.getImagePath();
        ImageIcon movieIcon = loadImage(imagePath, imageSize.width, imageSize.height); 
        
        JLabel imageLabel = new JLabel(movieIcon, JLabel.CENTER);
        imageLabel.setPreferredSize(imageSize);
        imageLabel.setMinimumSize(imageSize);
        imageLabel.setMaximumSize(imageSize);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); 
        
        imagePanel.add(imageLabel);
        card.add(imagePanel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 3, 3));
        detailsPanel.setBorder(new EmptyBorder(5, 0, 0, 0)); 
        
        JLabel titleLabel = new JLabel(movie.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        detailsPanel.add(titleLabel);

        detailsPanel.add(createStarRating(movie.getRating()));
        detailsPanel.add(new JLabel("Genre: " + movie.getGenre()));
        detailsPanel.add(new JLabel("Price: ₹" + movie.getRentalPrice()));
        
        JLabel statusLabel = new JLabel(movie.getStatus());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        if (movie.isAvailable()) {
            statusLabel.setForeground(new Color(0, 128, 0)); 
        } else {
            statusLabel.setForeground(Color.RED);
        }
        detailsPanel.add(statusLabel);
        
        card.add(detailsPanel, BorderLayout.CENTER);
        
        JButton rentButton = new JButton("Rent Movie");
        if (currentUser.isAdmin() || !movie.isAvailable()) {
            rentButton.setEnabled(false);
        }
        rentButton.addActionListener(e -> rentMovie(movie));
        card.add(rentButton, BorderLayout.SOUTH);
        
        return card;
    }
    
    private ImageIcon loadImage(String path, int width, int height) {
        // ... (This method is correct, no changes needed)
        String fullPath = "images" + File.separator + path;
        File file = new File(fullPath);
        ImageIcon icon;

        if (file.exists() && !file.isDirectory()) {
            icon = new ImageIcon(file.getAbsolutePath());
        } else {
            System.err.println("Cannot find image: " + fullPath + ". Loading default.");
            File defaultFile = new File("images" + File.separator + "default_movie.png");
            
            if(defaultFile.exists() && !defaultFile.isDirectory()) {
                icon = new ImageIcon(defaultFile.getAbsolutePath());
            } else {
                System.err.println("CRITICAL: DEFAULT IMAGE MISSING from images/default_movie.png");
                java.awt.image.BufferedImage placeholder = new java.awt.image.BufferedImage(
                    width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
                Graphics2D g = placeholder.createGraphics();
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, width, height);
                g.dispose();
                return new ImageIcon(placeholder);
            }
        }
        
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private JLabel createStarRating(double rating) {
        // ... (This method is correct, no changes needed)
        StringBuilder stars = new StringBuilder();
        int fullStars = (int) Math.round(rating);
        
        for (int i = 0; i < fullStars; i++) {
            stars.append("*");
        }
        for (int i = fullStars; i < 5; i++) {
            stars.append("-");
        }
        
        JLabel ratingLabel = new JLabel(stars.toString() + " (" + rating + ")");
        ratingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        ratingLabel.setForeground(new Color(245, 180, 0)); 
        return ratingLabel;
    }

    // This method now correctly uses the controller
    private void loadMovies() {
    	List<Movie> movies = movieController.getAllMovies(); 
        showMovies(movies);
    }
    
    // This method now correctly uses the controller
    private void searchMovies() {
        String searchText = searchField.getText().trim();
        List<Movie> movies = movieController.searchMovies(searchText); 
        showMovies(movies);
    }
    
    private void showMovies(List<Movie> movies) {
        // ... (This method is correct, no changes needed)
        movieGridPanel.removeAll(); 
        
        if (movies.isEmpty()) {
            movieGridPanel.add(new JLabel("No movies found."));
        } else {
            for (Movie movie : movies) {
                movieGridPanel.add(createMovieCard(movie));
            }
        }
        
        movieGridPanel.revalidate();
        movieGridPanel.repaint();
    }
    
    private void rentMovie(Movie movie) {
        if (!movie.isAvailable()) {
            JOptionPane.showMessageDialog(this, "This movie is already rented.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Rent '" + movie.getTitle() + "' for ₹" + movie.getRentalPrice() + "?\nDue in 7 days.",
            "Confirm Rental",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            // --- FIX 3: USE RENTAL CONTROLLER ---
            boolean success = rentalController.rentMovie(currentUser.getUserId(), movie.getMovieId(), movie.getRentalPrice());
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Movie rented successfully!");
                loadMovies(); 
            } else {
                JOptionPane.showMessageDialog(this, "Rental failed. Please try again.");
            }
        }
    }
    
    
    private void viewRentalHistory() {
        // --- FIX 4: PASS RENTAL CONTROLLER ---
        // Pass the controller, not the DAO
        new RentalHistoryDialog(this, rentalController, currentUser);
        loadMovies(); 
    }
    
    // This method now correctly uses the controller
    private void addMovie() {
        JTextField titleField = new JTextField();
        JTextField directorField = new JTextField();
        JTextField genreField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField durationField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField ratingField = new JTextField();
        JTextField imagePathField = new JTextField("default_movie.png"); 
        
        Object[] fields = {
            "Title:", titleField,
            "Director:", directorField,
            "Genre:", genreField,
            "Year:", yearField,
            "Duration (min):", durationField,
            "Price:", priceField,
            "Rating (1.0-5.0):", ratingField,
            "Image Filename (e.g., movie.jpg):", imagePathField 
        };
        
        int option = JOptionPane.showConfirmDialog(this, fields, "Add Movie", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            try {
                String title = titleField.getText();
                String director = directorField.getText();
                String genre = genreField.getText();
                int year = Integer.parseInt(yearField.getText());
                int duration = Integer.parseInt(durationField.getText());
                double price = Double.parseDouble(priceField.getText());
                double rating = Double.parseDouble(ratingField.getText());
                String imagePath = imagePathField.getText();
                
                // This call was already correct!
                boolean success = movieController.addMovie(
                    title, director, genre, year, duration, price, rating, imagePath
                );
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Movie added!");
                    loadMovies();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add movie");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Check all fields are valid: " + e.getMessage());
                e.printStackTrace(); 
            }
        }
    }
}