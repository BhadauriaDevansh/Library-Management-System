package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDatabase {
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

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT memberId,memberName,memberUsername,memberEmail,memberPhone,membership_date FROM members";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getInt("memberId"));
                member.setMemberName(rs.getString("memberName"));
                member.setMemberUsername(rs.getString("memberUsername"));
                member.setMemberEmail(rs.getString("memberEmail"));
                member.setMemberPhone(rs.getInt("memberPhone"));
                member.setMembership_date(rs.getString("membership_date"));
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    public Member getMemberById(int id) {
        Member member = null;
        String sql = "SELECT memberId,memberName,memberUsername,memberEmail,memberPhone,membership_date FROM members WHERE memberId = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    member = new Member();
                    member.setMemberId(rs.getInt("memberId"));
                    member.setMemberName(rs.getString("memberName"));
                    member.setMemberUsername(rs.getString("memberUsername"));
                    member.setMemberEmail(rs.getString("memberEmail"));
                    member.setMemberPhone(rs.getInt("memberPhone"));
                    member.setMembership_date(rs.getString("membership_date"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return member;
    }

    public Member loginUser(String username, String password) {
        Member member = null;
        String sql = "SELECT memberId, memberName, memberEmail, memberPhone, membership_date, memberUsername FROM members WHERE memberUsername = ? AND memberPassword = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    member = new Member();
                    member.setMemberId(rs.getInt("memberId"));
                    member.setMemberName(rs.getString("memberName"));
                    member.setMemberUsername(rs.getString("memberUsername"));
                    member.setMemberEmail(rs.getString("memberEmail"));
                    member.setMemberPhone(rs.getInt("memberPhone"));
                    member.setMembership_date(rs.getString("membership_date"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return member;
    }


    public void addMember(Member member) {
        String sql = "INSERT INTO members (memberId,memberName, memberUsername, memberEmail, memberPhone, membership_date,memberPassword,memberConfirmPassword) VALUES (?,?, ?, ?, ?, ?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, member.getMemberId());
            pstmt.setString(2, member.getMemberName());
            pstmt.setString(3,member.getMemberUsername());
            pstmt.setString(4, member.getMemberEmail());
            pstmt.setInt(5, member.getMemberPhone());
            pstmt.setString(6, member.getMembership_date());
            pstmt.setString(7, member.getMemberPassword());
            pstmt.setString(8, member.getMemberConfirmPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMember(Member member) {
        String sql = "UPDATE members SET memberId = ?, memberName = ?, memberUsername = ?, memberEmail = ?, memberPhone = ?, membership_date = ? WHERE memberId = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, member.getMemberId());
            pstmt.setString(2, member.getMemberName());
            pstmt.setString(3, member.getMemberUsername());
            pstmt.setString(4, member.getMemberEmail());
            pstmt.setInt(5, member.getMemberPhone());
            pstmt.setString(6, member.getMembership_date());
            pstmt.setInt(7, member.getMemberId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMember(int memberId) {
        String loansSql = "DELETE FROM loans WHERE memberId = ?";
        String booksSql = "DELETE FROM members WHERE memberId = ?";

        try (Connection conn = this.connect();
             PreparedStatement loansStmt = conn.prepareStatement(loansSql);
             PreparedStatement booksStmt = conn.prepareStatement(booksSql)) {

            // Delete related records in loans table
            loansStmt.setInt(1, memberId);
            loansStmt.executeUpdate();

            // Delete the book record itself
            booksStmt.setInt(1, memberId);
            booksStmt.executeUpdate();

            System.out.println("Member deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getAllMemberIds() {
        List<Integer> memberIds = new ArrayList<>();
        String sql = "SELECT memberId FROM members";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                memberIds.add(rs.getInt("memberId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return memberIds;
    }
}
