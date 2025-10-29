package model;

public class Movie {
    private int movieId;
    private String title;
    private String director;
    private String genre;
    private int releaseYear;
    private int duration;
    private String status;
    private double rentalPrice;
    private double rating;
    private String imagePath; // <-- HERE IS THE NEW FIELD

    // Updated constructor
    public Movie(int movieId, String title, String director, String genre, 
                 int releaseYear, int duration, String status, double rentalPrice, double rating, String imagePath) {
        this.movieId = movieId;
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.duration = duration;
        this.status = status;
        this.rentalPrice = rentalPrice;
        this.rating = rating;
        this.imagePath = imagePath; // <-- INITIALIZE IT
    }

    // Default constructor for 'addMovie'
    public Movie() {
        this.imagePath = "default_movie.png"; // Default image path
    }

    // --- Getters ---
    public int getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public String getDirector() { return director; }
    public String getGenre() { return genre; }
    public int getReleaseYear() { return releaseYear; }
    public int getDuration() { return duration; }
    public String getStatus() { return status; }
    public double getRentalPrice() { return rentalPrice; }
    public double getRating() { return rating; }
    public String getImagePath() { return imagePath; } // <-- HERE IS THE NEW GETTER

    // --- Setters ---
    public void setMovieId(int movieId) { this.movieId = movieId; }
    public void setTitle(String title) { this.title = title; }
    public void setDirector(String director) { this.director = director; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setStatus(String status) { this.status = status; }
    public void setRentalPrice(double rentalPrice) { this.rentalPrice = rentalPrice; }
    public void setRating(double rating) { this.rating = rating; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; } // <-- HERE IS THE NEW SETTER
    
    public boolean isAvailable() {
        return "AVAILABLE".equals(status);
    }
}

