package UI;

import dao.MovieDAO;
import dao.RentalDAO;
import model.Movie;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File; // Needed for File
import java.util.List;

public class MainApp extends JFrame {
    private User currentUser;
    private MovieDAO movieDAO;
    private RentalDAO rentalDAO;
    
    private JPanel movieGridPanel; // Panel to hold movie cards
    private JTextField searchField;
    
    public MainApp(User user) {
        this.currentUser = user;
        this.movieDAO = new MovieDAO();
        this.rentalDAO = new RentalDAO();
        setupUI();
        loadMovies();
    }
    
    private void setupUI() {
        setTitle("Movie Rental - Welcome " + currentUser.getName());
        setSize(1000, 700); // Larger size
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
        movieGridPanel = new JPanel(new GridLayout(0, 3, 15, 15)); // 3 columns, auto rows
        JScrollPane scrollPane = new JScrollPane(movieGridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom Panel: Buttons
     // Bottom Panel: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        // --- New logic for the history button ---
        JButton historyButton;
        if (currentUser.isAdmin()) {
            historyButton = new JButton("View All Rentals"); // Text for Admin
        } else {
            historyButton = new JButton("View My Rentals"); // Text for Customer
        }
        historyButton.addActionListener(e -> viewRentalHistory());
        buttonPanel.add(historyButton);
        // --- End of new logic ---

        if (currentUser.isAdmin()) {
            JButton addButton = new JButton("Add Movie");
            addButton.addActionListener(e -> addMovie());
            buttonPanel.add(addButton);
        }
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginPage();
        });
        buttonPanel.add(logoutButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        setVisible(true);
    }

    // Helper to create a single movie card
 // Helper to create a single movie card
    private JPanel createMovieCard(Movie movie) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        // Adjusted preferred height
        card.setPreferredSize(new Dimension(280, 380)); 
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // --- Image Panel (NORTH) ---
        // We put the imageLabel inside another panel to control its size
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        Dimension imageSize = new Dimension(150, 200); // Define a fixed image size
        
        String imagePath = movie.getImagePath();
        ImageIcon movieIcon = loadImage(imagePath, imageSize.width, imageSize.height); // Load and scale image
        
        JLabel imageLabel = new JLabel(movieIcon, JLabel.CENTER);
        // --- THIS IS THE KEY FIX ---
        // Force the image label to have a fixed size
        imageLabel.setPreferredSize(imageSize);
        imageLabel.setMinimumSize(imageSize);
        imageLabel.setMaximumSize(imageSize);
        // Add a border so you can see the placeholder
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); 
        
        imagePanel.add(imageLabel);
        card.add(imagePanel, BorderLayout.NORTH);

        // --- Details (CENTER) ---
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 3, 3));
        detailsPanel.setBorder(new EmptyBorder(5, 0, 0, 0)); 
        
        JLabel titleLabel = new JLabel(movie.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        detailsPanel.add(titleLabel);

        detailsPanel.add(createStarRating(movie.getRating()));
        detailsPanel.add(new JLabel("Genre: " + movie.getGenre()));
        detailsPanel.add(new JLabel("Price: ₹" + movie.getRentalPrice()));
        
        // Status Label
        JLabel statusLabel = new JLabel(movie.getStatus());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        if (movie.isAvailable()) {
            statusLabel.setForeground(new Color(0, 128, 0)); // Green
        } else {
            statusLabel.setForeground(Color.RED);
        }
        detailsPanel.add(statusLabel);
        
        card.add(detailsPanel, BorderLayout.CENTER);
        
        // --- Button (SOUTH) ---
        JButton rentButton = new JButton("Rent Movie");
        if (currentUser.isAdmin() || !movie.isAvailable()) {
            rentButton.setEnabled(false);
        }
        rentButton.addActionListener(e -> rentMovie(movie));
        card.add(rentButton, BorderLayout.SOUTH);
        
        return card;
    }
    // Helper to load and scale an image
 // Helper to load and scale an image
    private ImageIcon loadImage(String path, int width, int height) {
        // Look for the images folder in the project root
        String fullPath = "images" + File.separator + path;
        File file = new File(fullPath);
        ImageIcon icon;

        if (file.exists() && !file.isDirectory()) {
            // Load the specific movie image if it exists
            icon = new ImageIcon(file.getAbsolutePath());
        } else {
            // Fallback to the default image
            System.err.println("Cannot find image: " + fullPath + ". Loading default.");
            File defaultFile = new File("images" + File.separator + "default_movie.png");
            
            if(defaultFile.exists() && !defaultFile.isDirectory()) {
                icon = new ImageIcon(defaultFile.getAbsolutePath());
            } else {
                // If default is ALSO missing, print an error and use an empty icon
                System.err.println("CRITICAL: DEFAULT IMAGE MISSING from images/default_movie.png");
                // Create an empty, gray placeholder
                java.awt.image.BufferedImage placeholder = new java.awt.image.BufferedImage(
                    width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
                Graphics2D g = placeholder.createGraphics();
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, width, height);
                g.dispose();
                return new ImageIcon(placeholder);
            }
        }
        
        // Scale the loaded image
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    // Helper to create star rating text (keeping your last successful version)
 // Helper to create star rating text (using '*' to fix font issues)
    private JLabel createStarRating(double rating) {
        StringBuilder stars = new StringBuilder();
        
        // Round the rating to the nearest whole number
        int fullStars = (int) Math.round(rating);
        
        // Add '*' for each full star
        for (int i = 0; i < fullStars; i++) {
            stars.append("*");
        }
        
        // Add '-' for the remaining empty stars up to 5
        for (int i = fullStars; i < 5; i++) {
            stars.append("-");
        }
        
        // Create the label
        JLabel ratingLabel = new JLabel(stars.toString() + " (" + rating + ")");
        ratingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        ratingLabel.setForeground(new Color(245, 180, 0)); // Gold color
        return ratingLabel;
    }

    private void loadMovies() {
        List<Movie> movies = movieDAO.getAllMovies();
        showMovies(movies);
    }
    
    private void searchMovies() {
        String searchText = searchField.getText().trim();
        List<Movie> movies = movieDAO.searchMovies(searchText);
        showMovies(movies);
    }
    
    private void showMovies(List<Movie> movies) {
        movieGridPanel.removeAll(); // Clear old movies
        
        if (movies.isEmpty()) {
            movieGridPanel.add(new JLabel("No movies found."));
        } else {
            for (Movie movie : movies) {
                movieGridPanel.add(createMovieCard(movie));
            }
        }
        
        // This is important to ensure the layout recalculates correctly
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
            boolean success = rentalDAO.rentMovie(currentUser.getUserId(), movie.getMovieId(), movie.getRentalPrice());
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Movie rented successfully!");
                loadMovies(); // Refresh the movie list
            } else {
                JOptionPane.showMessageDialog(this, "Rental failed. Please try again.");
            }
        }
    }
    
// In UI/MainApp.java
    
    private void viewRentalHistory() {
        new RentalHistoryDialog(this, rentalDAO, currentUser);
        // --- ADD THIS LINE ---
        // After the dialog is closed, refresh the movie list
        loadMovies(); 
    }
    
    // Updated to include 'imagePath'
    private void addMovie() {
        JTextField titleField = new JTextField();
        JTextField directorField = new JTextField();
        JTextField genreField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField durationField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField ratingField = new JTextField();
        JTextField imagePathField = new JTextField("default_movie.png"); // New field with default
        
        Object[] fields = {
            "Title:", titleField,
            "Director:", directorField,
            "Genre:", genreField,
            "Year:", yearField,
            "Duration (min):", durationField,
            "Price:", priceField,
            "Rating (1.0-5.0):", ratingField,
            "Image Filename (e.g., movie.jpg):", imagePathField // New field
        };
        
        int option = JOptionPane.showConfirmDialog(this, fields, "Add Movie", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            try {
                Movie movie = new Movie();
                movie.setTitle(titleField.getText());
                movie.setDirector(directorField.getText());
                movie.setGenre(genreField.getText());
                movie.setReleaseYear(Integer.parseInt(yearField.getText()));
                movie.setDuration(Integer.parseInt(durationField.getText()));
                movie.setRentalPrice(Double.parseDouble(priceField.getText()));
                movie.setRating(Double.parseDouble(ratingField.getText()));
                movie.setImagePath(imagePathField.getText().trim().isEmpty() ? "default_movie.png" : imagePathField.getText().trim()); // Set imagePath
                movie.setStatus("AVAILABLE");
                
                if (movieDAO.addMovie(movie)) {
                    JOptionPane.showMessageDialog(this, "Movie added!");
                    loadMovies();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add movie");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Check all fields are valid: " + e.getMessage());
                e.printStackTrace(); // Print stack trace for debugging
            }
        }
    }
}