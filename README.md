# 🍿 Java Movie Rental System 🎬

A complete desktop movie rental application built with Java. This project uses Java Swing for the UI, MySQL for database management, and a 3-tier (MVC) architecture.

![Java](https://img.shields.io/badge/Java-11%2B-ED8B00?style=for-the-badge&logo=java)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Swing](https://img.shields.io/badge/Java_Swing-UI-30A96B?style=for-the-badge&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAARVJREFUOE/Fk79qg0AYxV/fE1EYBGmNIoJXiCAKDsEmdPBKxJ1bB1dx8gIuHkUHx2JwcAmiYxG8gj25gIuVUqCIH+3y5l2WOfN933c/DBCSn7yP51EwzT8XElL+FwBqGgYdF8B+BVCG8S/sRqAAYBfABwBfAHYAnAA4ANTQePPECbQBgA/gNwGgBwB+gHMBVIBWq0G73QYgGo2K8Hw+h8PhwNPpBDabDcZisHAY/+u+728gCGJxnhcMBoNKpRLS6TSKoxhmj/sC+Ww2GyQSiSgWi6B/gL/cH9VqFeB0OiEUCkGlUoHT6QRarRYymQx2u100Gg18Pt/mD+B1YV0Vw+l0QlEUoVqtorquQBAEVFUVz/O0Wq1im7ZtMxuBOQFf3mHwG8/DUAAAAABJRU5ErkJggg==)

---

## ✨ Features

* **Secure Authentication:** Separate login and registration for **Customers** and **Admins**.
* **Admin Dashboard:**
    * View all rentals from all users.
    * Add new movies to the database, including title, genre, rating, and image path.
* **Customer Dashboard:**
    * Browse and search for movies by title, director, or genre.
    * View movie details in a clean card layout, including cover images.
    * Rent movies (status changes to 'RENTED').
    * View personal rental history ("My Rentals").
    * Return movies (status changes to 'AVAILABLE').
* **Robust Backend:** Transactional SQL queries ensure that renting a movie correctly updates both the `rentals` and `movies` tables.

---

## 📸 Screenshots

A quick look at the application's interface.

| Login Page | Main Dashboard | Rental History |
| :---: | :---: | :---: |
| ![Login Screen](images/login.png) | ![Main App Screen](images/login.png) | ![History Screen](images/.png) |

*(**Pro-tip:** Take screenshots and place them in an `images` folder in your repository, then update the paths above!)*

---

## 🚀 How to Run

### 1. Database Setup
1.  Make sure you have a **MySQL** server running.
2.  Open your MySQL client (like Workbench) and run the `schema.sql` file provided in this repository. (If you don't have one, **use the script below**).
3.  This will create the `movie_rental_system` database and all required tables.
4.  **Important:** Edit `db/DatabaseConnection.java` to match your local MySQL username and password.

### 2. Project Setup
1.  **Clone** this repository.
2.  **Add the MySQL JDBC Driver:** Download the `mysql-connector-j-X.X.XX.jar` file and add it to your project's build path in your IDE (Eclipse, IntelliJ, etc.).
3.  **Image Folder:** Create an `images` folder in the root of the project. Add a `default_movie.png` file to it.

### 3. Run
* Find and run the `main()` method in **`UI/LoginPage.java`**.

### Default Admin Login
* **Username:** `admin`
* **Password:** `admin123`

---

## 📋 SQL Schema

Use this script to set up your database if you're starting fresh.

```sql
-- Create the database
CREATE DATABASE IF NOT EXISTS movie_rental_system;
USE movie_rental_system;

-- Create the users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15),
    user_type VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER'
);

-- Create the movies table
CREATE TABLE IF NOT EXISTS movies (
    movie_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    director VARCHAR(100),
    genre VARCHAR(50),
    release_year INT,
    duration INT,
    status VARCHAR(20) DEFAULT 'AVAILABLE',
    rental_price DOUBLE,
    rating DOUBLE,
    image_path VARCHAR(255) DEFAULT 'default_movie.png'
);

-- Create the rentals table
CREATE TABLE IF NOT EXISTS rentals (
    rental_id INT AUTO_INCREMENT PRIMARY KEY,
    movie_id INT,
    user_id INT,
    rental_date DATETIME,
    due_date DATETIME,
    return_date DATETIME,
    total_amount DOUBLE,
    status VARCHAR(20),
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Create a default ADMIN user
INSERT IGNORE INTO users (username, password, name, email, phone, user_type)
VALUES ('admin', 'admin123', 'Admin User', 'admin@app.com', '1234567890', 'ADMIN');

-- Add a sample movie
INSERT IGNORE INTO movies (title, director, genre, release_year, duration, status, rental_price, rating, image_path)
VALUES ('The Shawshank Redemption', 'Frank Darabont', 'Drama', 1994, 142, 'AVAILABLE', 3.99, 5.0, 'default_movie.png'),
       ('The Godfather', 'Francis Ford Coppola', 'Crime', 1972, 175, 'AVAILABLE', 4.50, 4.9, 'default_movie.png'),
       ('The Dark Knight', 'Christopher Nolan', 'Action', 2008, 152, 'AVAILABLE', 4.99, 4.8, 'default_movie.png');
```
