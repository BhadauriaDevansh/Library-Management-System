package View;

import Controller.MainController;
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

public class BookEntryForm extends JFrame {
    private final MainController controller;
    private DefaultTableModel tableModel;
    private JTable bookTable;
    private JTextField bookIdField;
    private JTextField bookTitleField;
    private JTextField returnDateField;
    private JTextField authorField;
    private JTextField bookIsbnField;
    private JTextField dateField;
    private JTextField quantityField;


    public BookEntryForm(MainController controller) {
        super("Book Entry Form");
        this.controller = controller;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1700, 900);
        setLayout(new GridLayout(1, 2));

        JPanel entryPanel = new JPanel(new GridBagLayout());
        JPanel tablePanel = new JPanel(new BorderLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel bookIdLabel = new JLabel("Book ID:");
        bookIdField = new JTextField(10); // Reduced size
        JLabel bookTitleLabel = new JLabel("Book Title:");
        bookTitleField = new JTextField(10); // Reduced size
        JLabel authorLabel = new JLabel("Author:");
        authorField = new JTextField(10); // Reduced size
        JLabel bookIsbnLabel = new JLabel("ISBN:");
        bookIsbnField = new JTextField(10); // Reduced size
        JLabel dateLabel = new JLabel("Published Date:");
        dateField = new JTextField(10); // Reduced size
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField(10); // Reduced size
        JButton addButton = new JButton("Add Book");
        JButton deleteButton = new JButton("Delete Book");
        JButton backButton = new JButton("Back to Dashboard");

        gbc.gridx = 0;
        gbc.gridy = 0;
        entryPanel.add(bookIdLabel, gbc);
        gbc.gridx = 1;
        entryPanel.add(bookIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        entryPanel.add(bookTitleLabel, gbc);
        gbc.gridx = 1;
        entryPanel.add(bookTitleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        entryPanel.add(authorLabel, gbc);
        gbc.gridx = 1;
        entryPanel.add(authorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        entryPanel.add(bookIsbnLabel, gbc);
        gbc.gridx = 1;
        entryPanel.add(bookIsbnField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        entryPanel.add(dateLabel, gbc);
        gbc.gridx = 1;
        entryPanel.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        entryPanel.add(quantityLabel, gbc);
        gbc.gridx = 1;
        entryPanel.add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        entryPanel.add(addButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        entryPanel.add(deleteButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        entryPanel.add(backButton, gbc);

        add(entryPanel);


        // Add search bar and search button to the table panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchIdLabel = new JLabel("Search Book ID:");
        JTextField searchIdField = new JTextField(10);
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchIdLabel);
        searchPanel.add(searchIdField);
        searchPanel.add(searchButton);
        tablePanel.add(searchPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Title", "Author", "ISBN", "Published Date", "Quantity", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true; // Allow all cells to be editable
            }
        };

        JTable bookTable = new JTable(tableModel);

        // Add custom renderer and editor for the "Actions" column
        bookTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        bookTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), bookTable, this));



        JScrollPane scrollPane = new JScrollPane(bookTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel);

        backButton.addActionListener(e -> controller.showDashboardForm());

        addButton.addActionListener(e -> {
            try {
                int bookId = Integer.parseInt(bookIdField.getText());
                String title = bookTitleField.getText();
                String author = authorField.getText();
                String isbn = bookIsbnField.getText();
                String published_date = dateField.getText();
                String quantity = quantityField.getText();

                String jsonInputString = String.format(
                        "{\"bookId\":%d,\"title\":\"%s\",\"author\":\"%s\",\"isbn\":\"%s\",\"published_date\":\"%s\",\"quantity\":\"%s\"}",
                        bookId, title, author, isbn, published_date, quantity);

                URL url = new URL("http://localhost:8080/library/books");
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
                    System.out.println("Book added successfully!");
                } else {
                    System.out.println("Failed to add book. Server responded with code: " + code);
                }
                clearFields();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Book ID must be an integer", "Input Error", JOptionPane.ERROR_MESSAGE);
                nfe.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Request Error", JOptionPane.ERROR_MESSAGE);
            }
            fetchAndDisplayBooks();



        });

        searchButton.addActionListener(e -> {
            String input = searchIdField.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid book ID.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int bookId = Integer.parseInt(input);
                fetchAndDisplayBookById(bookId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid book ID.", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow >= 0) {
                int bookId = (int) tableModel.getValueAt(selectedRow, 0);
                deleteBookById(bookId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to delete.", "Delete Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        fetchAndDisplayBooks();

    }

    private void fetchAndDisplayBooks() {
        try {
            URL url = new URL("http://localhost:8080/library/books");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            br.close();

            // Parse JSON response
            JsonArray booksArray = JsonParser.parseString(response.toString()).getAsJsonArray();

            // Update the table model
            tableModel.setRowCount(0); // Clear existing rows
            for (JsonElement bookElement : booksArray) {
                JsonObject book = bookElement.getAsJsonObject();
                tableModel.addRow(new Object[]{
                        book.get("bookId").getAsInt(),
                        book.get("title").getAsString(),
                        book.get("author").getAsString(),
                        book.get("isbn").getAsString(),
                        book.get("published_date").getAsString(),
                        book.get("quantity").getAsString(),
                        "Update"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching books: " + e.getMessage(), "Fetch Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchAndDisplayBookById(int bookId) {
        try {
            URL url = new URL("http://localhost:8080/library/books/" + bookId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            br.close();

            // Parse JSON response
            JsonObject book = JsonParser.parseString(response.toString()).getAsJsonObject();

            // Update the table model
            tableModel.setRowCount(0); // Clear existing rows
            tableModel.addRow(new Object[]{
                    book.get("bookId").getAsInt(),
                    book.get("title").getAsString(),
                    book.get("author").getAsString(),
                    book.get("isbn").getAsString(),
                    book.get("published_date").getAsString(),
                    book.get("quantity").getAsString(),
                    "Update"
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching book: " + e.getMessage(), "Fetch Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateBook(int bookId, String title, String author, String isbn, String publishedDate, String quantity) {
        try {
            URL url = new URL("http://localhost:8080/library/books/" + bookId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Create a JSON string manually
            String jsonInputString = String.format(
                    "{\"bookId\":%d,\"title\":\"%s\",\"author\":\"%s\",\"isbn\":\"%s\",\"published_date\":\"%s\",\"quantity\":\"%s\"}",
                    bookId, title, author, isbn, publishedDate, quantity);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_NO_CONTENT) {
                System.out.println("Book updated successfully!");
                fetchAndDisplayBooks();
            } else {
                System.out.println("Failed to update book. Server responded with code: " + code);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Update Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBookById(int bookId) {
        try {
            URL url = new URL("http://localhost:8080/library/books/" + bookId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_NO_CONTENT || code == HttpURLConnection.HTTP_OK) {
                System.out.println("Book deleted successfully!");
                fetchAndDisplayBooks();
            } else {
                System.out.println("Failed to delete book. Server responded with code: " + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting book: " + e.getMessage(), "Delete Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void clearFields() {
        bookIdField.setText("");
        bookTitleField.setText("");
        authorField.setText("");
        bookIsbnField.setText("");
        dateField.setText("");
        quantityField.setText("");
    }
}


class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Update" : value.toString());
        return this;
    }
}

// Custom editor for the button column
class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable bookTable;
    private BookEntryForm bookEntryForm; // Added bookEntryForm field

    public ButtonEditor(JCheckBox checkBox, JTable bookTable, BookEntryForm bookEntryForm) { // Modified constructor
        super(checkBox);
        this.bookTable = bookTable;
        this.bookEntryForm = bookEntryForm; // Initialize bookEntryForm
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
            int row = bookTable.getSelectedRow();

            int bookId = (int) bookTable.getValueAt(row, 0);
            String title = (String) bookTable.getValueAt(row, 1);
            String author = (String) bookTable.getValueAt(row, 2);
            String isbn = (String) bookTable.getValueAt(row, 3);
            String publishedDate = (String) bookTable.getValueAt(row, 4);
            String quantity = (String) bookTable.getValueAt(row, 5);

            // Update the database with the edited values
            bookEntryForm.updateBook(bookId, title, author, isbn, publishedDate, quantity);
        }
        isPushed = false;
        return label;
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
