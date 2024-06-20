# Library Management System

## Overview

The Library Management System is a Java-based application designed to manage books, members, and loan records in a library. Developed using Java and Swing for the frontend and Spring Boot for the backend services, it integrates MySQL for database management, ensuring efficient storage and retrieval of data. The system consists of two main components:

1. **Server-side Application (Backend)**: Provides RESTful APIs to manage books, members, and loans using Java, Jersey for RESTful services, and MySQL for data storage. It features functionality for managing book entries, loans, and member information with HTTP communication handled through Java’s HttpURLConnection. The backend uses Jetty for server deployment.
2. **Client-side Application (Frontend)**: A desktop-based application using Java Swing to provide a user interface for library staff to manage the library's inventory and loans.

This project showcases a seamless blend of desktop and web technologies to create a robust library management solution.


## Technologies Used

### Backend
- Java
- Jersey (for RESTful web services)
- MySQL (for data storage)
- Maven (for dependency management)

### Frontend
- Java
- Java Swing (for UI components)
- HTTP (for communication with the backend)

## Setup Instructions

### Prerequisites
- JDK 8 or higher
- Maven
- MySQL
- IDE (e.g., IntelliJ IDEA, Eclipse)

### Database Setup

1. **Create MySQL Database**
    ```sql
    CREATE DATABASE LibraryDB; // Replace with your MySQL Database Name
    USE LibraryDB;
    ```

2. **Create Tables**
    Use the provided `Create_tables.sql` file provied in Server-side/LibraryMainService to create the necessary tables in your database:
   

### Backend Setup

1. **Clone the Repository**
    ```bash
    git clone <your-repository-url>
    cd library-management-system/backend
    ```

2. **Update Database Configuration**
    Update the database URL, username, and password in your `LoanDatabase`, `BookDatabase`, and `MemberDatabase` classes.
    ```java
    private Connection connect() {
        String url = "jdbc:mysql://localhost:3306/LibraryDB"; // Replace with your MySQL Database Name
        String user = "root";
        String password = "YourPassword"; // Replace with your MySQL password
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    ```

3. **Build the Project**
    ```bash
    mvn clean install
    ```

4. **Run the Application**
    ```bash
    mvn exec:java -Dexec.mainClass="com.yourpackage.MainClass"
    ```

### Frontend Setup

1. **Clone the Repository**
    ```bash
    git clone <your-repository-url>
    cd library-management-system/frontend
    ```

2. **Run the Application**
    Open the project in your IDE and run the main class to start the desktop application.

## Features

### Backend Features
- **Books Management**
  - Add, update, delete, and retrieve books
- **Members Management**
  - Add, update, delete, and retrieve members
- **Loans Management**
  - Add, update, delete, and retrieve loans
  - Only display books not currently on loan in the dropdown for adding loans
  - Display all members in the dropdown for adding loans

### Frontend Features
- **Books Management UI**
  - Form for adding, updating, and deleting books
  - Table for displaying all books
- **Members Management UI**
  - Form for adding, updating, and deleting members
  - Table for displaying all members
- **Loans Management UI**
  - Form for adding, updating, and deleting loans
  - Table for displaying all loans
  - Dropdowns for book IDs and member IDs, with filtering for available books

## Usage

### Running the Backend

Ensure the backend server is running. You can test the endpoints using tools like Postman or through the frontend application.

### Using the Frontend

1. **Books Management**
    - Add, update, and delete books using the provided form.
    - View all books in the table.

2. **Members Management**
    - Add, update, and delete members using the provided form.
    - View all members in the table.

3. **Loans Management**
    - Add new loans by selecting an available book and a member from the dropdowns.
    - View all loan records in the table.
    - Update or delete existing loans as needed.

## RESTful Endpoints

The backend exposes several RESTful endpoints:

- **Books**
  - `GET /library/books`: Retrieve all books
  - `GET /library/books/{id}`: Retrieve a book by ID
  - `POST /library/books`: Add a new book
  - `PUT /library/books/{id}`: Update a book by ID
  - `DELETE /library/books/{id}`: Delete a book by ID

- **Members**
  - `GET /library/members`: Retrieve all members
  - `GET /library/members/{id}`: Retrieve a member by ID
  - `POST /library/members`: Add a new member
  - `PUT /library/members/{id}`: Update a member by ID
  - `DELETE /library/members/{id}`: Delete a member by ID

- **Loans**
  - `GET /library/loans`: Retrieve all loans
  - `GET /library/loans/{id}`: Retrieve a loan by ID
  - `POST /library/loans`: Add a new loan
  - `PUT /library/loans/{id}`: Update a loan by ID
  - `DELETE /library/loans/{id}`: Delete a loan by ID

## Contact

For any issues or questions, please open an issue in the repository or contact [Your Name] at [Your Email].
