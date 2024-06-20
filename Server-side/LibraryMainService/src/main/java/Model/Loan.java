package Model;

import java.util.Date;

public class Loan {

    private int loanId;
    private int bookId;
    private int memberId;
    private String loanDate;
    private String returnDate;


    public Loan() {
    }

    public Loan(int loanId, int bookId, int memberId, String loanDate, String returnDate) {
        this.loanId = loanId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }


    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
