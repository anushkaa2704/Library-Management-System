import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import rojeru_san.complementos.RSTableMetro;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.awt.event.*;
import java.sql.*;


public class AdminDash extends JFrame {
    DefaultTableModel booksModel;
    DefaultTableModel studentModel;
    JLabel bookCountLabel;
    JLabel studentCountLabel;
    JLabel issuedCountLabel;
    JLabel defaulterCountLabel;

    AdminDash()
    {
        Font f1 = new Font("Swiss721 BT", Font.PLAIN, 40);
        Font f2 = new Font("Calibri", Font.PLAIN, 18);

        JPanel navpanel = new JPanel();
        navpanel.setBackground(new Color(102, 102, 255));
        navpanel.setBounds(0,0,1900,70);
        navpanel.setLayout(null);


        JLabel l1 = new JLabel();
        l1.setBounds(12, 12, 45, 45);
        try {
            InputStream is = getClass().getResourceAsStream("/adminIcons/icons8_menu_48px_1.png");
            if (is != null) {
                ImageIcon icon = new ImageIcon(ImageIO.read(is));
                Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                l1.setIcon(new ImageIcon(img));
            } else {
                System.err.println("menu-icon.png not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        navpanel.add(l1);

        JPanel p1=new JPanel();
        p1.setBounds(70,10,3,50);
        p1.setBackground(new Color(51,51,51));
        navpanel.add(p1);

        JLabel l2=new JLabel("Digital Library Management System");
        l2.setBounds(100,10,380,50);
        l2.setFont(new Font("Yu Gothic UI Light",Font.PLAIN,25));
        l2.setForeground(new Color(255,255,255));
        navpanel.add(l2);

        JLabel l4 = new JLabel("Welcome, Admin!");
        l4.setBounds(1280, 15, 250, 40);// Right-aligned
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

        // Sidebar Panel
        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(new Color(51, 51, 51));
        sidePanel.setBounds(0, 70, 270, 900); // Below navbar
        sidePanel.setLayout(null);

        // Example Label: Home Page
        JLabel homeLabel = new JLabel("  Home Page");
        homeLabel.setBounds(0, 20, 270, 50);
        homeLabel.setForeground(Color.WHITE);
        homeLabel.setFont(new Font("Yu Gothic UI", Font.BOLD, 18));
        homeLabel.setOpaque(true);
        homeLabel.setBackground(new Color(255, 51, 51)); // Active background

        try {
            InputStream iconStream = getClass().getResourceAsStream("/adminIcons/icons8_Home_26px_2.png");
            if (iconStream != null) {
                ImageIcon icon = new ImageIcon(ImageIO.read(iconStream));
                Image img = icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                homeLabel.setIcon(new ImageIcon(img));
                homeLabel.setHorizontalAlignment(SwingConstants.LEFT);
                homeLabel.setIconTextGap(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add label to sidebar
        sidePanel.add(homeLabel);

        JLabel DLMSLabel = new JLabel("  DLMS Dashboard");
        DLMSLabel.setBounds(0, 80, 270, 50);
        DLMSLabel.setForeground(Color.WHITE);
        DLMSLabel.setFont(new Font("Yu Gothic UI", Font.BOLD, 18));
        DLMSLabel.setOpaque(true);
        DLMSLabel.setBackground(new Color(51, 51, 51)); // Active background

        try {
            InputStream iconStream = getClass().getResourceAsStream("/adminIcons/icons8_Library_26px_1.png");
            if (iconStream != null) {
                ImageIcon icon = new ImageIcon(ImageIO.read(iconStream));
                Image img = icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                DLMSLabel.setIcon(new ImageIcon(img));
                DLMSLabel.setHorizontalAlignment(SwingConstants.LEFT);
                DLMSLabel.setIconTextGap(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add label to sidebar
        sidePanel.add(DLMSLabel);

        JLabel featurelabel =new JLabel(" Features");
        featurelabel.setBounds(0,140,270,50);
        featurelabel.setForeground(Color.WHITE);
        featurelabel.setFont(new Font("Yu Gothic UI", Font.BOLD, 18));
        featurelabel.setOpaque(true);
        featurelabel.setBackground(new Color(51, 51, 51));
        sidePanel.add(featurelabel);


        String[] menuLabels = {
                 "Manage Books", "Manage Students", "Issue Book", "View Issued Books", "Defaulter List"
        };

        String[] menuIcons = {
                "/adminIcons/icons8_Book_26px.png",
                "/adminIcons/icons8_Read_Online_26px.png",
                "/adminIcons/icons8_Sell_26px.png",
                "/adminIcons/icons8_Books_26px.png",
                "/adminIcons/icons8_Conference_26px.png"
        };

        int y = 200;  // Start from below the navbar

        for (int i = 0; i < menuLabels.length; i++) {
            int index=i;
            JLabel menuItem = new JLabel("  " + menuLabels[i]);  // Padding left
            menuItem.setBounds(0, y, 270, 50);
            menuItem.setForeground(Color.WHITE);
            menuItem.setFont(new Font("Yu Gothic UI", Font.BOLD, 18));
            menuItem.setOpaque(true);
            menuItem.setBackground(new Color(51, 51, 51));
            menuItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            try {
                InputStream iconStream = getClass().getResourceAsStream(menuIcons[i]);
                if (iconStream != null) {
                    ImageIcon icon = new ImageIcon(ImageIO.read(iconStream));
                    Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                    menuItem.setIcon(new ImageIcon(img));
                } else {
                    System.err.println("Icon not found: " + menuIcons[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            menuItem.addMouseListener(new MouseAdapter() {
                                          @Override
                                          public void mouseClicked(MouseEvent e) {
                                              switch (menuLabels[index]) {
                                                  case "Manage Books":
                                                      new ManageBooks();
                                                      dispose();// Call your method
                                                      break;
                                                  case "Manage Students":
                                                      new ManageStudents();
                                                      dispose();
                                                      break;
                                                  case "Issue Book":
                                                      new IssueBook();
                                                      dispose();
                                                      break;
                                                  default:
                                                      JOptionPane.showMessageDialog(null, "Unknown action.");
                                              }
                                          }
            });

            // Optional: Add mouse listener if you want action on click
            // final int index = i;
            // menuItem.addMouseListener(new MouseAdapter() {
            //     public void mouseClicked(MouseEvent e) {
            //         System.out.println("Clicked: " + menuLabels[index]);
            //     }
            // });

            sidePanel.add(menuItem);  // Make sure leftPanel is defined and added to container
            y += 60;  // Vertical gap between items
        }

        JLabel logout = new JLabel("  Logout");
        logout.setBounds(0, 500, 270, 60);
        logout.setForeground(Color.WHITE);
        logout.setFont(new Font("Yu Gothic UI", Font.BOLD, 18));
        logout.setOpaque(true);
        logout.setBackground(new Color(102, 102, 255)); // Active background

        logout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LoginPage();
                dispose();
            }
        });

        try {
            InputStream iconStream = getClass().getResourceAsStream("/adminIcons/icons8_Exit_26px_2.png");
            if (iconStream != null) {
                ImageIcon icon = new ImageIcon(ImageIO.read(iconStream));
                Image img = icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                logout.setIcon(new ImageIcon(img));
                logout.setHorizontalAlignment(SwingConstants.LEFT);
                logout.setIconTextGap(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add label to sidebar
        sidePanel.add(logout);

        JPanel mainpanel = new JPanel();
        mainpanel.setBounds(270,50,1630,900);
        mainpanel.setLayout(null);
        mainpanel.setBackground(new Color(255,255,255));
        int panelWidth = 250;
        int panelHeight = 140;
        int gap = 57;
        int startX = 50;
        int startY = 50;

        String[] topLabels = { "No. of Books", "No. of Students", "Issued Books", "Defaulter List" };
        String[] iconPaths = {
                "/adminIcons/icons8_Book_Shelf_50px.png",
                "/adminIcons/icons8_People_50px.png",
                "/adminIcons/icons8_Sell_50px.png",
                "/adminIcons/icons8_List_of_Thumbnails_50px.png"
        };

        for (int i = 0; i < 4; i++) {
            // Top label
            JLabel titleLabel = new JLabel(topLabels[i]);
            titleLabel.setFont(new Font("Yu Gothic UI", Font.BOLD, 18));
            titleLabel.setBounds(startX + i * (panelWidth + gap), startY - 30, panelWidth, 30);
            mainpanel.add(titleLabel);

            // Panel
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(null);
            infoPanel.setBounds(startX + i * (panelWidth + gap), startY, panelWidth, panelHeight);
            infoPanel.setBackground(new Color(220, 220, 220));
            infoPanel.setBorder(BorderFactory.createMatteBorder(5, 0, 0, 0, new Color(51, 51, 255)));

            // Icon
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

            // Number
            JLabel numberLabel = new JLabel("10"); // You can replace "10" dynamically later
            numberLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 40));
            numberLabel.setBounds(120, 40, 100, 60);
            if (i == 0) bookCountLabel = numberLabel;
            else if (i == 1) studentCountLabel = numberLabel;
            else if (i == 2) issuedCountLabel = numberLabel;
            else if (i == 3) defaulterCountLabel = numberLabel;

            infoPanel.add(iconLabel);
            infoPanel.add(numberLabel);

            mainpanel.add(infoPanel);
        }



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


        studentModel = new DefaultTableModel();
        studentModel.setColumnIdentifiers(new Object[]{"Student ID", "Name", "Branch"});
        loadStudentsToTable(studentModel);
        studentTable.setModel(studentModel);


        JScrollPane studentScroll = new JScrollPane(studentTable);
        studentScroll.setBounds(50, 250, 750, 180); // ↓ reduced height here
        studentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainpanel.add(studentScroll);

        RSTableMetro bookTable = new RSTableMetro();
        bookTable.setRowHeight(30);
        bookTable.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
        bookTable.setColorBackgoundHead(new Color(51, 51, 255));
        bookTable.setColorFilasForeground1(Color.BLACK);
        bookTable.setColorFilasForeground2(Color.BLACK);
        bookTable.setColorFilasBackgound1(new Color(240, 255, 240));
        bookTable.setColorFilasBackgound2(new Color(220, 255, 220));
        bookTable.setSelectionBackground(new Color(102, 102, 255));
        bookTable.setShowHorizontalLines(true);
        bookTable.setShowVerticalLines(true);

        booksModel = new DefaultTableModel();
        booksModel.setColumnIdentifiers(new Object[]{"Book Id","Book Name", "Author", "Quantity"});
        loadBooksToTable(booksModel); // Load from database


        bookTable.setModel(booksModel);

// ScrollPane for book table with smaller height
        JScrollPane bookScroll = new JScrollPane(bookTable);
        bookScroll.setBounds(50, 500, 750, 180); // ↓ reduced height here
        bookScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainpanel.add(bookScroll);






        Container c = getContentPane();
        c.setLayout(null);

        c.add(navpanel);
        c.add(sidePanel);
        c.add(mainpanel);


        setTitle("Admin Dashboard");
        setSize(1900, 1000);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Absolute positioning
        setResizable(true); // Allow resizing
        setVisible(true);

        bookCountLabel.setText(String.valueOf(getTotalBooks()));
        studentCountLabel.setText(String.valueOf(getTotalStudents()));
        issuedCountLabel.setText(String.valueOf(getIssuedBooksCount()));
        defaulterCountLabel.setText(String.valueOf(getDefaulterCount()));
    }

    public static void main(String[] args) {
        new AdminDash();
    }

    public void loadBooksToTable(DefaultTableModel model) {
        try {
            model.setRowCount(0); // Clear previous data

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704");

            String sql = "SELECT book_id, name, author, quantity FROM availablebooks";
            PreparedStatement pst = con.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String id=rs.getString("book_id");
                String name = rs.getString("name");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");

                model.addRow(new Object[]{id,name, author, quantity});
            }

            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load books", "Error", JOptionPane.ERROR_MESSAGE);
        }


    }
    public void loadStudentsToTable(DefaultTableModel model) {
        try {
            model.setRowCount(0); // Clear existing rows

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704");

            String sql = "SELECT id,username, branch FROM users";
            PreparedStatement pst = con.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String username = rs.getString("username");
                String branch = rs.getString("branch");

                // Assuming course is "BE" for now (since not in DB)
                model.addRow(new Object[]{id,username, branch});
            }

            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load student data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public int getTotalBooks() {
        int count = 0;
        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704")) {

            String sql = "SELECT COUNT(*) FROM availablebooks";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getTotalStudents() {
        int count = 0;
        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704")) {

            String sql = "SELECT COUNT(*) FROM users";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getIssuedBooksCount() {
        int count = 0;
        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704")) {

            String sql = "SELECT COUNT(*) FROM request_issue WHERE status = 'Issued'";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getDefaulterCount() {
        int count = 0;
        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704")) {

            String sql = "SELECT COUNT(*) FROM request_issue WHERE status = 'Issued' AND DATEDIFF(CURDATE(), request_date) > 14";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


}

//full panel -> 1900 x 1000
//nav panel -> 1900 x 70
//side panel -> 270 x 900
//main panel -> 1630 x 900
