import rojeru_san.complementos.RSTableMetro;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

public class ManageBooks extends JFrame {
    JTextField bookIdField, bookNameField, authorField, quantityField;
    DefaultTableModel booksModel;

    ManageBooks()
    {
        Font headingFont = new Font("Calibri", Font.BOLD, 28);
        Font subHeadingFont = new Font("Calibri", Font.PLAIN, 18);
        Font fieldFont = new Font("Calibri", Font.PLAIN, 16);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(102, 102, 255));
        leftPanel.setBounds(0, 0, 450, 800);
        leftPanel.setLayout(null);


        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.white);
        rightPanel.setBounds(450, 0, 750, 800);
        rightPanel.setLayout(null);

        JLabel headinglabel=new JLabel("Manage Books");
        headinglabel.setFont(headingFont);
        headinglabel.setBounds(300,60,450,45);
        rightPanel.add(headinglabel);

        String[] labels = {"Enter Book Id:", "Enter Book Name:", "Author Name", "Quantity"};
        String[] iconPaths = {"/AddNewBookIcons/icons8_Contact_26px.png", "/AddNewBookIcons/icons8_Moleskine_26px.png", "/AddNewBookIcons/icons8_Collaborator_Male_26px.png", "/AddNewBookIcons/icons8_Unit_26px.png"};
        int y = 120;

        for (int i = 0; i < labels.length; i++) {
            try {
                InputStream iconStream = getClass().getResourceAsStream(iconPaths[i]);
                if (iconStream != null) {
                    ImageIcon icon = new ImageIcon(ImageIO.read(iconStream));
                    JLabel iconLabel = new JLabel(icon);
                    iconLabel.setBounds(40, y + 5, 45, 45);
                    leftPanel.add(iconLabel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            JLabel lbl = new JLabel(labels[i]);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(subHeadingFont);
            lbl.setBounds(100, y, 150, 20);
            leftPanel.add(lbl);

            JTextField tf;
            if(i == 0) tf = bookIdField = new JTextField();
            else if(i == 1) tf = bookNameField = new JTextField();
            else if(i == 2) tf = authorField = new JTextField();
            else tf = quantityField = new JTextField();

            tf.setFont(fieldFont);
            tf.setBounds(100, y + 20, 250, 30);
            tf.setBackground(new Color(102, 102, 255));
            tf.setForeground(Color.WHITE);
            tf.setCaretColor(Color.WHITE);
            tf.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
            leftPanel.add(tf);

            y += 90;
        }



        JButton addBtn = new JButton("Add");
        addBtn.setBounds(20, y+20, 130, 50);
        addBtn.setBackground(new Color(0, 0, 204));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setFont(subHeadingFont);
        leftPanel.add(addBtn);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(160, y+20, 130, 50);
        updateBtn.setBackground(new Color(0, 0, 204));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFont(subHeadingFont);
        updateBtn.setFocusPainted(false);
        leftPanel.add(updateBtn);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(300, y+20, 130, 50);
        deleteBtn.setBackground(new Color(0, 0, 204));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setFont(subHeadingFont);
        leftPanel.add(deleteBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(160, y+110, 130, 50);
        backBtn.setBackground(new Color(255, 51, 51));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(subHeadingFont);
        backBtn.setFocusPainted(false);
        leftPanel.add(backBtn);
        backBtn.addActionListener(a->{
            dispose();
            new AdminDash();
        });



        booksModel = new DefaultTableModel();
        booksModel.setColumnIdentifiers(new Object[]{"Book ID", "Name", "Author", "Quantity"});
        RSTableMetro booksTable = new RSTableMetro();
        booksTable.setRowHeight(30);
        booksTable.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
        booksTable.setColorBackgoundHead(new Color(51, 51, 255));
        booksTable.setColorFilasForeground1(Color.BLACK);
        booksTable.setColorFilasForeground2(Color.BLACK);
        booksTable.setColorFilasBackgound1(new Color(230, 230, 255));
        booksTable.setColorFilasBackgound2(new Color(210, 210, 255));
        booksTable.setSelectionBackground(new Color(102, 102, 255));
        booksTable.setShowHorizontalLines(true);
        booksTable.setShowVerticalLines(true);
        booksTable.setModel(booksModel);

        loadBooksToTable(booksModel);



        JScrollPane studentScroll = new JScrollPane(booksTable);
        studentScroll.setBounds(65, 250, 600, 220); // ↓ reduced height here
        studentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        rightPanel.add(studentScroll);



        addBtn.addActionListener(a->{
            String bookId = bookIdField.getText().trim();
            String name = bookNameField.getText().trim();
            String author = authorField.getText().trim();
            String quantity = quantityField.getText().trim();

            if (bookId.isEmpty() || name.isEmpty() || author.isEmpty() || quantity.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultTableModel model = (DefaultTableModel) booksTable.getModel();
            model.addRow(new Object[]{bookId, name, author, quantity});

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                java.sql.Connection con = java.sql.DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704");

                String sql = "INSERT INTO availablebooks (book_id, name, author, quantity) VALUES (?, ?, ?, ?)";
                java.sql.PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, bookId);
                pst.setString(2, name);
                pst.setString(3, author);
                pst.setInt(4, Integer.parseInt(quantity));

                int row = pst.executeUpdate();
                if (row > 0) {
                    JOptionPane.showMessageDialog(this, "Book Added Successfully");
                }

                pst.close();
                con.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error in Adding Book", "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteBtn.addActionListener(a->
        {
            int selectedRow = booksTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a book to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String bookId = booksModel.getValueAt(selectedRow, 0).toString();
            String url="jdbc:mysql://localhost:3306/librarymanagementsystem";
            try(Connection conn=DriverManager.getConnection(url,"root","Apatil#2704"))
            {
                String sql="DELETE FROM availablebooks WHERE book_id = ?;";
                try(PreparedStatement pst = conn.prepareStatement(sql)) {
                    pst.setString(1, bookId);
                    int rowsAffected = pst.executeUpdate();
                    if (rowsAffected > 0) {
                        booksModel.removeRow(selectedRow); // Remove row from table
                        JOptionPane.showMessageDialog(null, "Book deleted successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Book not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });

        updateBtn.addActionListener(a -> {
            int selectedRow = booksTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a book to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String bookId = bookIdField.getText().trim();
            String name = bookNameField.getText().trim();
            String author = authorField.getText().trim();
            String quantityStr = quantityField.getText().trim();

            if (bookId.isEmpty() || name.isEmpty() || author.isEmpty() || quantityStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Incomplete Data", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int quantity = Integer.parseInt(quantityStr); // Ensure quantity is numeric

                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704");

                String sql = "UPDATE availablebooks SET name = ?, author = ?, quantity = ? WHERE book_id = ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, name);
                pst.setString(2, author);
                pst.setInt(3, quantity);
                pst.setString(4, bookId);

                int rowsUpdated = pst.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Book updated successfully.");
                    loadBooksToTable(booksModel); // refresh table
                } else {
                    JOptionPane.showMessageDialog(this, "No book found.", "Update Failed", JOptionPane.ERROR_MESSAGE);
                }

                pst.close();
                con.close();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantity must be a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });



        Container c=getContentPane();
        c.setLayout(null);
        c.add(leftPanel);
        c.add(rightPanel);

        setTitle("Manage Books");
        setSize(1200, 800);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Absolute positioning
        setResizable(true); // Allow resizing
        setVisible(true);
    }

    public static void main(String[] args) {
        new ManageBooks();
    }
    public void loadBooksToTable(DefaultTableModel model) {
        try {
            model.setRowCount(0); // Clear existing rows in the table

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704");

            String sql = "SELECT * FROM availablebooks";
            PreparedStatement pst = con.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String bookId = rs.getString("book_id");
                String name = rs.getString("name");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");

                model.addRow(new Object[]{bookId, name, author, quantity});
            }

            rs.close();
            pst.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load books", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
