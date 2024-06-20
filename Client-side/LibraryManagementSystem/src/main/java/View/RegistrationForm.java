package View;

import Controller.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RegistrationForm extends JFrame {
    private JTextField idField;
    private JTextField nameTextField;
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField membership_dateField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private DefaultTableModel tableModel;
    private MainController controller;
    private JTextField searchField;
    private JButton searchButton;

    public RegistrationForm(MainController controller) {
        super("Registration Form");
        this.controller = controller;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField(10);
        JLabel nameLabel = new JLabel("Name:");
        nameTextField = new JTextField(10);
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        JLabel phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField(10);
        JLabel membership_dateLabel = new JLabel("Membership Date:");
        membership_dateField = new JTextField(10);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JPasswordField(15);
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Login");
        JButton deleteButton = new JButton("Delete");

        JButton showPasswordButton = new JButton("Show");
        showPasswordButton.setPreferredSize(new Dimension(80, 25));
        JButton showConfirmPasswordButton = new JButton("Show");
        showConfirmPasswordButton.setPreferredSize(new Dimension(80, 25));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(idLabel, gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(nameTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(membership_dateLabel, gbc);
        gbc.gridx = 1;
        panel.add(membership_dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        gbc.gridx = 2;
        panel.add(showPasswordButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(confirmPasswordLabel, gbc);
        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);
        gbc.gridx = 2;
        panel.add(showConfirmPasswordButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        panel.add(deleteButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 19;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(backButton, gbc);

        add(panel, BorderLayout.WEST);

        // Table for displaying members
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Username", "Email", "Phone", "Membership_date", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true; // Allow all cells to be editable
            }
        };
        JTable membersTable = new JTable(tableModel);

        membersTable.getColumn("Actions").setCellRenderer(new RegisterButtonRenderer());
        membersTable.getColumn("Actions").setCellEditor(new RegisterButtonEditor(new JCheckBox(),membersTable,this));

        JScrollPane tableScrollPane = new JScrollPane(membersTable);

        JPanel tablePanel = new JPanel(new BorderLayout());

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Search by ID:");
        searchField = new JTextField(10);
        searchButton = new JButton("Search");
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        registerButton.addActionListener(e -> {
            try {
                int memberId = Integer.parseInt(idField.getText());
                String memberName = nameTextField.getText();
                String memberUsername = usernameField.getText();
                String memberEmail = emailField.getText();
                int memberPhone = Integer.parseInt(phoneField.getText());
                String membership_date = membership_dateField.getText();
                String memberPassword = String.valueOf(passwordField.getPassword());
                String memberConfirmPassword = String.valueOf(confirmPasswordField.getPassword());

                String jsonInputString = String.format(
                        "{\"memberId\":%d,\"memberName\":\"%s\",\"memberUsername\":\"%s\",\"memberEmail\":\"%s\",\"memberPhone\":%d,\"membership_date\":\"%s\",\"memberPassword\":\"%s\",\"memberConfirmPassword\":\"%s\"}",
                        memberId, memberName, memberUsername, memberEmail, memberPhone, membership_date, memberPassword, memberConfirmPassword);

                URL url = new URL("http://localhost:8080/library/members");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int code = conn.getResponseCode();
                if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED) {
                    System.out.println("Member added successfully!");
                    fetchAndDisplayMembers();
                    clearFields();
                } else {
                    System.out.println("Failed to add member. Server responded with code: " + code);
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "ID and Phone number must be valid numbers", "Input Error", JOptionPane.ERROR_MESSAGE);
                nfe.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Request Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        showPasswordButton.addActionListener(new ActionListener() {
            private boolean isPasswordVisible = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPasswordVisible) {
                    passwordField.setEchoChar('*');
                    showPasswordButton.setText("Show");
                } else {
                    passwordField.setEchoChar((char) 0);
                    showPasswordButton.setText("Hide");
                }
                isPasswordVisible = !isPasswordVisible;
            }
        });

        backButton.addActionListener(e -> controller.showLoginForm());

        showConfirmPasswordButton.addActionListener(new ActionListener() {
            private boolean isPasswordVisible = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPasswordVisible) {
                    confirmPasswordField.setEchoChar('*');
                    showConfirmPasswordButton.setText("Show");
                } else {
                    confirmPasswordField.setEchoChar((char) 0);
                    showConfirmPasswordButton.setText("Hide");
                }
                isPasswordVisible = !isPasswordVisible;
            }
        });

        searchButton.addActionListener(e -> {
            String searchId = searchField.getText().trim();
            if (searchId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid ID", "Input Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    int memberId = Integer.parseInt(searchId);
                    searchAndDisplayMemberById(memberId);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this, "ID must be a valid number", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        fetchAndDisplayMembers(); // Fetch and display members initially

        deleteButton.addActionListener(e -> {
            int selectedRow = membersTable.getSelectedRow();
            if (selectedRow >=0) { // Check if a row is selected
                int memberId = (int) membersTable.getValueAt(selectedRow, 0); // Assuming ID is in the first column
                deleteMemberById(memberId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a member to delete.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }


    private void fetchAndDisplayMembers() {
        try {
            URL url = new URL("http://localhost:8080/library/members");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JsonArray jsonArray = JsonParser.parseString(response.toString()).getAsJsonArray();
                tableModel.setRowCount(0); // Clear existing rows

                for (JsonElement element : jsonArray) {
                    JsonObject jsonObject = element.getAsJsonObject();
                    int memberId = jsonObject.get("memberId").getAsInt();
                    String memberName = jsonObject.get("memberName").getAsString();
                    String memberUsername = jsonObject.get("memberUsername").getAsString();
                    String memberEmail = jsonObject.get("memberEmail").getAsString();
                    int memberPhone = jsonObject.get("memberPhone").getAsInt();
                    String membership_date = jsonObject.get("membership_date").getAsString();

                    tableModel.addRow(new Object[]{memberId, memberName, memberUsername, memberEmail, memberPhone, membership_date});
                }
            } else {
                System.out.println("Failed to fetch members. Server responded with code: " + conn.getResponseCode());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Request Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchAndDisplayMemberById(int memberId) {
        try {
            URL url = new URL("http://localhost:8080/library/members/" + memberId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
                tableModel.setRowCount(0); // Clear existing rows

                int id = jsonObject.get("memberId").getAsInt();
                String name = jsonObject.get("memberName").getAsString();
                String username = jsonObject.get("memberUsername").getAsString();
                String email = jsonObject.get("memberEmail").getAsString();
                int phone = jsonObject.get("memberPhone").getAsInt();
                String membershipDate = jsonObject.get("membership_date").getAsString();

                tableModel.addRow(new Object[]{id, name, username, email, phone, membershipDate});
            } else {
                JOptionPane.showMessageDialog(this, "Member ID not found", "Search Error", JOptionPane.ERROR_MESSAGE);
                 // Clear table if member not found
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Request Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateMember(int memberId, String memberName, String memberUsername, String memberEmail, int memberPhone, String membership_date) {
        try {
            URL url = new URL("http://localhost:8080/library/members/" + memberId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Create a JSON string manually
            String jsonInputString = String.format(
                    "{\"memberId\":%d,\"memberName\":\"%s\",\"memberUsername\":\"%s\",\"memberEmail\":\"%s\",\"memberPhone\":%d,\"membership_date\":\"%s\"}",
                    memberId, memberName, memberUsername, memberEmail, memberPhone, membership_date);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_NO_CONTENT) {
                System.out.println("Member updated successfully!");
                fetchAndDisplayMembers();
            } else {
                System.out.println("Failed to update member. Server responded with code: " + code);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Update Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMemberById(int memberId) {
        try {
            URL url = new URL("http://localhost:8080/library/members/" + memberId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_NO_CONTENT || code == HttpURLConnection.HTTP_OK) {
                System.out.println("Member deleted successfully!");
                fetchAndDisplayMembers();
            } else {
                System.out.println("Failed to delete member. Server responded with code: " + code);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Delete Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        idField.setText("");
        nameTextField.setText("");
        usernameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        membership_dateField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }


}

class RegisterButtonRenderer extends JButton implements TableCellRenderer {
    public RegisterButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Update" : value.toString());
        return this;
    }
}

// Custom editor for the button column
class RegisterButtonEditor extends DefaultCellEditor {
    private JButton button;
    private boolean isPushed;
    private String label;
    private JTable membersTable;
    private RegistrationForm registrationForm;

    public RegisterButtonEditor(JCheckBox checkBox, JTable membersTable, RegistrationForm registrationForm) {
        super(checkBox);
        this.membersTable = membersTable;
        this.registrationForm = registrationForm; // Updated assignment

        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "Update" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            int row = membersTable.getSelectedRow();
            int memberId = (int) membersTable.getValueAt(row, 0);
            String memberName = (String) membersTable.getValueAt(row, 1);
            String memberUsername = (String) membersTable.getValueAt(row, 2);
            String memberEmail = (String) membersTable.getValueAt(row, 3);
            int memberPhone = (int) membersTable.getValueAt(row, 4);
            String membershipDate = (String) membersTable.getValueAt(row, 5);

            // Call updateMember function from RegistrationForm
            registrationForm.updateMember(memberId, memberName, memberUsername, memberEmail, memberPhone, membershipDate);
        }
        isPushed = false;
        return button.getText();
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}




