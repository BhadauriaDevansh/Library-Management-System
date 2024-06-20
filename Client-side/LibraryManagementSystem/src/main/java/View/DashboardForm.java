package View;

import Controller.*;
import javax.swing.*;
import java.awt.*;

public class DashboardForm extends JFrame {
    public DashboardForm(MainController controller) {
        super("Dashboard");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 900);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton bookEntryButton = new JButton("Book Entry Form");
        JButton loanFormButton = new JButton("Loan Form");
        JButton logoutButton = new JButton("Logout");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(bookEntryButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(loanFormButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(logoutButton, gbc);

        add(panel, BorderLayout.CENTER);

        bookEntryButton.addActionListener(e -> controller.showBookEntryForm());
        loanFormButton.addActionListener(e -> controller.showLoanForm());
        logoutButton.addActionListener(e -> controller.showLoginForm());
    }
}

