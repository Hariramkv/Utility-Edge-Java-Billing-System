import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddCustomerFrame extends JFrame implements ActionListener {

    JTextField nameField, phoneField, addressField, meterField, userField;
    JPasswordField passField;

    JButton saveBtn, clearBtn, closeBtn;

    public AddCustomerFrame() {

        setTitle("Utility Edge - Add Customer");
        setSize(820, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= HEADER =================
        JPanel top = new JPanel();
        top.setBackground(new Color(8,25,60));
        top.setBorder(new EmptyBorder(18,20,18,20));

        JLabel title = new JLabel("Customer Registration");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));

        top.add(title);

        add(top, BorderLayout.NORTH);

        // ================= MAIN =================
        GradientPanel main = new GradientPanel();
        main.setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(560, 470));
        card.setBackground(new Color(255,255,255,230));
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220,230,240),2,true),
                new EmptyBorder(20,20,20,20)));

        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField    = field();
        phoneField   = field();
        addressField = field();
        meterField   = field();
        userField    = field();
        passField    = new JPasswordField();
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        addRow(card, gbc, 0, "Customer Name", nameField);
        addRow(card, gbc, 1, "Phone Number", phoneField);
        addRow(card, gbc, 2, "Address", addressField);
        addRow(card, gbc, 3, "Meter Number", meterField);
        addRow(card, gbc, 4, "Username", userField);
        addRow(card, gbc, 5, "Password", passField);

        // Buttons
        saveBtn  = ovalBtn("Save");
        clearBtn = ovalBtn("Clear");
        closeBtn = ovalBtn("Close");

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);

        btnPanel.add(saveBtn);
        btnPanel.add(clearBtn);
        btnPanel.add(closeBtn);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;

        card.add(btnPanel, gbc);

        main.add(card);

        add(main, BorderLayout.CENTER);

        saveBtn.addActionListener(this);
        clearBtn.addActionListener(this);
        closeBtn.addActionListener(this);

        setVisible(true);
    }

    // ================= ROW =================
    void addRow(JPanel p, GridBagConstraints gbc,
                int y, String text, JComponent comp) {

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;

        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 17));

        p.add(l, gbc);

        gbc.gridx = 1;
        comp.setPreferredSize(new Dimension(260,38));

        p.add(comp, gbc);
    }

    JTextField field() {

        JTextField t = new JTextField();
        t.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        return t;
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

        b.setPreferredSize(new Dimension(140,40));
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorder(new LineBorder(
                new Color(255,255,255,100),1,true));

        return b;
    }

    // ================= SAVE =================
    void saveCustomer() {

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO customers(name,phone,address,meter_no,username,password) VALUES(?,?,?,?,?,?)"
            );

            ps.setString(1, nameField.getText());
            ps.setString(2, phoneField.getText());
            ps.setString(3, addressField.getText());
            ps.setString(4, meterField.getText());
            ps.setString(5, userField.getText());
            ps.setString(6, new String(passField.getPassword()));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Customer Added Successfully");

            clearFields();

        } catch(Exception e) {

            JOptionPane.showMessageDialog(this, e);
        }
    }

    void clearFields() {

        nameField.setText("");
        phoneField.setText("");
        addressField.setText("");
        meterField.setText("");
        userField.setText("");
        passField.setText("");
    }

    // ================= ACTION =================
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()==saveBtn)
            saveCustomer();

        if(e.getSource()==clearBtn)
            clearFields();

        if(e.getSource()==closeBtn)
            dispose();
    }

    // ================= BG =================
    class GradientPanel extends JPanel {

        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;

            GradientPaint gp = new GradientPaint(
                    0,0,new Color(0,95,180),
                    getWidth(),getHeight(),
                    new Color(0,180,130));

            g2.setPaint(gp);
            g2.fillRect(0,0,getWidth(),getHeight());
        }
    }

    public static void main(String[] args) {
        new AddCustomerFrame();
    }
}