package controller;

import model.Movie;
import model.MovieDAO;
import java.util.List;

public class MovieController {

    private MovieDAO movieDAO;

    public MovieController() {
        this.movieDAO = new MovieDAO(); // The Controller creates the DAO
    }

    // The View calls this
    public List<Movie> getAllMovies() {
        return movieDAO.getAllMovies();
    }

    // The View calls this
    public List<Movie> searchMovies(String searchText) {
        return movieDAO.searchMovies(searchText);
    }

    // The View calls this
    public boolean addMovie(String title, String director, String genre, 
                            int year, int duration, double price, double rating, String imagePath) {
        
        // The Controller does the logic
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDirector(director);
        movie.setGenre(genre);
        movie.setReleaseYear(year);
        movie.setDuration(duration);
        movie.setRentalPrice(price);
        movie.setRating(rating);
        movie.setImagePath(imagePath.trim().isEmpty() ? "default_movie.png" : imagePath.trim());
        movie.setStatus("AVAILABLE");
        
        return movieDAO.addMovie(movie);
    }
}