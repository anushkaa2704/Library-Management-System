import rojeru_san.complementos.RSTableMetro;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class IssueBook extends JFrame {
    DefaultTableModel booksModel;
    IssueBook()
    {

        Font headingFont = new Font("Calibri", Font.BOLD, 28);
        Font subHeadingFont = new Font("Calibri", Font.PLAIN, 18);
        Font fieldFont = new Font("Calibri", Font.PLAIN, 16);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.white);
        mainPanel.setBounds(0, 0, 900, 700);
        mainPanel.setLayout(null);

        JLabel headinglabel=new JLabel("Book Issue Requests");
        headinglabel.setFont(headingFont);
        headinglabel.setBounds(350,60,450,45);
        mainPanel.add(headinglabel);

        booksModel = new DefaultTableModel();
        booksModel.setColumnIdentifiers(new Object[]{"Student Name", "Book Name", "Quantity","Request Date","Status"});
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

        JScrollPane studentScroll = new JScrollPane(booksTable);
        studentScroll.setBounds(65, 150, 800, 220); // ↓ reduced height here
        studentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainPanel.add(studentScroll);

        JButton issueBtn = new JButton("Approve Request");
        issueBtn.setBounds(250, 450, 200, 60);
        issueBtn.setBackground(new Color(0, 0, 204));
        issueBtn.setForeground(Color.WHITE);
        issueBtn.setFocusPainted(false);
        issueBtn.setFont(new Font("Calibri", Font.BOLD, 22));
        mainPanel.add(issueBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(500, 450, 200, 60);
        backBtn.setBackground(new Color(0, 0, 204));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Calibri", Font.BOLD, 22));
        backBtn.setFocusPainted(false);
        mainPanel.add(backBtn);

        backBtn.addActionListener(a->
        {
            new AdminDash();
            dispose();
        });

        issueBtn.addActionListener(a -> {
            int selectedRow = booksTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a request to approve.");
                return;
            }

            String username = (String) booksModel.getValueAt(selectedRow, 0);
            String bookName = (String) booksModel.getValueAt(selectedRow, 1);
            int quantity = (int) booksModel.getValueAt(selectedRow, 2);
            String status = (String) booksModel.getValueAt(selectedRow, 4);

            if (!status.equalsIgnoreCase("Pending")) {
                JOptionPane.showMessageDialog(this, "Only 'Pending' requests can be approved.");
                return;
            }

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Book is not available.");
                return;
            }

            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704");

                // 1. Update status to Issued
                String updateRequest = "UPDATE request_issue SET status = 'Issued' WHERE username = ? AND bookname = ?";
                PreparedStatement pst1 = con.prepareStatement(updateRequest);
                pst1.setString(1, username);
                pst1.setString(2, bookName);
                pst1.executeUpdate();

                // 2. Decrement book quantity
                String updateQuantity = "UPDATE availablebooks SET quantity = quantity - 1 WHERE name = ? AND quantity > 0";
                PreparedStatement pst2 = con.prepareStatement(updateQuantity);
                pst2.setString(1, bookName);
                pst2.executeUpdate();

                pst1.close();
                pst2.close();
                con.close();

                JOptionPane.showMessageDialog(this, "Request approved successfully!");
                loadRequestsToTable();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
            }
        });




        Container c=getContentPane();
        c.setLayout(null);
        c.add(mainPanel);

        loadRequestsToTable();

        setTitle("Issue Book");
        setSize(900, 700);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Absolute positioning
        setResizable(true); // Allow resizing
        setVisible(true);
    }

    public void loadRequestsToTable() {
        try {
            booksModel.setRowCount(0); // clear old rows

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704");

            String sql = "SELECT r.username, r.bookname, a.quantity, r.request_date, r.status " +
                    "FROM request_issue r " +
                    "JOIN availablebooks a ON r.bookname = a.name";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String username = rs.getString("username");
                String bookname = rs.getString("bookname");
                int quantity = rs.getInt("quantity");
                String date = rs.getString("request_date");
                String status = rs.getString("status");

                booksModel.addRow(new Object[]{username, bookname, quantity, date, status});
            }

            rs.close();
            pst.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading requests", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        new IssueBook();
    }
}
