package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDatabase {
    private Connection connect() {
        String url = "jdbc:mysql://localhost:3306/LibraryDB";
        String user = "root";
        String password = "Devesh@74";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("bookId"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setPublished_date(rs.getString("published_date"));
                book.setQuantity(rs.getString("quantity"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE bookId = ?";
        Book book = null;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    book = new Book();
                    book.setBookId(rs.getInt("bookId"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setPublished_date(rs.getString("published_date"));
                    book.setQuantity(rs.getString("quantity"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    public void addBook(Book book) {
        String sql = "INSERT INTO books (bookId, title, author, isbn, published_date, quantity) VALUES (?,?,?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, book.getBookId());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getIsbn());
            pstmt.setString(5, book.getPublished_date());
            pstmt.setString(6, book.getQuantity());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(int bookId) {
        String loansSql = "DELETE FROM loans WHERE bookId = ?";
        String booksSql = "DELETE FROM books WHERE bookId = ?";

        try (Connection conn = this.connect();
             PreparedStatement loansStmt = conn.prepareStatement(loansSql);
             PreparedStatement booksStmt = conn.prepareStatement(booksSql)) {

            // Delete related records in loans table
            loansStmt.setInt(1, bookId);
            loansStmt.executeUpdate();

            // Delete the book record itself
            booksStmt.setInt(1, bookId);
            booksStmt.executeUpdate();

            System.out.println("Book deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, published_date = ?, quantity = ? WHERE bookId = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setString(4, book.getPublished_date());
            pstmt.setString(5, book.getQuantity());
            pstmt.setInt(6, book.getBookId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getAvailableBookIds() {
        List<Integer> availableBookIds = new ArrayList<>();
        String sql = "SELECT bookId FROM books WHERE bookId NOT IN (SELECT bookId FROM loans)";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                availableBookIds.add(rs.getInt("bookId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableBookIds;
    }
}
