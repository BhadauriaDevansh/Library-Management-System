package Model;

import java.util.Objects;

public class Member {
    private int memberId;
    private String memberName;
    private String memberUsername;
    private String memberEmail;
    private int memberPhone;
    private String membership_date;
    private String memberPassword;
    private String memberConfirmPassword;



    public Member(){}

    public Member(int memberId, String memberName, String memberUsername, String memberEmail, int memberPhone, String membership_date, String memberPassword, String memberConfirmPassword) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberUsername = memberUsername;
        this.memberEmail = memberEmail;
        this.memberPhone = memberPhone;
        this.membership_date = membership_date;
        this.memberPassword = memberPassword;
        this.memberConfirmPassword = memberConfirmPassword;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberUsername() {
        return memberUsername;
    }

    public void setMemberUsername(String memberUsername) {
        this.memberUsername = memberUsername;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public int getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(int memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getMembership_date() {
        return membership_date;
    }

    public void setMembership_date(String membership_date) {
        this.membership_date = membership_date;
    }

    public String getMemberPassword() {
        return memberPassword;
    }

    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }

    public String getMemberConfirmPassword() {
        return memberConfirmPassword;
    }

    public void setMemberConfirmPassword(String memberConfirmPassword) {
        this.memberConfirmPassword = memberConfirmPassword;
    }
}