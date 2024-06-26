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

## Database Setup

1. **Create MySQL Database**
    ```sql
    CREATE DATABASE LibraryDB; // Replace with your MySQL Database Name
    USE LibraryDB;
    ```

2. **Create Tables**
    Use the provided `Create_tables.sql` file provied in Server-side/LibraryMainService to create the necessary tables in your database:
   

## Project Setup

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
## Running the Complete Application

After cloning the repository and setting up both the backend and frontend as described, follow these steps to run the entire Library Management System:

1. **Start the Backend**
    Navigate to the `Server-side` directory and run the main class to start the Spring Boot backend services. This will establish the necessary connections and endpoints for the application.
    ```bash
    cd Library-Management-System/Server-side
    mvn exec:java -Dexec.mainClass="Application.Main"
    ```

2. **Start the Frontend**
    Navigate to the `Client-side` directory, open the project in your preferred IDE, and execute `MainController.java` to launch the Swing-based desktop application. Ensure that the server-side application is 
    running to allow the frontend to interact with the backend services.
    ```bash
    cd ../Client-side
    open in IDE and run MainController.java
    ```

By following these steps, you'll have a fully functional Library Management System that seamlessly integrates desktop and web technologies to manage library resources efficiently.

## Features

Ensure the backend server is running. You can test the endpoints using tools like Postman or through the frontend application.

### Backend Features
- **Books Management**
  - Add, update, delete, and retrieve books 
- **Members Management**
  - Add, update, delete,login authentication and retrieve members
- **Loans Management**
  - Add, update, delete, and retrieve loans
  - Only display books not currently on loan in the dropdown for adding loans
  - Display all members in the dropdown for adding loans

  ### Frontend Features
1. **Books Management UI**
    - Add, update, and delete books using the provided form.
    - View all books in the table.

2. **Members Management UI**
    - Add, update, and delete members using the provided form.
    - View all members in the table.
    - User authentication login form which authenticates registered users by verifying credentials with security encryption.

3. **Loans Management UI**
    - Add new loans by selecting an available book and a member from the dropdowns.
    - View all loan records in the table.
    - Update or delete existing loans as needed.


## RESTful Endpoints

The backend exposes several RESTful endpoints:

- **Books**
  - `GET /library/books`: Retrieve all books
  - `GET /library/books/{id}`: Retrieve a book by ID
  - `GET /library/books/available`: Retrieve only the books that are available for loan and have not been issued to any member.
  - `POST /library/books`: Add a new book
  - `PUT /library/books/{id}`: Update a book by ID
  - `DELETE /library/books/{bookId}`: Delete a book by ID

- **Members**
  - `GET /library/members`: Retrieve all members
  - `GET /library/members/{id}`: Retrieve a member by ID
  - `GET /library/members/ids`: Retrieve all members ID use for loan implementation 
  - `POST /library/members/login`: Authenticate registered users by verifying credentials with security encryption.
  - `POST /library/members`: Add a new member
  - `PUT /library/members/{id}`: Update a member by ID
  - `DELETE /library/members/{memberId}`: Delete a member by ID

- **Loans**
  - `GET /library/loans`: Retrieve all loans
  - `GET /library/loans/{id}`: Retrieve a loan by ID
  - `POST /library/loans`: Add a new loan
  - `PUT /library/loans/{id}`: Update a loan by ID
  - `DELETE /library/loans/{loanId}`: Delete a loan by ID

## Contact

For any issues or questions, please open an issue in the repository or contact **Devansh Bhadauria** at devanshbhadauria4070@gmail.com
