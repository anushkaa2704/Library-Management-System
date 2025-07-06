import rojeru_san.complementos.RSTableMetro;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

public class ManageStudents extends JFrame {
    JTextField  studentNameField,studentEmailField, studentBranchField;
    DefaultTableModel studentModel;
    ManageStudents()
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

        JLabel headinglabel=new JLabel("Manage Students");
        headinglabel.setFont(headingFont);
        headinglabel.setBounds(300,60,450,45);
        rightPanel.add(headinglabel);

        String[] labels = {"Enter Student Name:", "Email","Branch"};
        String[] iconPaths = {"/AddNewBookIcons/icons8_Contact_26px.png", "/AddNewBookIcons/icons8_Moleskine_26px.png", "/AddNewBookIcons/icons8_Collaborator_Male_26px.png"};
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
            lbl.setBounds(100, y, 180, 20);
            leftPanel.add(lbl);

            JTextField tf;
            if(i == 0) tf = studentNameField = new JTextField();
            else if(i == 1) tf = studentEmailField = new JTextField();
            else tf = studentBranchField = new JTextField();

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

        studentModel = new DefaultTableModel();
        studentModel.setColumnIdentifiers(new Object[]{"Student ID", "Name", "email","Branch"});

        RSTableMetro studentTable = new RSTableMetro();
        studentTable.setRowHeight(30);
        studentTable.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
        studentTable.setColorBackgoundHead(new Color(51, 51, 255));
        studentTable.setColorFilasForeground1(Color.BLACK);
        studentTable.setColorFilasForeground2(Color.BLACK);
        studentTable.setColorFilasBackgound1(new Color(230, 230, 255));
        studentTable.setColorFilasBackgound2(new Color(210, 210, 255));
        studentTable.setSelectionBackground(new Color(102, 102, 255));
        studentTable.setShowHorizontalLines(true);
        studentTable.setShowVerticalLines(true);
        studentTable.setModel(studentModel);


        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBounds(65, 250, 600, 220); // ↓ reduced height here
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        rightPanel.add(scrollPane);

        addBtn.addActionListener(a->
        {
            String username = studentNameField.getText().trim();
            String email = studentEmailField.getText().trim();
            String branch = studentBranchField.getText().trim();

            if (username.isEmpty() || email.isEmpty() || branch.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
            model.addRow(new Object[]{username, email, branch});

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                java.sql.Connection con = java.sql.DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704");

                String sql = "INSERT INTO users (username, password, email, branch) VALUES (?, ?, ?,?)";
                java.sql.PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, username);
                pst.setString(2, "student123");
                pst.setString(3, email);
                pst.setString(4, branch);

                int row = pst.executeUpdate();
                if (row > 0) {
                    JOptionPane.showMessageDialog(this, "Student Added Successfully");
                    loadStudentsToTable(studentModel);
                }

                pst.close();
                con.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error Adding Student", "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        loadStudentsToTable(studentModel);

        deleteBtn.addActionListener(a->
        {
            int selectedRow = studentTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a student to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String id = studentModel.getValueAt(selectedRow, 0).toString();
            String url="jdbc:mysql://localhost:3306/librarymanagementsystem";
            try(Connection conn=DriverManager.getConnection(url,"root","Apatil#2704"))
            {
                String sql="DELETE FROM users WHERE id = ?;";
                try(PreparedStatement pst = conn.prepareStatement(sql)) {
                    pst.setString(1, id);
                    int rowsAffected = pst.executeUpdate();
                    if (rowsAffected > 0) {
                        studentModel.removeRow(selectedRow); // Remove row from table
                        JOptionPane.showMessageDialog(null, "Student deleted successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });




        Container c=getContentPane();
        c.setLayout(null);
        c.add(leftPanel);
        c.add(rightPanel);


        setTitle("Manage Students");
        setSize(1200, 800);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Absolute positioning
        setResizable(true); // Allow resizing
        setVisible(true);
    }

    public void loadStudentsToTable(DefaultTableModel model) {
        try {
            model.setRowCount(0); // Clear existing rows
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704");

            String sql = "SELECT id, username,email, branch FROM users";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("branch")
                });
            }

            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load students", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ManageStudents();
    }
}
