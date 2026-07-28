import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewBillsFrame extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;

    JTextField searchField;

    JButton searchBtn, refreshBtn, paidBtn, deleteBtn;

    public ViewBillsFrame() {

        setTitle("View Bills");
        setSize(1250, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(0,95,180));
        top.setBorder(new EmptyBorder(15,20,15,20));

        JLabel title = new JLabel("Bill Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);

        top.add(title, BorderLayout.WEST);
        add(top, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel();

        model.setColumnIdentifiers(new String[] {
            "Bill ID","Customer ID","Month",
            "Electricity","Water","Gas",
            "Internet","Total Amount",
            "Status","Due Date"
        });

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 15)
        );

        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        // Bottom controls
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        bottom.setBackground(new Color(245,250,255));

        searchField = new JTextField(20);

        searchBtn = button("Search");
        refreshBtn = button("Refresh");
        paidBtn = button("Mark Paid");
        deleteBtn = button("Delete");

        searchBtn.addActionListener(this);
        refreshBtn.addActionListener(this);
        paidBtn.addActionListener(this);
        deleteBtn.addActionListener(this);

        bottom.add(new JLabel("Customer ID / Month"));
        bottom.add(searchField);
        bottom.add(searchBtn);
        bottom.add(refreshBtn);
        bottom.add(paidBtn);
        bottom.add(deleteBtn);

        add(bottom, BorderLayout.SOUTH);

        loadBills();

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

    // Load all bills
    void loadBills() {

        model.setRowCount(0);

        try {

            Connection con = DBConnection.getConnection();

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(
                "SELECT * FROM bills"
            );

            while(rs.next()) {

                model.addRow(new Object[] {
                    rs.getInt("bill_id"),
                    rs.getInt("customer_id"),
                    rs.getString("month"),
                    rs.getInt("electricity_units"),
                    rs.getInt("water_units"),
                    rs.getInt("gas_units"),
                    rs.getDouble("internet_charge"),
                    rs.getDouble("total_amount"),
                    rs.getString("status"),
                    rs.getDate("due_date")
                });
            }

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    // Search
    void searchBill() {

        model.setRowCount(0);

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM bills WHERE customer_id LIKE ? OR month LIKE ?"
            );

            ps.setString(1, "%" + searchField.getText() + "%");
            ps.setString(2, "%" + searchField.getText() + "%");

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                model.addRow(new Object[] {
                    rs.getInt("bill_id"),
                    rs.getInt("customer_id"),
                    rs.getString("month"),
                    rs.getInt("electricity_units"),
                    rs.getInt("water_units"),
                    rs.getInt("gas_units"),
                    rs.getDouble("internet_charge"),
                    rs.getDouble("total_amount"),
                    rs.getString("status"),
                    rs.getDate("due_date")
                });
            }

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    // Mark Paid
    void markPaid() {

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
                "UPDATE bills SET status='Paid' WHERE bill_id=?"
            );

            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Payment Updated");

            loadBills();

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    // Delete
    void deleteBill() {

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
                "DELETE FROM bills WHERE bill_id=?"
            );

            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Bill Deleted");

            loadBills();

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == searchBtn)
            searchBill();

        if(e.getSource() == refreshBtn)
            loadBills();

        if(e.getSource() == paidBtn)
            markPaid();

        if(e.getSource() == deleteBtn)
            deleteBill();
    }

    public static void main(String[] args) {
        new ViewBillsFrame();
    }
}