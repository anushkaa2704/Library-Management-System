import rojeru_san.complementos.RSTableMetro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaulterList extends JFrame {
    DefaultTableModel model;

    public DefaulterList() {
        setTitle("Defaulter List");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel title = new JLabel("Defaulter Students");
        title.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 26));
        title.setBounds(260, 20, 300, 40);
        title.setForeground(new Color(102, 0, 102));
        add(title);

        RSTableMetro table = new RSTableMetro();
        table.setRowHeight(30);
        table.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
        table.setColorBackgoundHead(new Color(102, 0, 204));
        table.setColorFilasForeground1(Color.BLACK);
        table.setColorFilasForeground2(Color.BLACK);
        table.setColorFilasBackgound1(new Color(255, 230, 255));
        table.setColorFilasBackgound2(new Color(240, 220, 255));
        table.setSelectionBackground(new Color(153, 102, 255));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Username", "Book Name", "Issue Date", "Due Date", "Days Late"});
        table.setModel(model);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(50, 90, 680, 300);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scroll);

        loadDefaulterData();

        setVisible(true);
    }

    public void loadDefaulterData() {
        try {
            model.setRowCount(0); // clear table
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/librarymanagementsystem", "root", "Apatil#2704");

            String sql = "SELECT username, bookname, request_date, due_date FROM request_issue WHERE status = 'Issued'";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();

            while (rs.next()) {
                String user = rs.getString("username");
                String book = rs.getString("bookname");
                Date issueDate = rs.getDate("request_date");
                Date dueDate = rs.getDate("due_date");

                if (dueDate != null && dueDate.before(today)) {
                    long diffInMillies = today.getTime() - dueDate.getTime();
                    long daysLate = diffInMillies / (1000 * 60 * 60 * 24);

                    model.addRow(new Object[]{
                            user, book,
                            sdf.format(issueDate),
                            sdf.format(dueDate),
                            daysLate
                    });
                }
            }

            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading defaulter data.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new DefaulterList();
    }
}
