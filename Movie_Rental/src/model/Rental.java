import java.sql.Date;

public class Rental {
    private int rentalId;
    private int movieId;
    private int userId;
    private Date rentalDate;
    private Date returnDate;
    private Date dueDate;
    private String movieTitle;
    private String userName;
    
    // Constructor
    public Rental(int rentalId, int movieId, int userId, Date rentalDate, 
                  Date returnDate, Date dueDate) {
        this.rentalId = rentalId;
        this.movieId = movieId;
        this.userId = userId;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.dueDate = dueDate;
    }
    
    // Getters and Setters
    public int getRentalId() { return rentalId; }
    public void setRentalId(int rentalId) { this.rentalId = rentalId; }
    
    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public Date getRentalDate() { return rentalDate; }
    public void setRentalDate(Date rentalDate) { this.rentalDate = rentalDate; }
    
    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
    
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    
    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}
