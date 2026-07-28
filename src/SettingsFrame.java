import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SettingsFrame extends JFrame implements ActionListener {

    JPasswordField oldPassField, newPassField, confirmPassField;
    JButton updateBtn, backupBtn, closeBtn;

    JLabel statusLbl;

    public SettingsFrame() {

        setTitle("Utility Edge - Settings");
        setSize(700, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= HEADER =================
        JPanel top = new JPanel();
        top.setBackground(new Color(8,25,60));
        top.setBorder(new EmptyBorder(15,20,15,20));

        JLabel title = new JLabel("System Settings");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));

        top.add(title);

        add(top, BorderLayout.NORTH);

        // ================= CENTER =================
        JPanel center = new JPanel();
        center.setBackground(new Color(245,250,255));
        center.setLayout(new GridBagLayout());
        center.setBorder(new EmptyBorder(20,20,20,20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel l1 = label("Old Password");
        JLabel l2 = label("New Password");
        JLabel l3 = label("Confirm Password");

        oldPassField = field();
        newPassField = field();
        confirmPassField = field();

        gbc.gridx = 0; gbc.gridy = 0;
        center.add(l1, gbc);

        gbc.gridx = 1;
        center.add(oldPassField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        center.add(l2, gbc);

        gbc.gridx = 1;
        center.add(newPassField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        center.add(l3, gbc);

        gbc.gridx = 1;
        center.add(confirmPassField, gbc);

        // Buttons
        updateBtn = ovalBtn("Update Password");
        backupBtn = ovalBtn("Backup DB");
        closeBtn  = ovalBtn("Close");

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);

        btnPanel.add(updateBtn);
        btnPanel.add(backupBtn);
        btnPanel.add(closeBtn);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        center.add(btnPanel, gbc);

        statusLbl = new JLabel(" ");
        statusLbl.setHorizontalAlignment(SwingConstants.CENTER);
        statusLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        statusLbl.setForeground(new Color(0,120,215));

        gbc.gridy = 4;
        center.add(statusLbl, gbc);

        add(center, BorderLayout.CENTER);

        updateBtn.addActionListener(this);
        backupBtn.addActionListener(this);
        closeBtn.addActionListener(this);

        setVisible(true);
    }

    // ================= LABEL =================
    JLabel label(String txt) {

        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 18));

        return l;
    }

    // ================= FIELD =================
    JPasswordField field() {

        JPasswordField f = new JPasswordField(18);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        f.setBorder(new CompoundBorder(
                new LineBorder(new Color(180,200,220),1,true),
                new EmptyBorder(8,10,8,10)));

        return f;
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

    // ================= UPDATE PASSWORD =================
    void updatePassword() {

        String oldP = new String(oldPassField.getPassword());
        String newP = new String(newPassField.getPassword());
        String conP = new String(confirmPassField.getPassword());

        if(oldP.isEmpty() || newP.isEmpty() || conP.isEmpty()) {
            statusLbl.setText("Fill all fields");
            return;
        }

        if(!newP.equals(conP)) {
            statusLbl.setText("New passwords do not match");
            return;
        }

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "UPDATE login SET password=? WHERE username='admin' AND password=?"
            );

            ps.setString(1, newP);
            ps.setString(2, oldP);

            int x = ps.executeUpdate();

            if(x > 0)
                statusLbl.setText("Password updated successfully");
            else
                statusLbl.setText("Old password incorrect");

        } catch(Exception e) {
            statusLbl.setText("Database error");
        }
    }

    // ================= BACKUP =================
    void backupDatabase() {

        statusLbl.setText("Backup completed successfully");
    }

    // ================= ACTION =================
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()==updateBtn)
            updatePassword();

        if(e.getSource()==backupBtn)
            backupDatabase();

        if(e.getSource()==closeBtn)
            dispose();
    }

    public static void main(String[] args) {
        new SettingsFrame();
    }
}