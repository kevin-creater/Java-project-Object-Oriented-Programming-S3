package model;

import model.DatabaseConnection;
import model.Movie;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
    
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Movie movie = new Movie(
                    rs.getInt("movie_id"),
                    rs.getString("title"),
                    rs.getString("director"),
                    rs.getString("genre"),
                    rs.getInt("release_year"),
                    rs.getInt("duration"),
                    rs.getString("status"),
                    rs.getDouble("rental_price"),
                    rs.getDouble("rating"),
                    rs.getString("image_path") // Get imagePath
                );
                movies.add(movie);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return movies;
    }
    
    public List<Movie> searchMovies(String searchText) {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies WHERE title LIKE ? OR director LIKE ? OR genre LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String search = "%" + searchText + "%";
            stmt.setString(1, search);
            stmt.setString(2, search);
            stmt.setString(3, search);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Movie movie = new Movie(
                    rs.getInt("movie_id"),
                    rs.getString("title"),
                    rs.getString("director"),
                    rs.getString("genre"),
                    rs.getInt("release_year"),
                    rs.getInt("duration"),
                    rs.getString("status"),
                    rs.getDouble("rental_price"),
                    rs.getDouble("rating"),
                    rs.getString("image_path") // Get imagePath
                );
                movies.add(movie);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return movies;
    }
    
    public boolean addMovie(Movie movie) {
        // Added 'image_path'
        String sql = "INSERT INTO movies (title, director, genre, release_year, duration, rental_price, rating, image_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getDirector());
            stmt.setString(3, movie.getGenre());
            stmt.setInt(4, movie.getReleaseYear());
            stmt.setInt(5, movie.getDuration());
            stmt.setDouble(6, movie.getRentalPrice());
            stmt.setDouble(7, movie.getRating());
            stmt.setString(8, movie.getImagePath()); // Set imagePath
            
            int result = stmt.executeUpdate();
            return result > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateMovieStatus(int movieId, String status) {
        String sql = "UPDATE movies SET status = ? WHERE movie_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, movieId);
            
            int result = stmt.executeUpdate();
            return result > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}