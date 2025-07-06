import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.sql.*;



public class SignupPage extends JFrame {
    JTextField usernameField, emailField, contactField;
    JPasswordField passwordField;

    SignupPage(){
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
            InputStream is = getClass().getResourceAsStream("/icons/signup-library-icon.png"); // adjust path as per your jar
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

        JLabel heading = new JLabel("Signup Page");
        heading.setFont(headingFont);
        heading.setForeground(Color.WHITE);
        heading.setBounds(140, 50, 300, 40);

        JLabel subHeading = new JLabel("Create New Account Here");
        subHeading.setFont(subHeadingFont);
        subHeading.setForeground(Color.WHITE);
        subHeading.setBounds(125, 90, 300, 30);

        String[] labels = {"Username", "Password", "Email", "Branch"};
        String[] iconPaths = {"/icons/icons8_Account_50px.png", "/icons/icons8_Secure_50px.png", "/icons/icons8_Secured_Letter_50px.png", "/AddNewBookIcons/icons8_Literature_100px_1.png"};
        int y = 180;
        JTextField[] fields = new JTextField[4];


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

            JTextField tf = labels[i].equals("Password") ? new JPasswordField() : new JTextField();
            fields[i] = tf;
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
        signupBtn.setBounds(160, y+20, 130, 50);
        signupBtn.setBackground(new Color(255, 51, 51));
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setFocusPainted(false);
        signupBtn.setFont(f2);
        rightPanel.add(signupBtn);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(160, y+110, 130, 50);
        loginBtn.setBackground(new Color(0, 0, 204));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(f2);
        loginBtn.setFocusPainted(false);
        rightPanel.add(loginBtn);

        signupBtn.addActionListener(a->{
            String username = fields[0].getText().trim();
            String password = new String(((JPasswordField) fields[1]).getPassword()).trim();
            String email = fields[2].getText().trim();
            String branch = fields[3].getText().trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || branch.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704");
                PreparedStatement ps = con.prepareStatement("INSERT INTO users (username, password, email, branch) VALUES (?, ?, ?, ?)");
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, email);
                ps.setString(4, branch);

                int result = ps.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(null, "Signup successful!");
                    new UserDashboard(username);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Signup failed.");
                }

                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
            }

        });

        loginBtn.addActionListener(a->{
            new LoginPage();
            dispose();
        });

        Container c = getContentPane();
        c.setLayout(null);

        c.add(rightPanel);
        c.add(leftPanel);
        leftPanel.add(l1);
        leftPanel.add(l2);
        rightPanel.add(heading);
        rightPanel.add(subHeading);

        setTitle("Signup Page");
        setSize(1523, 828);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Absolute positioning
        setResizable(true); // Allow resizing
        setVisible(true);
    }

    public static void main(String[] args) {
        SignupPage s=new SignupPage();
    }
}
