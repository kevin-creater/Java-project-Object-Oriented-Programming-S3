package controller;

import model.RentalDAO;
import model.User;
import javax.swing.table.DefaultTableModel;

public class RentalController {

    private RentalDAO rentalDAO;

    public RentalController() {
        this.rentalDAO = new RentalDAO();
    }

    public boolean rentMovie(int userId, int movieId, double price) {
        return rentalDAO.rentMovie(userId, movieId, price);
    }

    public boolean returnMovie(int rentalId, int movieId) {
        return rentalDAO.returnMovie(rentalId, movieId);
    }

    public DefaultTableModel getRentalHistory(User user) {
        if (user.isAdmin()) {
            return rentalDAO.getAllRentalHistory();
        } else {
            return rentalDAO.getRentalHistory(user.getUserId());
        }
    }
}