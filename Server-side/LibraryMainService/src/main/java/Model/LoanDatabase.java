package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDatabase {

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

    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setLoanId(rs.getInt("loanId"));
                loan.setBookId(rs.getInt("bookId"));
                loan.setMemberId(rs.getInt("memberId"));
                loan.setLoanDate(rs.getString("loanDate"));
                loan.setReturnDate(rs.getString("returnDate"));
                loans.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    public boolean addLoan(Loan loan) {
        String sql = "INSERT INTO loans (loanId, bookId, memberId, loanDate, returnDate) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loan.getLoanId());
            pstmt.setInt(2, loan.getBookId());
            pstmt.setInt(3, loan.getMemberId());
            pstmt.setString(4, loan.getLoanDate());
            pstmt.setString(5, loan.getReturnDate());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Loan getLoanById(int id) {
        Loan loan = null;
        String sql = "SELECT loanId, bookId, memberId, loanDate, returnDate FROM loans WHERE loanId = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    loan = new Loan();
                    loan.setLoanId(rs.getInt("loanId"));
                    loan.setBookId(rs.getInt("bookId"));
                    loan.setMemberId(rs.getInt("memberId"));
                    loan.setLoanDate(rs.getString("loanDate"));
                    loan.setReturnDate(rs.getString("returnDate"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loan;
    }

    public boolean updateLoan(Loan loan) {
        String sql = "UPDATE loans SET loanId = ?, bookId = ?, memberId = ?, loanDate = ?, returnDate = ? WHERE loanId = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loan.getLoanId());
            pstmt.setInt(2, loan.getBookId());
            pstmt.setInt(3, loan.getMemberId());
            pstmt.setString(4, loan.getLoanDate());
            pstmt.setString(5, loan.getReturnDate());
            pstmt.setInt(6, loan.getLoanId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteLoan(int loanId) {
        String sql = "DELETE FROM loans WHERE loanId = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loanId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
