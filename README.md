# 🍿 Movie Rental System

Welcome to the Movie Rental System! This is a complete desktop application built with Java Swing that simulates a real-world video rental store. It features a clean, user-friendly interface and a robust backend powered by a MySQL database.

This project features secure, separate interfaces for both regular **Customers** and system **Administrators**, each with different permissions and capabilities. It is built using the **Model-View-Controller (MVC)** design pattern to ensure clean, scalable, and maintainable code.

## 📸 Screenshots

(Add your own screenshots to these placeholders!)

| Login Page | Customer Dashboard | Admin Rental History |
| :---: | :---: | :---: |
| ![Login Page](screenshots/login.png) | ![Customer Dashboard](screenshots/dashboard.png) | ![Admin History](screenshots/admin_history.png) |

## ✨ Features

* **Secure Authentication:** Separate login and registration system for Customers and Admins.
* **Movie Catalog:** Browse the full movie catalog with images, ratings, genres, and prices.
* **Search Functionality:** Instantly search the catalog by title, director, or genre.
* **Customer Portal:**
    * Rent available movies.
    * View personal rental history (active and returned).
    * Return movies to make them available again.
* **Admin Portal:**
    * **Add Movies:** Easily add new movies, including poster images, to the system.
    * **View All Transactions:** Monitor all rentals and returns from all users.

## 🛠️ Tech Stack

![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

* **Frontend:** **Java Swing** (GUI)
* **Backend:** Java (JDK 11+)
* **Database:** MySQL
* **Driver:** MySQL Connector/J (JDBC)
* **Architecture:** Model-View-Controller (MVC)

## 🚀 Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

You will need the following software installed on your machine:
* [Java JDK 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or newer
* [MySQL Server](https://dev.mysql.com/downloads/mysql/)
* An IDE (like [Eclipse](https://www.eclipse.org/) or [IntelliJ IDEA](https://www.jetbrains.com/idea/))

### Installation

1.  **Clone the repo**
    ```sh
    https://github.com/kevinptom/Java-project-Object-Oriented-Programming-S3.git
    ```

2.  **Database Setup**
    * Start your MySQL server.
    * Create a new database named `movie_rental_system`.
    * **Important:** You must **export your local database schema** into an `.sql` file (e.g., `database.sql`) and add it to the repository. Then, import this file into your new `movie_rental_system` database to create all the tables (`movies`, `users`, `rentals`).

3.  **Configure Database Connection**
    * Open the project in your IDE.
    * Navigate to `src/model/DatabaseConnection.java`.
    * Update the `USERNAME` and `PASSWORD` constants to match your local MySQL credentials.
    ```java
    // src/model/DatabaseConnection.java
    
    private static final String URL = "jdbc:mysql://localhost:3306/movie_rental_system";
    private static final String USERNAME = "root"; // Your MySQL username
    private static final String PASSWORD = "your_password"; // Your MySQL password
    ```

4.  **Add Image Assets**
    * In the root directory of the project, create a new folder named `images`.
    * Add all your movie poster `.jpg` or `.png` files into this folder.
    * **Crucial:** You must add a file named `default_movie.png` to this folder. It will be used as a fallback if a specific movie's poster is not found.

5.  **Run the Application**
    * Compile the project.
    * Run the `src/view/LoginPage.java` file to start the application.

## 🧑‍💻 How to Use

1.  **Admin:** Log in using the 'ADMIN' role. (You may need to manually add an admin user to your `users` table first). You can now add movies and view all rentals.
2.  **Customer:** Register a new 'CUSTOMER' account from the login page.
3.  **Browse & Rent:** Log in as the customer, browse the movie list, and click "Rent Movie" on any available title.
4.  **View & Return:** Go to "View My Rentals" to see your rental history. You can select an "ACTIVE" rental and click "Return Selected Movie."

## 📁 Project Structure

This project follows the MVC design pattern:

```
src/
├── controller/   // Handles logic between View and Model
│   ├── MovieController.java
│   ├── RentalController.java
│   └── UserController.java
├── model/        // Data, Business Logic, and Database
│   ├── Movie.java
│   ├── Rental.java
│   ├── User.java
│   ├── MovieDAO.java
│   ├── RentalDAO.java
│   ├── UserDAO.java
│   └── DatabaseConnection.java
└── view/         // All GUI components (JFrames, JDialogs)
    ├── LoginPage.java
    ├── MainApp.java
    ├── RegistrationDialog.java
    └── RentalHistoryDialog.java
```

## 👥 Contributors

A big thanks to everyone who contributed to this project.

| Name | GitHub | Role |
| :--- | :--- | :--- |
| **[Your Name]** | `[@your-github-username]` | Lead Developer |
| [Contributor 2] | `[@github-user]` | [Role] |
| [Contributor 3] | `[@github-user]` | [Role] |


