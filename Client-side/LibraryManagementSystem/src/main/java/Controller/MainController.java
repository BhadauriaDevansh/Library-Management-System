package Controller;

import View.*;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MainController {
    private static final String URL = "jdbc:mysql://localhost:3306/librarydb";
    private static final String USER = "root";
    private static final String PASSWORD = "Devesh@74";
    private Connection connection;

    private JFrame currentForm;
    private LoginForm loginForm;
    private DashboardForm dashboardForm;
    private RegistrationForm registrationForm;
    private BookEntryForm bookEntryForm;
    private LoanEntryForm loanEntryForm;

    public MainController() {
        loginForm = new LoginForm(this);
        dashboardForm = new DashboardForm(this);
        registrationForm = new RegistrationForm(this);
        bookEntryForm = new BookEntryForm(this);
        loanEntryForm = new LoanEntryForm(this);

        connectToDatabase();
    }

    public void showLoginForm() {
        hideCurrentForm();
        currentForm = loginForm;
        loginForm.setVisible(true);
    }

    public void showDashboardForm() {
        hideCurrentForm();
        currentForm = dashboardForm;
        dashboardForm.setVisible(true);
    }

    public void showRegistrationForm() {
        hideCurrentForm();
        currentForm = registrationForm;
        registrationForm.setVisible(true);
    }

    public void showBookEntryForm() {
        hideCurrentForm();
        currentForm = bookEntryForm;
        bookEntryForm.setVisible(true);
    }

    public void showLoanForm() {
        hideCurrentForm();
        currentForm = loanEntryForm;
        loanEntryForm.setVisible(true);
    }

    private void hideCurrentForm() {
        if (currentForm != null) {
            currentForm.setVisible(false);
        }
    }

    public void start() {
        showLoginForm();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainController controller = new MainController();
            controller.start();
        });
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database.");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
