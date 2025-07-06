import rojeru_san.complementos.RSTableMetro;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.sql.*;

public class LoginPage extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;

    LoginPage()
    {
        Font f1 = new Font("Swiss721 BT", Font.PLAIN, 40);
        Font f2 = new Font("Calibri", Font.PLAIN, 18);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(102, 102, 255));
        rightPanel.setBounds(1060, 10, 460, 820);
        rightPanel.setLayout(null);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.white);
        leftPanel.setBounds(10, 10, 1040, 820);
        leftPanel.setLayout(null);

        JLabel l1 = new JLabel("Welcome to");
        l1.setFont(f1);
        l1.setBounds(360,60,300,60);
        l1.setForeground(new Color(255,51,51));

        JLabel l2 = new JLabel("Digital Library");
        l2.setFont(f1);
        l2.setBounds(345,120,300,60);
        l2.setForeground(new Color(102,102,255));

        try {
            InputStream is = getClass().getResourceAsStream("/icons/library-3.png.png"); // adjust path as per your jar
            if (is != null) {
                ImageIcon icon = new ImageIcon(ImageIO.read(is));
                JLabel imageLabel = new JLabel(icon);
                imageLabel.setBounds(0, 190, 990, 630); // 👈 your desired bounds
                leftPanel.add(imageLabel);
            } else {
                System.err.println("Image not found in JAR!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Font headingFont = new Font("Calibri", Font.BOLD, 28);
        Font subHeadingFont = new Font("Calibri", Font.PLAIN, 18);
        Font fieldFont = new Font("Calibri", Font.PLAIN, 16);

        JLabel heading = new JLabel("Login Page");
        heading.setFont(headingFont);
        heading.setForeground(Color.WHITE);
        heading.setBounds(140, 130, 300, 40);

        JLabel subHeading = new JLabel("Welcome, login to your account here");
        subHeading.setFont(subHeadingFont);
        subHeading.setForeground(Color.WHITE);
        subHeading.setBounds(85, 170, 300, 30);

        String[] labels = {"Username", "Password"};
        String[] iconPaths = {"/icons/icons8_Account_50px.png", "/icons/icons8_Secure_50px.png"};
        int y = 280;

        for (int i = 0; i < labels.length; i++) {
            try {
                InputStream iconStream = getClass().getResourceAsStream(iconPaths[i]);
                if (iconStream != null) {
                    ImageIcon icon = new ImageIcon(ImageIO.read(iconStream));
                    JLabel iconLabel = new JLabel(icon);
                    iconLabel.setBounds(40, y + 5, 45, 45);
                    rightPanel.add(iconLabel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            JLabel lbl = new JLabel(labels[i]);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(subHeadingFont);
            lbl.setBounds(100, y, 150, 20);
            rightPanel.add(lbl);

            JTextField tf;
            if (labels[i].equals("Password")) {
                tf = new JPasswordField();
                passwordField = (JPasswordField) tf;
            } else {
                tf = new JTextField();
                usernameField = tf; // since only "Username" left
            }

            tf.setFont(fieldFont);
            tf.setBounds(100, y + 20, 250, 30);
            tf.setBackground(new Color(102, 102, 255));
            tf.setForeground(Color.WHITE);
            tf.setCaretColor(Color.WHITE);
            tf.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
            rightPanel.add(tf);

            tf.setFont(fieldFont);
            tf.setBounds(100, y + 20, 250, 30);
            tf.setBackground(new Color(102, 102, 255));
            tf.setForeground(Color.WHITE);
            tf.setCaretColor(Color.WHITE);
            tf.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
            rightPanel.add(tf);

            y += 90;
        }

        JButton signupBtn = new JButton("Signup");
        signupBtn.setBounds(160, y+110, 130, 50);
        signupBtn.setBackground(new Color(255, 51, 51));
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setFont(f2);
        signupBtn.setFocusPainted(false);
        rightPanel.add(signupBtn);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(160, y+20, 130, 50);
        loginBtn.setBackground(new Color(0, 0, 204));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(f2);
        rightPanel.add(loginBtn);

        JLabel adminLabel =new JLabel("<html><u>Login as Admin instead?</u></html>");
        adminLabel.setBounds(130,y+180,300,40);
        adminLabel.setForeground(Color.WHITE);
        adminLabel.setFont(new Font("Swiss721 BT", Font.BOLD, 18));
        rightPanel.add(adminLabel);

        adminLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new AdminLogin();
                dispose();
            }
        });

        signupBtn.addActionListener(a->{
            new SignupPage();
            dispose();
        });

        loginBtn.addActionListener(a->
        {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
            } else {
                // Check database connection here
                try {
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704");
                    PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
                    pst.setString(1, username);
                    pst.setString(2, password);

                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Login successful!");
                        // Proceed to user dashboard
                        new UserDashboard(username); // if you have one
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid username or password.");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Database error.");
                }
            }
        });



        Container c = getContentPane();
        c.setLayout(null);

        c.add(rightPanel);
        c.add(leftPanel);
        leftPanel.add(l1);
        leftPanel.add(l2);
        rightPanel.add(heading);
        rightPanel.add(subHeading);

        setTitle("Login Page");
        setSize(1523, 828);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Absolute positioning
        setResizable(true); // Allow resizing
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginPage();

    }
}
