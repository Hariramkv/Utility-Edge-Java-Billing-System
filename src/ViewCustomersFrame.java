import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewCustomersFrame extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;

    JTextField searchField;

    JButton searchBtn, refreshBtn, deleteBtn;

    public ViewCustomersFrame() {

        setTitle("View Customers");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(0,95,180));
        top.setBorder(new EmptyBorder(15,20,15,20));

        JLabel title = new JLabel("Customer Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);

        top.add(title, BorderLayout.WEST);

        add(top, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        searchPanel.setBackground(new Color(245,250,255));

        searchField = new JTextField(20);

        searchBtn = button("Search");
        refreshBtn = button("Refresh");
        deleteBtn = button("Delete");

        searchBtn.addActionListener(this);
        refreshBtn.addActionListener(this);
        deleteBtn.addActionListener(this);

        searchPanel.add(new JLabel("Search Name / Meter No"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(refreshBtn);
        searchPanel.add(deleteBtn);

        add(searchPanel, BorderLayout.SOUTH);

        // Table
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[] {
                "ID","Name","Phone","Address",
                "Meter No","Username"
        });

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 16));

        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        loadCustomers();

        setVisible(true);
    }

    JButton button(String txt) {

        JButton b = new JButton(txt);
        b.setFont(new Font("Segoe UI", Font.BOLD, 15));
        b.setBackground(new Color(0,120,215));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);

        return b;
    }

    // Load All Customers
    void loadCustomers() {

        model.setRowCount(0);

        try {

            Connection con = DBConnection.getConnection();

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(
                "SELECT id,name,phone,address,meter_no,username FROM customers"
            );

            while(rs.next()) {

                model.addRow(new Object[] {
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6)
                });
            }

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    // Search
    void searchCustomer() {

        model.setRowCount(0);

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "SELECT id,name,phone,address,meter_no,username FROM customers WHERE name LIKE ? OR meter_no LIKE ?"
            );

            ps.setString(1, "%" + searchField.getText() + "%");
            ps.setString(2, "%" + searchField.getText() + "%");

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                model.addRow(new Object[] {
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6)
                });
            }

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    // Delete
    void deleteCustomer() {

        int row = table.getSelectedRow();

        if(row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select a row first");
            return;
        }

        int id = Integer.parseInt(
                model.getValueAt(row,0).toString());

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "DELETE FROM customers WHERE id=?"
            );

            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Customer Deleted");

            loadCustomers();

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == searchBtn)
            searchCustomer();

        if(e.getSource() == refreshBtn)
            loadCustomers();

        if(e.getSource() == deleteBtn)
            deleteCustomer();
    }

    public static void main(String[] args) {
        new ViewCustomersFrame();
    }
}