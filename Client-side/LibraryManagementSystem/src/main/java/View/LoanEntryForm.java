package View;

import Controller.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoanEntryForm extends JFrame {
    private final MainController controller;
    private JComboBox<Integer> bookIdComboBox;
    private JComboBox<Integer> memberIdComboBox;
    private JTextField loanIdField;
    private JTextField loanDateField;
    private JTextField returnDateField;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;

    public LoanEntryForm(MainController controller) {
        super("Loan Form");
        this.controller = controller;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST; // Align components to the left

        JLabel loanIdLabel = new JLabel("Loan ID:");
        loanIdField = new JTextField(10);
        JLabel loanDateLabel = new JLabel("Loan Date:");
        loanDateField = new JTextField(10);
        JLabel returnDateLabel = new JLabel("Return Date:");
        returnDateField = new JTextField(10);
        JButton addButton = new JButton("Add Loan");
        JButton deleteButton = new JButton("Delete Loan");
        JButton backButton = new JButton("Back to Main");

        // Initialize JComboBox for Book ID
        bookIdComboBox = new JComboBox<>();
        bookIdComboBox.setPreferredSize(new Dimension(200, 25));
        JLabel bookIdLabel = new JLabel("Book ID:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST; // Align to the right for labels
        panel.add(bookIdLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Align to the left for components
        panel.add(bookIdComboBox, gbc);

        // Initialize JComboBox for Member ID
        memberIdComboBox = new JComboBox<>();
        memberIdComboBox.setPreferredSize(new Dimension(200, 25));
        JLabel memberIdLabel = new JLabel("Member ID:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST; // Align to the right for labels
        panel.add(memberIdLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Align to the left for components
        panel.add(memberIdComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST; // Align to the right for labels
        panel.add(loanIdLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Align to the left for components
        panel.add(loanIdField, gbc);

        populateComboBoxes();

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST; // Align to the right for labels
        panel.add(loanDateLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Align to the left for components
        panel.add(loanDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST; // Align to the right for labels
        panel.add(returnDateLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Align to the left for components
        panel.add(returnDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(addButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(deleteButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(backButton, gbc);

        add(panel, BorderLayout.WEST);

        // Table for displaying loans
        tableModel = new DefaultTableModel(new String[]{"Loan ID", "Member ID", "Book ID", "Loan Date", "Return Date","Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true; // Allow all cells to be editable
            }
        };
        JTable loansTable = new JTable(tableModel);

        loansTable.getColumn("Actions").setCellRenderer(new LoanButtonRenderer());
        loansTable.getColumn("Actions").setCellEditor(new LoanButtonEditor(new JCheckBox(),loansTable,this));

        JScrollPane tableScrollPane = new JScrollPane(loansTable);

        JPanel tablePanel = new JPanel(new BorderLayout());

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Search by Loan ID:");
        searchField = new JTextField(10);
        searchButton = new JButton("Search");
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        addButton.addActionListener(e -> {
            try {
                int loanId = Integer.parseInt(loanIdField.getText());
                int memberId = (int) memberIdComboBox.getSelectedItem();
                int bookId = (int) bookIdComboBox.getSelectedItem();
                String loanDate = loanDateField.getText();
                String returnDate = returnDateField.getText();

                String jsonInputString = String.format(
                        "{\"loanId\":%d,\"memberId\":%d,\"bookId\":%d,\"loanDate\":\"%s\",\"returnDate\":\"%s\"}",
                        loanId, memberId, bookId, loanDate, returnDate);

                URL url = new URL("http://localhost:8080/library/loans");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int code = conn.getResponseCode();
                if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED) {
                    System.out.println("Loan added successfully!");
                    fetchAndDisplayLoans();
                    clearFields();
                    populateComboBoxes();
                } else {
                    System.out.println("Failed to add loan. Server responded with code: " + code);
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Loan ID, Member ID, and Book ID must be valid numbers", "Input Error", JOptionPane.ERROR_MESSAGE);
                nfe.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Request Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = loansTable.getSelectedRow();
            if (selectedRow >= 0) { // Check if a row is selected
                int loanId = (int) loansTable.getValueAt(selectedRow, 0); // Assuming Loan ID is in the first column
                deleteLoanById(loanId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a loan to delete.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> controller.showLoginForm());

        searchButton.addActionListener(e -> {
            String searchId = searchField.getText().trim();
            if (searchId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Loan ID", "Input Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    int loanId = Integer.parseInt(searchId);

                    URL url = new URL("http://localhost:8080/library/loans/" + loanId);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");

                    int code = conn.getResponseCode();
                    if (code == HttpURLConnection.HTTP_OK) {
                        // Read response
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // Parse JSON response
                        JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();

                        // Clear the table model
                        tableModel.setRowCount(0);

                        // Add the retrieved loan to the table model
                        Object[] rowData = new Object[]{
                                jsonObject.get("loanId").getAsInt(),
                                jsonObject.get("memberId").getAsInt(),
                                jsonObject.get("bookId").getAsInt(),
                                jsonObject.get("loanDate").getAsString(),
                                jsonObject.get("returnDate").getAsString()
                        };
                        tableModel.addRow(rowData);

                    } else if (code == HttpURLConnection.HTTP_NOT_FOUND) {
                        JOptionPane.showMessageDialog(this, "Loan ID not found", "Search Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        System.out.println("Failed to search loan. Server responded with code: " + code);
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this, "Loan ID must be a valid number", "Input Error", JOptionPane.ERROR_MESSAGE);
                    nfe.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Request Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        fetchAndDisplayLoans(); // Fetch and display loans when the form is initialized
    }

    private void populateComboBoxes() {
        try {
            // Fetch Book IDs from URL
            URL bookIdUrl = new URL("http://localhost:8080/library/books/available");
            HttpURLConnection bookIdConn = (HttpURLConnection) bookIdUrl.openConnection();
            bookIdConn.setRequestMethod("GET");
            bookIdConn.setRequestProperty("Accept", "application/json");

            int bookIdCode = bookIdConn.getResponseCode();
            if (bookIdCode == HttpURLConnection.HTTP_OK) {
                BufferedReader bookIdReader = new BufferedReader(new InputStreamReader(bookIdConn.getInputStream()));
                String bookIdInputLine;
                StringBuilder bookIdResponse = new StringBuilder();
                while ((bookIdInputLine = bookIdReader.readLine()) != null) {
                    bookIdResponse.append(bookIdInputLine);
                }
                bookIdReader.close();

                // Parse JSON response and populate the Book ID ComboBox
                JsonArray bookIdArray = JsonParser.parseString(bookIdResponse.toString()).getAsJsonArray();
                bookIdComboBox.removeAllItems(); // Clear existing items
                for (JsonElement element : bookIdArray) {
                    bookIdComboBox.addItem(element.getAsInt());
                }
            } else {
                System.out.println("Failed to fetch Book IDs. Server responded with code: " + bookIdCode);
            }

            // Fetch Member IDs from URL
            URL memberIdUrl = new URL("http://localhost:8080/library/members/ids");
            HttpURLConnection memberIdConn = (HttpURLConnection) memberIdUrl.openConnection();
            memberIdConn.setRequestMethod("GET");
            memberIdConn.setRequestProperty("Accept", "application/json");

            int memberIdCode = memberIdConn.getResponseCode();
            if (memberIdCode == HttpURLConnection.HTTP_OK) {
                BufferedReader memberIdReader = new BufferedReader(new InputStreamReader(memberIdConn.getInputStream()));
                String memberIdInputLine;
                StringBuilder memberIdResponse = new StringBuilder();
                while ((memberIdInputLine = memberIdReader.readLine()) != null) {
                    memberIdResponse.append(memberIdInputLine);
                }
                memberIdReader.close();

                // Parse JSON response and populate the Member ID ComboBox
                JsonArray memberIdArray = JsonParser.parseString(memberIdResponse.toString()).getAsJsonArray();
                memberIdComboBox.removeAllItems(); // Clear existing items
                for (JsonElement element : memberIdArray) {
                    memberIdComboBox.addItem(element.getAsInt());
                }
            } else {
                System.out.println("Failed to fetch Member IDs. Server responded with code: " + memberIdCode);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Request Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void fetchAndDisplayLoans() {
        try {
            URL url = new URL("http://localhost:8080/library/loans");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                // Read response
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response
                JsonArray jsonArray = JsonParser.parseString(response.toString()).getAsJsonArray();

                // Clear table before populating
                tableModel.setRowCount(0);

                // Populate table with retrieved data
                for (JsonElement element : jsonArray) {
                    JsonObject jsonObject = element.getAsJsonObject();
                    Object[] rowData = {
                            jsonObject.get("loanId").getAsInt(),
                            jsonObject.get("memberId").getAsInt(),
                            jsonObject.get("bookId").getAsInt(),
                            jsonObject.get("loanDate").getAsString(),
                            jsonObject.get("returnDate").getAsString()
                    };
                    tableModel.addRow(rowData);
                }
            } else {
                System.out.println("Failed to fetch loans. Server responded with code: " + code);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Request Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateLoan(int loanId, int memberId, int bookId, String loanDate, String returnDate) {
        try {
            URL url = new URL("http://localhost:8080/library/loans/" + loanId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Create a JSON string manually
            String jsonInputString = String.format(
                    "{\"loanId\":%d,\"memberId\":%d,\"bookId\":%d,\"loanDate\":\"%s\",\"returnDate\":\"%s\"}",
                    loanId, memberId, bookId, loanDate, returnDate);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_NO_CONTENT) {
                System.out.println("loan updated successfully!");
                fetchAndDisplayLoans();
            } else {
                System.out.println("Failed to update loan. Server responded with code: " + code);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Update Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteLoanById(int loanId) {
        try {
            URL url = new URL("http://localhost:8080/library/loans/" + loanId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_NO_CONTENT) {
                System.out.println("Loan deleted successfully!");
                fetchAndDisplayLoans();
            } else {
                System.out.println("Failed to delete loan. Server responded with code: " + code);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Request Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields(){
        loanIdField.setText("");
        loanDateField.setText("");
        returnDateField.setText("");
    }


}

class LoanButtonRenderer extends JButton implements TableCellRenderer {
    public LoanButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Update" : value.toString());
        return this;
    }
}

// Custom editor for the button column
class LoanButtonEditor extends DefaultCellEditor {
    private JButton button;
    private boolean isPushed;
    private String label;
    private JTable loansTable;
    private LoanEntryForm loanEntryForm;

    public LoanButtonEditor(JCheckBox checkBox, JTable loansTable, LoanEntryForm loanEntryForm) {
        super(checkBox);
        this.loansTable = loansTable;
        this.loanEntryForm = loanEntryForm; // Updated assignment

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
            int row = loansTable.getSelectedRow();
            int loanId = (int) loansTable.getValueAt(row, 0);
            int memberId = (int) loansTable.getValueAt(row, 1);
            int bookId = (int) loansTable.getValueAt(row, 2);
            String loanDate = (String) loansTable.getValueAt(row, 3);
            String returnDate = (String) loansTable.getValueAt(row, 4);

            // Call updateMember function from RegistrationForm
            loanEntryForm.updateLoan(loanId, memberId, bookId, loanDate, returnDate );
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