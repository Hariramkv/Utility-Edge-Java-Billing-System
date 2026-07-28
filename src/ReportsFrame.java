import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReportsFrame extends JFrame implements ActionListener {

    JTable table;
    DefaultTableModel model;

    JLabel totalRevenueLbl, totalBillsLbl, pendingLbl;

    JButton refreshBtn, closeBtn;

    public ReportsFrame() {

        setTitle("Utility Edge - Reports");
        setSize(1250, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= HEADER =================
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(8,25,60));
        top.setBorder(new EmptyBorder(15,20,15,20));

        JLabel title = new JLabel("Revenue & Billing Reports");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));

        top.add(title, BorderLayout.WEST);

        add(top, BorderLayout.NORTH);

        // ================= CENTER =================
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(new Color(240,248,255));
        center.setBorder(new EmptyBorder(15,15,15,15));

        // Top Cards
        JPanel cards = new JPanel(new GridLayout(1,3,15,15));
        cards.setOpaque(false);

        totalRevenueLbl = valueLabel("₹ 0");
        totalBillsLbl   = valueLabel("0");
        pendingLbl      = valueLabel("0");

        cards.add(card("Total Revenue", totalRevenueLbl));
        cards.add(card("Bills Generated", totalBillsLbl));
        cards.add(card("Pending Bills", pendingLbl));

        center.add(cards, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel();

        model.setColumnIdentifiers(new String[] {
            "Bill ID","Customer ID","Month",
            "Total Amount","Status","Due Date"
        });

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 15));

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new EmptyBorder(15,0,15,0));

        center.add(sp, BorderLayout.CENTER);

        // Bottom Buttons
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);

        refreshBtn = ovalBtn("Refresh Report");
        closeBtn   = ovalBtn("Close");

        bottom.add(refreshBtn);
        bottom.add(Box.createHorizontalStrut(15));
        bottom.add(closeBtn);

        center.add(bottom, BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);

        refreshBtn.addActionListener(this);
        closeBtn.addActionListener(this);

        loadReport();

        setVisible(true);
    }

    // ================= BUTTON =================
    JButton ovalBtn(String txt) {

        JButton b = new JButton(txt) {

            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                if(getModel().isRollover())
                    g2.setColor(new Color(0,145,255));
                else
                    g2.setColor(new Color(8,25,60));

                g2.fillRoundRect(0,0,getWidth(),getHeight(),35,35);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        b.setPreferredSize(new Dimension(170,40));
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorder(new LineBorder(
                new Color(255,255,255,100),1,true));

        return b;
    }

    // ================= CARD =================
    JPanel card(String title, JLabel value) {

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(
                new LineBorder(new Color(220,230,240),2,true),
                new EmptyBorder(15,15,15,15)));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 18));
        t.setAlignmentX(Component.CENTER_ALIGNMENT);

        value.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(Box.createVerticalGlue());
        p.add(t);
        p.add(Box.createVerticalStrut(10));
        p.add(value);
        p.add(Box.createVerticalGlue());

        return p;
    }

    JLabel valueLabel(String txt) {

        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 28));
        l.setForeground(new Color(0,120,215));

        return l;
    }

    // ================= LOAD REPORT =================
    void loadReport() {

        model.setRowCount(0);

        try {

            Connection con = DBConnection.getConnection();

            ResultSet rs1 = con.createStatement()
                    .executeQuery("SELECT IFNULL(SUM(total_amount),0) FROM bills");

            if(rs1.next())
                totalRevenueLbl.setText("₹ " + rs1.getString(1));

            ResultSet rs2 = con.createStatement()
                    .executeQuery("SELECT COUNT(*) FROM bills");

            if(rs2.next())
                totalBillsLbl.setText(rs2.getString(1));

            ResultSet rs3 = con.createStatement()
                    .executeQuery("SELECT COUNT(*) FROM bills WHERE status='Pending'");

            if(rs3.next())
                pendingLbl.setText(rs3.getString(1));

            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT bill_id,customer_id,month,total_amount,status,due_date FROM bills");

            while(rs.next()) {

                model.addRow(new Object[] {
                    rs.getInt(1),
                    rs.getInt(2),
                    rs.getString(3),
                    rs.getDouble(4),
                    rs.getString(5),
                    rs.getDate(6)
                });
            }

        } catch(Exception e) {

            JOptionPane.showMessageDialog(this, e);
        }
    }

    // ================= ACTION =================
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()==refreshBtn)
            loadReport();

        if(e.getSource()==closeBtn)
            dispose();
    }

    public static void main(String[] args) {
        new ReportsFrame();
    }
}