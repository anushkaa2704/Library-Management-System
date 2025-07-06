import rojeru_san.complementos.RSTableMetro;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.InputStream;
import java.sql.*;

public class UserDashboard extends JFrame
{
    DefaultTableModel booksModel;
    String currentUsername;
    JLabel issuedCountLabel;
    JLabel returnedCountLabel;


    UserDashboard(String username) {

        this.currentUsername = username;
        JPanel navpanel = new JPanel();
        navpanel.setBackground(new Color(102, 102, 255));
        navpanel.setBounds(0,0,1900,70);
        navpanel.setLayout(null);

        JLabel l2=new JLabel("Digital Library Management System");
        l2.setBounds(10,10,380,50);
        l2.setFont(new Font("Yu Gothic UI Light",Font.PLAIN,25));
        l2.setForeground(new Color(255,255,255));
        navpanel.add(l2);

        JLabel l4 = new JLabel("Welcome, " + currentUsername + "!");
        l4.setBounds(700, 15, 350, 40);// Right-aligned
        l4.setForeground(Color.WHITE);
        l4.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 25));

        try {
            InputStream is = getClass().getResourceAsStream("/adminIcons/male_user_50px.png");
            if (is != null) {
                ImageIcon icon = new ImageIcon(ImageIO.read(is));
                Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                l4.setIcon(new ImageIcon(img));
                l4.setHorizontalTextPosition(SwingConstants.RIGHT);
                l4.setVerticalTextPosition(SwingConstants.CENTER);
            } else {
                System.err.println("male_user_50px.png not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        navpanel.add(l4);

        JPanel mainpanel = new JPanel();
        mainpanel.setBounds(0,70,1000,930);
        mainpanel.setLayout(null);
        mainpanel.setBackground(new Color(255,255,255));
        int panelWidth = 250;
        int panelHeight = 140;
        int gap = 57;
        int startX = 75;
        int startY = 50;

        String[] topLabels = { "No. of Issued Books", "No. of Books Returned", "Total Fine Applicable" };
        String[] iconPaths = {
                "/adminIcons/icons8_Book_Shelf_50px.png",
                "/adminIcons/icons8_Sell_50px.png",
                "/adminIcons/icons8_List_of_Thumbnails_50px.png"
        };

        for (int i = 0; i < 3; i++) {
            JLabel titleLabel = new JLabel(topLabels[i]);
            titleLabel.setFont(new Font("Yu Gothic UI", Font.BOLD, 18));
            titleLabel.setBounds(startX + i * (panelWidth + gap), startY - 30, panelWidth, 30);
            mainpanel.add(titleLabel);

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(null);
            infoPanel.setBounds(startX + i * (panelWidth + gap), startY, panelWidth, panelHeight);
            infoPanel.setBackground(new Color(220, 220, 220));
            infoPanel.setBorder(BorderFactory.createMatteBorder(5, 0, 0, 0, new Color(51, 51, 255)));

            JLabel iconLabel = new JLabel();
            iconLabel.setBounds(40, 40, 60, 60);
            try {
                InputStream iconStream = getClass().getResourceAsStream(iconPaths[i]);
                if (iconStream != null) {
                    ImageIcon icon = new ImageIcon(ImageIO.read(iconStream));
                    Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    iconLabel.setIcon(new ImageIcon(img));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            JLabel numberLabel;
            if (i == 0) {
                issuedCountLabel = new JLabel("0");
                numberLabel = issuedCountLabel;
            } else if (i == 1) {
                returnedCountLabel = new JLabel("0");
                numberLabel = returnedCountLabel;
            } else {
                numberLabel = new JLabel("0");
            }

            numberLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 40));
            numberLabel.setBounds(120, 40, 100, 60);

            infoPanel.add(iconLabel);
            infoPanel.add(numberLabel);
            mainpanel.add(infoPanel);
        }


        JLabel tabletitle = new JLabel("Available Books");
        tabletitle.setFont( new Font("Calibri", Font.BOLD, 24));
        tabletitle.setBounds(420, 260, 600, 40);
        mainpanel.add(tabletitle);

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


        DefaultTableModel bookModel = new DefaultTableModel();
        bookModel.setColumnIdentifiers(new Object[]{"Book Name", "Author", "Availability","Status"});

        booksTable.setModel(bookModel);
        loadBooksToTable(bookModel);
        updateBookCounts();


        JScrollPane studentScroll = new JScrollPane(booksTable);
        studentScroll.setBounds(200, 300, 600, 220); // ↓ reduced height here
        studentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainpanel.add(studentScroll);

        JButton issueBtn = new JButton("Request Issue");
        issueBtn.setBounds(200, 570, 180, 60);
        issueBtn.setBackground(new Color(0, 0, 204));
        issueBtn.setForeground(Color.WHITE);
        issueBtn.setFocusPainted(false);
        issueBtn.setFont(new Font("Calibri", Font.BOLD, 22));
        mainpanel.add(issueBtn);


        JButton returnBtn = new JButton("Return");
        returnBtn.setBounds(400, 570, 180, 60);
        returnBtn.setBackground(new Color(0, 0, 204));
        returnBtn.setForeground(Color.WHITE);
        returnBtn.setFocusPainted(false);
        returnBtn.setFont(new Font("Calibri", Font.BOLD, 22));
        mainpanel.add(returnBtn);

        returnBtn.addActionListener(e -> {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a book to return!");
                return;
            }

            String bookName = (String) bookModel.getValueAt(selectedRow, 0);
            String currentStatus = (String) bookModel.getValueAt(selectedRow, 3);

            if (!"Issued".equalsIgnoreCase(currentStatus)) {
                JOptionPane.showMessageDialog(this, "Only issued books can be returned.");
                return;
            }

            try {
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704");

                // Get current date
                java.util.Date today = new java.util.Date();
                java.sql.Date returnDate = new java.sql.Date(today.getTime());

                // Step 1: Update status and return_date in request_issue
                String updateSQL = "UPDATE request_issue SET status = 'Returned', return_date = ? WHERE username = ? AND bookname = ? AND status = 'Issued'";
                PreparedStatement updatePst = con.prepareStatement(updateSQL);
                updatePst.setDate(1, returnDate);
                updatePst.setString(2, currentUsername);
                updatePst.setString(3, bookName);

                int updated = updatePst.executeUpdate();

                if (updated > 0) {
                    // Step 2: Increment book quantity
                    String incrementSQL = "UPDATE availablebooks SET quantity = quantity + 1 WHERE name = ?";
                    PreparedStatement incPst = con.prepareStatement(incrementSQL);
                    incPst.setString(1, bookName);
                    incPst.executeUpdate();


                    bookModel.setValueAt("Returned", selectedRow, 3);
                    loadBooksToTable(bookModel);
                    updateBookCounts();


                    JOptionPane.showMessageDialog(this, "Book returned successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to return the book.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                updatePst.close();
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error while returning book.");
            }
        });




        issueBtn.addActionListener(e -> {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a book first!");
                return;
            }

            String bookName = (String) bookModel.getValueAt(selectedRow, 0);
            String author = (String) bookModel.getValueAt(selectedRow, 1);
            String availability = (String) bookModel.getValueAt(selectedRow, 2);
            String currentStatus = (String) bookModel.getValueAt(selectedRow, 3);

            if (!availability.equalsIgnoreCase("Available")) {
                JOptionPane.showMessageDialog(this, "This book is not available.");
                return;
            }

            if (!currentStatus.equals("-")) {
                JOptionPane.showMessageDialog(this, "You have already requested this book.");
                return;
            }

            java.util.Date today = new java.util.Date();
            java.sql.Date requestDate = new java.sql.Date(today.getTime());

            try {
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704");

                String sql = "INSERT INTO request_issue (username, bookname, author, request_date, status) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, currentUsername);
                pst.setString(2, bookName);
                pst.setString(3, author);
                pst.setDate(4, requestDate);
                pst.setString(5, "Pending");

                int result = pst.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Issue request sent successfully!");
                    bookModel.setValueAt("Pending", selectedRow, 3);
                    loadBooksToTable(bookModel);
                    updateBookCounts();

                } else {
                    JOptionPane.showMessageDialog(this, "Failed to send request.");
                }

                pst.close();
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error occurred while sending request.");
            }
        });



        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(600, 570, 180, 60);
        logoutBtn.setBackground(new Color(0, 0, 204));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Calibri", Font.BOLD, 22));
        logoutBtn.setFocusPainted(false);
        mainpanel.add(logoutBtn);

        logoutBtn.addActionListener(a->{
            new LoginPage();
            dispose();
        });

        Container c = getContentPane();
        c.setLayout(null);

        c.add(navpanel);
        c.add(mainpanel);

        setTitle("User Dashboard");
        setSize(1000, 800);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Absolute positioning
        setResizable(true); // Allow resizing
        setVisible(true);


    }


    public static void main(String[] args)
    {
        new UserDashboard("Anushkaa");
    }
    public void loadBooksToTable(DefaultTableModel model) {
        try {
            model.setRowCount(0); // Clear table

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704");

            // Step 1: Fetch ALL requests by this user (not just pending)
            PreparedStatement statusStmt = con.prepareStatement(
                    "SELECT bookname, status FROM request_issue WHERE username = ?");
            statusStmt.setString(1, currentUsername);
            ResultSet statusRs = statusStmt.executeQuery();

            // Store bookname → status (Pending or Issued)
            java.util.Map<String, String> bookStatusMap = new java.util.HashMap<>();
            while (statusRs.next()) {
                bookStatusMap.put(statusRs.getString("bookname"), statusRs.getString("status"));
            }

            // Step 2: Load available books
            String sql = "SELECT name, author, quantity FROM availablebooks";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");

                String availability = quantity >= 1 ? "Available" : "Not Available";
                String status = bookStatusMap.getOrDefault(name, "-"); // Shows "Pending", "Issued", or "-"

                model.addRow(new Object[]{name, author, availability, status});
            }

            rs.close();
            pst.close();
            statusRs.close();
            statusStmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading books", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public int getIssuedBooksCount(String username) {
        int count = 0;
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704");

            String sql = "SELECT COUNT(*) FROM request_issue WHERE username = ? AND status = 'Issued'";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getReturnedBooksCount(String username) {
        int count = 0;
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704");

            String sql = "SELECT COUNT(*) FROM request_issue WHERE username = ? AND status = 'Returned'";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    private void updateBookCounts() {
        issuedCountLabel.setText(String.valueOf(getIssuedBooksCount(currentUsername)));
        returnedCountLabel.setText(String.valueOf(getReturnedBooksCount(currentUsername)));
    }

}
